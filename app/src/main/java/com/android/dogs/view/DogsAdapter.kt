package com.android.dogs.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.android.dogs.R
import com.android.dogs.databinding.ItemDogBinding
import com.android.dogs.model.DogBreed
import kotlinx.android.synthetic.main.item_dog.view.*

/* This is RecyclerView adapter class. */
class DogsAdapter(private val dogsList: ArrayList<DogBreed>) :
    RecyclerView.Adapter<DogsAdapter.DogsViewHolder>(), DogClickListener {

    /**/
    fun updateDogsList(newDogsList: List<DogBreed>) {
        dogsList.clear()
        dogsList.addAll(newDogsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =
            DataBindingUtil.inflate<ItemDogBinding>(inflater, R.layout.item_dog, parent, false)
        return DogsViewHolder(view)
    }

    override fun getItemCount() = dogsList.count()

    override fun onBindViewHolder(holder: DogsViewHolder, position: Int) {

        holder.view.dog = dogsList[position]
        holder.view.listener = this
    }

    override fun onDogClicked(v: View) {
        val action = ListFragmentDirections.actionDetailsFragment()
        action.dogUuid = v.dogId.text.toString().toInt()
        Navigation.findNavController(v).navigate(action)
    }

    /* This is Recyclerview Holder class. */
    inner class DogsViewHolder(var view: ItemDogBinding) : RecyclerView.ViewHolder(view.root)


}