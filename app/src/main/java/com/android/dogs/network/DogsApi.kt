package com.android.dogs.network

import com.android.dogs.model.DogBreed
import io.reactivex.Single
import retrofit2.http.GET

/* This is Retrofit interface*/
interface DogsApi {
    @GET("DevTides/DogsApi/master/dogs.json")
    fun getDogs(): Single<List<DogBreed>>
}