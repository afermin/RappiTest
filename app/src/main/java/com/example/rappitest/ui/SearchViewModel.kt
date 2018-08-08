package com.example.rappitest.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.rappitest.vo.Resource


/**
 * Created by Alexander Fermin (alexfer06@gmail.com) on 06/08/2018.
 */

abstract class SearchViewModel<T> : ViewModel() {

    abstract fun request(): LiveData<Resource<List<T>>>

    var isMovie: Boolean = true

    var categoryIdItemPosition = MutableLiveData<Int>()


    init {

    }
}
