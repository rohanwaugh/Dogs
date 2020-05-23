package com.android.dogs.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.dogs.model.DogBreed
import com.android.dogs.network.DogsService
import com.android.dogs.room.DogDatabase
import com.android.dogs.utils.SharedPreferenceHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class DogsViewModel(application: Application) : BaseViewModel(application) {

    private val prefsHelper = SharedPreferenceHelper(getApplication())
    private val refreshTime = 5 * 60 * 1000 * 1000 * 1000L

    private val dogsService = DogsService()
    private val disposable = CompositeDisposable()

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        val updateTime = prefsHelper.getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDatabase()
        } else {
            fetchFromRemote()
        }
    }

    fun refreshBypassCache(){
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
                        Toast.makeText(
                            getApplication(),
                            "Dogs retrieved from endpoint",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onError(e: Throwable) {
                        loading.value = false
                        dogsLoadError.value = true
                        Log.d("Error", e.printStackTrace().toString())
                    }

                })
        )
    }

    private fun fetchFromDatabase() {
        loading.value = true

        launch {
            val dogsList = DogDatabase(getApplication()).dogDao().getAllDogs()
            dogsRetrieved(dogsList)
            Toast.makeText(getApplication(), "Dogs retrieved from database", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun dogsRetrieved(dogsList: List<DogBreed>) {
        dogs.value = dogsList
        loading.value = false
        dogsLoadError.value = false
    }

    private fun storeDogToRoomDatabase(dogsList: List<DogBreed>) {
        launch {
            val dao = DogDatabase(getApplication()).dogDao()
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