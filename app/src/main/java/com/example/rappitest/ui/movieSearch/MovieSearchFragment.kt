package com.example.rappitest.ui.movieSearch

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.IBinder
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.example.rappitest.AppExecutors
import com.example.rappitest.R
import com.example.rappitest.binding.FragmentDataBindingComponent
import com.example.rappitest.databinding.FragmentSearchBinding
import com.example.rappitest.di.Injectable
import com.example.rappitest.ui.common.SearchListAdapter
import com.example.rappitest.ui.common.RetryCallback
import com.example.rappitest.util.autoCleared
import javax.inject.Inject
import android.support.v7.widget.GridLayoutManager
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*


//@OpenForTesting
class MovieSearchFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentSearchBinding>()

    var adapter by autoCleared<SearchListAdapter>()

    lateinit var viewmodel: MovieSearchViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_search,
                container,
                false,
                dataBindingComponent
        )

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewmodel = ViewModelProviders.of(this, viewModelFactory)
                .get(MovieSearchViewModel::class.java)
        binding.viewmodel = viewmodel
        initRecyclerView()

        val rvAdapter = SearchListAdapter(
                dataBindingComponent = dataBindingComponent,
                appExecutors = appExecutors,
                showFullName = true
        ) { movie ->
            navController().navigate(
                    MovieSearchFragmentDirections.showMovie(movie.id)
            )
        }
        binding.list.adapter = rvAdapter
        binding.list.layoutManager = GridLayoutManager(context, 2)
        adapter = rvAdapter

        initSearchInputListener()

        binding.callback = object : RetryCallback {
            override fun retry() {
                viewmodel.refresh()
            }
        }

    }

    private fun initSearchInputListener() {/*
        binding.input.setOnEditorActionListener { view: View, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(view)
                true
            } else {
                false
            }
        }
        binding.input.setOnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                doSearch(view)
                true
            } else {
                false
            }
        }*/
    }


    private fun doSearch(v: View) {
        //val query = binding.input.text.toString()
        // Dismiss keyboard
        dismissKeyboard(v.windowToken)
    }

    private fun initRecyclerView() {

        binding.list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (lastPosition == adapter.itemCount - 1) {
                    viewmodel.loadNextPage()
                }
            }
        })
        viewmodel.results.observe(this, Observer { result ->
            binding.searchResource = result
            binding.resultCount = result?.data?.size ?: 0
            adapter.submitList(result?.data)
        })

        viewmodel.loadMoreStatus.observe(this, Observer { loadingMore ->
            if (loadingMore == null) {
                binding.loadingMore = false
            } else {
                binding.loadingMore = loadingMore.isRunning
                val error = loadingMore.errorMessageIfNotHandled
                if (error != null) {
                    Snackbar.make(binding.loadMoreBar, error, Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }

    /**
     * Created to be able to override in tests
     */
    fun navController() = findNavController()
}
