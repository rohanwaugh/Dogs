package com.android.dogs.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.dogs.R
import com.android.dogs.databinding.FragmentDetailsBinding
import com.android.dogs.viewmodel.DogsDetailsViewModel


/**
 * A simple [Fragment] subclass.
 */
class DetailsFragment : Fragment() {

    private lateinit var dogsDetailsViewModel: DogsDetailsViewModel
    private var dogUuid = 0

    private lateinit var dataBinding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate<FragmentDetailsBinding>(
            LayoutInflater.from(context),
            R.layout.fragment_details,
            container,
            false
        )
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            dogUuid = DetailsFragmentArgs.fromBundle(it).dogUuid
        }

        dogsDetailsViewModel = ViewModelProvider(this).get(DogsDetailsViewModel::class.java)
        dogsDetailsViewModel.getDogDetails(dogUuid)

        observeViewModel()
    }

    private fun observeViewModel() {

        dogsDetailsViewModel.dogsDetails.observe(viewLifecycleOwner, Observer { dog ->

            dog?.let {
                dataBinding.dog = dog
            }
        })
    }

}
