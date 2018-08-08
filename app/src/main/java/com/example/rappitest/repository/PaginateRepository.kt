package com.example.rappitest.repository

import android.arch.lifecycle.LiveData
import com.example.rappitest.vo.Resource

/**
 * Created by Alexander Fermin (alexfer06@gmail.com) on 08/08/2018.
 */
abstract class PaginateRepository {

    abstract fun searchNextPage(category: String): LiveData<Resource<Boolean>>

}