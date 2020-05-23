package com.android.dogs.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.android.dogs.R
import com.android.dogs.model.DogBreed
import kotlinx.android.synthetic.main.item_dog.view.*

class DogsAdapter(private val dogsList:ArrayList<DogBreed>): RecyclerView.Adapter<DogsAdapter.DogsViewHolder>() {

    fun updateDogsList(newDogsList: List<DogBreed>){
        dogsList.clear()
        dogsList.addAll(newDogsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dog,parent,false)
        return DogsViewHolder(view)
    }

    override fun getItemCount() = dogsList.count()

    override fun onBindViewHolder(holder: DogsViewHolder, position: Int) {
        holder.bindDog(dogsList[position])
        holder.itemView.setOnClickListener {
            Navigation.findNavController(it).navigate(ListFragmentDirections.actionDetailsFragment())
        }
    }

    inner class DogsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindDog(dogBreed: DogBreed) {
            itemView.dogName.text = dogBreed.dogBreed
            itemView.lifeSpan.text = dogBreed.lifeSpan
        }

    }
}