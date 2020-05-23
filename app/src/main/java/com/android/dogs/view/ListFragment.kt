package com.android.dogs.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.dogs.R
import com.android.dogs.viewmodel.DogsViewModel
import kotlinx.android.synthetic.main.fragment_list.*


/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {

    private lateinit var dogsViewModel: DogsViewModel
    private val dogsListAdapter = DogsAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dogsViewModel = ViewModelProvider(this).get(DogsViewModel::class.java)
        dogsViewModel.refresh()

        dogsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dogsListAdapter
        }

        refreshLayout.setOnRefreshListener {
            dogsList.visibility = View.GONE
            listError.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            dogsViewModel.refreshBypassCache()
            refreshLayout.isRefreshing = false
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        dogsViewModel.dogs.observe(viewLifecycleOwner, Observer { dogs ->
            dogs?.let {
                dogsList.visibility = View.VISIBLE
                dogsListAdapter.updateDogsList(dogs)
            }
        })
        dogsViewModel.dogsLoadError.observe(viewLifecycleOwner, Observer { isError ->
            isError?.let {
                if(it)listError.visibility = View.VISIBLE else listError.visibility = View.GONE
            }
        })

        dogsViewModel.loading.observe(viewLifecycleOwner, Observer {isLoading->
            isLoading?.let {
                progressBar.visibility = if(it) View.VISIBLE else View.GONE
                if(it){
                    dogsList.visibility = View.GONE
                    listError.visibility = View.GONE
                }
            }
        })

    }
}
