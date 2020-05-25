package com.android.dogs.utils

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.android.dogs.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/* This is Utility function which provides CircularProgressDrawable to show small
*  progressBar while loading image in Recyclerview
* */
fun getProgressDrawable(context: Context): CircularProgressDrawable{
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

/* This is Extension function of ImageView class which is used to load image into ImageView
*  using Glide library
* */
fun ImageView.loadImage(uri: String?, circularProgressDrawable: CircularProgressDrawable){
    val options = RequestOptions()
        .placeholder(circularProgressDrawable)
        .error(R.mipmap.ic_dog)
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this)

}

/* This is BindingAdapter method which is used to loadImage to imageView in layout xml file using DataBinding
*  Note - "android:imageUrl" this is used in item_dog.xml and fragment_details.xml for ImageView
* */
@BindingAdapter("android:imageUrl")
fun loadImage(view:ImageView, uri:String?){
    view.loadImage(uri, getProgressDrawable(view.context))
}