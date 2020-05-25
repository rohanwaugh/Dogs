package com.android.dogs.network

import com.android.dogs.model.DogBreed
import com.android.dogs.utils.BASE_URL
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/* This is Retrofit service implementation class. */
class DogsService {

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())        // This will convert backend JSON data into data into our DogBreed data class compatible data
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // This will convert the data class into Observable(in this case Single) objects similar to LiveData
        .build()
        .create(DogsApi::class.java)

    fun getDogs():Single<List<DogBreed>>{
        return api.getDogs()
    }
}