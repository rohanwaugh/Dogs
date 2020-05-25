package com.android.dogs.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.palette.graphics.Palette
import com.android.dogs.R
import com.android.dogs.databinding.FragmentDetailsBinding
import com.android.dogs.model.DogPalette
import com.android.dogs.viewmodel.DogsDetailsViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition


/**
 * A simple [Fragment] subclass.
 */
class DetailsFragment : Fragment() {

    private lateinit var dogsDetailsViewModel: DogsDetailsViewModel
    private var dogUuid = 0
    private var sendSmsStarted = false

    private lateinit var dataBinding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
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

                it.imageUrl?.let { url ->
                    setupBackgroundColor(url)
                }
            }
        })
    }

    private fun setupBackgroundColor(url: String) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate { palette ->
                            val color = palette?.vibrantSwatch?.rgb ?: 0
                            val dogPalette = DogPalette(color)
                            dataBinding.palette = dogPalette
                        }
                }

            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.details_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionSendSms -> {
                sendSmsStarted = true
                (activity as MainActivity).checkSmsPermission()
            }

            R.id.actionShare -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onPermissionResult(permissionGranted: Boolean) {

    }

}
