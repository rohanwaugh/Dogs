package com.android.dogs.view

import android.view.View

/* This is RecyclerViewItem onClickListener used for DataBinding inside item_dog.xml*/
interface DogClickListener {

    fun onDogClicked(v: View)
}