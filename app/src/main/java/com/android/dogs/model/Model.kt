package com.android.dogs.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/* This data class is used for storing Dog information. Also this is Entity(table)
*  for Room database
* */
@Entity
data class DogBreed(
    @ColumnInfo(name = "breed_id")
    @SerializedName("id")
    val breedId: String?,

    @ColumnInfo(name = "dog_name")
    @SerializedName("name")
    val dogBreed: String?,

    @ColumnInfo(name = "life_span")
    @SerializedName("life_span")
    val lifeSpan: String?,

    @ColumnInfo(name = "breed_group")
    @SerializedName("breed_group")
    val breedGroup: String?,

    @ColumnInfo(name = "bred_for")
    @SerializedName("bred_for")
    val breedFor: String?,

    @SerializedName("temperament")
    val temperament: String?,

    @ColumnInfo(name = "dog_url")
    @SerializedName("url")
    val imageUrl: String?
){
    // This is primary key for database table
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}

/* This data class is used for Palette functionality. */
data class DogPalette(val color:Int)

/* This data class is used for SMS functionality. */
data class SmsInfo(
    var to: String,
    var text: String,
    var imageUrl: String?
)