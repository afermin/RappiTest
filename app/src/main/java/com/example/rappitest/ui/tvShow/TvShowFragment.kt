package com.example.rappitest.ui.tvShow

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
import com.example.rappitest.R
import com.example.rappitest.binding.FragmentDataBindingComponent
import com.example.rappitest.databinding.FragmentTvShowBinding
import com.example.rappitest.di.Injectable
import com.example.rappitest.ui.common.RetryCallback
import com.example.rappitest.util.autoCleared
import javax.inject.Inject

/**
 * Created by Alexander Fermin (alexfer06@gmail.com) on 06/08/2018.
 */
class TvShowFragment  : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: TvShowViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    // mutable for testing
    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentTvShowBinding>()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(TvShowViewModel::class.java)
        val params = TvShowFragmentArgs.fromBundle(arguments)
        viewModel.setId(params.id)

        val tvShow = viewModel.tvShow
        val tvShowDetail = viewModel.tvShowDetail
        tvShow.observe(this, Observer { resource ->
            binding.tvShow = resource
        })
        tvShowDetail.observe(this, Observer { resource ->
            binding.tvShowDetail = resource?.data
        })

        initTvShowDetail(viewModel)
    }

    private fun initTvShowDetail(viewModel: TvShowViewModel) {
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
        val dataBinding = DataBindingUtil.inflate<FragmentTvShowBinding>(
                inflater,
                R.layout.fragment_tv_show,
                container,
                false,
                dataBindingComponent
        )
        dataBinding.retryCallback = object : RetryCallback {
            override fun retry() {
                viewModel.retry()
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
