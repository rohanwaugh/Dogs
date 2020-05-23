package com.android.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.dogs.model.DogBreed

class DogsViewModel : ViewModel() {

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        val dog1 = DogBreed(
            "Breed1",
            "Dog1 breed",
            "10 years",
            "breedGroup",
            "breedFor",
            "temperament ",
            ""
        )
        val dog2 = DogBreed(
            "Breed2",
            "Dog2 breed",
            "15 years",
            "breedGroup2",
            "breedFor2",
            "temperament 2",
            ""
        )
        val dog3 = DogBreed(
            "Breed3",
            "Dog3 breed",
            "20 years",
            "breedGroup",
            "breedFor3",
            "temperament 3",
            ""
        )

        val dogList = arrayListOf(dog1,dog2,dog3)
        dogs.value = dogList
        dogsLoadError.value = false
        loading.value = false
    }
}