package com.android.dogs.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.dogs.R
import com.android.dogs.model.DogBreed
import com.android.dogs.network.DogsService
import com.android.dogs.room.DogDatabase
import com.android.dogs.utils.NotificationsHelper
import com.android.dogs.utils.REFRESH_TIME
import com.android.dogs.utils.SharedPreferenceHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class DogsViewModel(application: Application) : BaseViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private val prefsHelper = SharedPreferenceHelper(context)

    private val dogsService = DogsService()
    private val disposable = CompositeDisposable()

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        val updateTime = prefsHelper.getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < REFRESH_TIME) {
            fetchFromDatabase()
        } else {
            fetchFromRemote()
        }
    }

    fun refreshBypassCache() {
        fetchFromRemote()
    }

    private fun fetchFromRemote() {
        loading.value = true
        disposable.add(
            dogsService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>() {
                    override fun onSuccess(dogsList: List<DogBreed>) {
                        storeDogToRoomDatabase(dogsList)
                        NotificationsHelper(context).createNotification(context.getString(R.string.endpoint_notification_text))
                    }

                    override fun onError(e: Throwable) {
                        loading.value = false
                        dogsLoadError.value = true
                    }

                })
        )
    }

    private fun fetchFromDatabase() {
        loading.value = true

        launch {
            val dogsList = DogDatabase(context).dogDao().getAllDogs()
            dogsRetrieved(dogsList)
            NotificationsHelper(context).createNotification(context.getString(R.string.database_notification_text))
        }
    }

    private fun dogsRetrieved(dogsList: List<DogBreed>) {
        dogs.value = dogsList
        loading.value = false
        dogsLoadError.value = false
    }

    private fun storeDogToRoomDatabase(dogsList: List<DogBreed>) {
        launch {
            val dao = DogDatabase(context).dogDao()
            dao.deleteAllDogs()
            val result = dao.insertAll(*dogsList.toTypedArray())
            var i = 0
            while (i < dogsList.size) {
                dogsList[i].uuid = result[i].toInt()
                ++i
            }
            dogsRetrieved(dogsList)
        }
        prefsHelper.saveUpdateTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}