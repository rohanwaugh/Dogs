package com.android.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.dogs.model.DogBreed

class DogsDetailsViewModel : ViewModel() {

    val dogsDetails = MutableLiveData<DogBreed>()

    fun getDogDetails(){
        val dog = DogBreed(
            "Breed1",
            "Dog1 breed",
            "10 years",
            "breedGroup",
            "breedFor",
            "temperament ",
            ""
        )
        dogsDetails.value = dog
    }

}