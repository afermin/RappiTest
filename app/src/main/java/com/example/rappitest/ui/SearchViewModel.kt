package com.example.rappitest.ui

import android.arch.lifecycle.*


/**
 * Created by Alexander Fermin (alexfer06@gmail.com) on 06/08/2018.
 */

abstract class SearchViewModel : ViewModel() {

    abstract fun query()

    var isMovie: Boolean = true

    var categoryIdItemPosition = MutableLiveData<Int>()


    init {

    }
}
