package com.example.rappitest.ui.movie

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.rappitest.AppExecutors
import com.example.rappitest.binding.FragmentDataBindingComponent
import com.example.rappitest.di.Injectable
import com.example.rappitest.ui.common.RetryCallback
import com.example.rappitest.util.autoCleared
import com.example.rappitest.R
import com.example.rappitest.databinding.FragmentMovieBinding
import javax.inject.Inject

/**
 * The UI Controller for displaying a Movie's information.
 */
class MovieFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var movieViewModel: MovieViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    // mutable for testing
    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentMovieBinding>()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        movieViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MovieViewModel::class.java)
        val params = MovieFragmentArgs.fromBundle(arguments)
        movieViewModel.setId(params.id)

        val movie = movieViewModel.movie
        val movieDetail = movieViewModel.movieDetail
        movie.observe(this, Observer { resource ->
            binding.movie = resource
        })
        movieDetail.observe(this, Observer { resource ->
            binding.movieDetail = resource?.data
        })

        initMovieDetail(movieViewModel)
    }

    private fun initMovieDetail(viewModel: MovieViewModel) {
        /*viewModel.movie.observe(this, Observer { listResource ->
            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource?.data != null) {
                adapter.submitList(listResource.data)
            } else {
                adapter.submitList(emptyList())
            }
        })*/
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentMovieBinding>(
                inflater,
                R.layout.fragment_movie,
                container,
                false,
                dataBindingComponent
        )
        dataBinding.retryCallback = object : RetryCallback {
            override fun retry() {
                movieViewModel.retry()
            }
        }
        binding = dataBinding
        return dataBinding.root
    }

    /**
     * Created to be able to override in tests
     */
    fun navController() = findNavController()
}
