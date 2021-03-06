package com.android.dogs.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.dogs.R
import com.android.dogs.viewmodel.DogsViewModel
import kotlinx.android.synthetic.main.fragment_list.*


/**
 * This is ListFragment which will show Dogs in Recyclerview
 */
class ListFragment : Fragment() {

    private lateinit var dogsViewModel: DogsViewModel
    private val dogsListAdapter = DogsAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Enable Menu for this Fragment
        setHasOptionsMenu(true)
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

        // SwipeRefreshLayout on refresh click listener
        refreshLayout.setOnRefreshListener {
            dogsList.visibility = View.GONE
            listError.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            dogsViewModel.refreshBypassCache()
            refreshLayout.isRefreshing = false
        }

        observeViewModel()
    }

    /* This function observers ViewModel LiveData objects*/
    private fun observeViewModel() {
        dogsViewModel.dogs.observe(viewLifecycleOwner, Observer { dogs ->
            dogs?.let {
                dogsList.visibility = View.VISIBLE
                dogsListAdapter.updateDogsList(dogs)
            }
        })
        dogsViewModel.dogsLoadError.observe(viewLifecycleOwner, Observer { isError ->
            isError?.let {
                if (it) listError.visibility = View.VISIBLE else listError.visibility = View.GONE
            }
        })

        dogsViewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                progressBar.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    dogsList.visibility = View.GONE
                    listError.visibility = View.GONE
                }
            }
        })

    }

    /* This function will setup Menu for ListFragment. */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_menu, menu)
    }

    /* This function will handle MenuItem click. */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.actionSettings -> {
                view?.let {
                    Navigation.findNavController(it)
                        .navigate(ListFragmentDirections.actionSettingsFragment())
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
