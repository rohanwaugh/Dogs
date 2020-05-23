package com.android.dogs.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.dogs.R
import com.android.dogs.viewmodel.DogsDetailsViewModel
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.fragment_details.dogName
import kotlinx.android.synthetic.main.item_dog.*


/**
 * A simple [Fragment] subclass.
 */
class DetailsFragment : Fragment() {

    private lateinit var dogsDetailsViewModel: DogsDetailsViewModel
    private var dogUuid = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dogsDetailsViewModel = ViewModelProvider(this).get(DogsDetailsViewModel::class.java)
        dogsDetailsViewModel.getDogDetails()

        arguments?.let {
            dogUuid = DetailsFragmentArgs.fromBundle(it).dogUuid
        }

        observeViewModel()

    }

    private fun observeViewModel() {
        dogsDetailsViewModel.dogsDetails.observe(viewLifecycleOwner, Observer { dog ->

            dog?.let {
                dogName.text = dog.dogBreed
                dogPurpose.text = dog.breedFor
                dogTemparament.text = dog.temperament
                dogLifeSpan.text = dog.lifeSpan
            }
        })
    }

}
