package com.android.dogs.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.android.dogs.model.DogBreed
import com.android.dogs.room.DogDatabase
import kotlinx.coroutines.launch

class DogsDetailsViewModel(application: Application) : BaseViewModel(application) {

    val dogsDetails = MutableLiveData<DogBreed>()

    fun getDogDetails(uuid:Int){
      launch {
          val dog = DogDatabase(getApplication()).dogDao().getDog(uuid)
          dogsDetails.value = dog
      }
    }

}