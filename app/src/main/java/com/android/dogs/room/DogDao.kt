package com.android.dogs.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.android.dogs.model.DogBreed

/* This is DAO implementation which will provides methods which we can use to do database operations
*  e.g insert,delete,query
*  All above database operations has to be performed in background/ separate thread so we have use coroutines and hence all
*  below functions are suspend functions
* */
@Dao
interface DogDao {

    @Insert
    suspend fun insertAll(vararg dogs: DogBreed): List<Long>

    @Query("SELECT * FROM dogbreed")
    suspend fun getAllDogs(): List<DogBreed>

    @Query("SELECT * FROM dogbreed WHERE uuid = :dogId")
    suspend fun getDog(dogId: Int): DogBreed

    @Query("DELETE FROM dogbreed")
    suspend fun deleteAllDogs()
}