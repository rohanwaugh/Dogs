package com.android.dogs.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.android.dogs.R
import com.android.dogs.model.DogBreed
import com.android.dogs.network.DogsService
import com.android.dogs.room.DogDatabase
import com.android.dogs.utils.NotificationsHelper
import com.android.dogs.utils.SharedPreferenceHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.lang.NumberFormatException

/* This is ViewModel for ListFragment. */
class DogsViewModel(application: Application) : BaseViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private val prefsHelper = SharedPreferenceHelper(context)
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L

    private val dogsService = DogsService()
    private val disposable = CompositeDisposable()

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    /* This method is called from ListFragment and will fetch Dogs data either from
    *  endPoint (using Retrofit) or from database(room) based on if Cache is expired or not.
    *  */
    fun refresh() {
        checkCacheDuration()
        val updateTime = prefsHelper.getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDatabase()
        } else {
            fetchFromRemote()
        }
    }

    /* This function will read Cache duration from SharedPreference and update the refreshTime*/
    private fun checkCacheDuration() {
        val cachePreference = prefsHelper.getCacheDuration()
        try {
            val cachePreferenceInt = cachePreference?.toInt() ?: 5 * 60
            refreshTime = cachePreferenceInt.times(1000 * 1000 * 1000L)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    /* this function will bypass the Cache logic and will always fetch data from endpoint. */
    fun refreshBypassCache() {
        fetchFromRemote()
    }

    /* This function will actually fetch dogs data using Retrofit and RxJava
    *  When Success-> Stores data into Room database
    * */
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

    /* This function will fetch dogs data from Room database.*/
    private fun fetchFromDatabase() {
        loading.value = true

        launch {
            val dogsList = DogDatabase(context).dogDao().getAllDogs()
            dogsRetrieved(dogsList)
            NotificationsHelper(context).createNotification(context.getString(R.string.database_notification_text))
        }
    }

    /* This function will update all LiveData objects with corresponding data.
    *  LisFragment is observing all LiveData objects. Once value is set here, Listfragment will get
    *  notification.
    * */
    private fun dogsRetrieved(dogsList: List<DogBreed>) {
        dogs.value = dogsList
        loading.value = false
        dogsLoadError.value = false
    }

    /* This function will store Dogs data to Room database*/
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

    /* This is ViewModel lifecycle method which is called when ViewModel is getting cleared.
    *  Here we are clearing RxJava Disposable object.
    * */
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}