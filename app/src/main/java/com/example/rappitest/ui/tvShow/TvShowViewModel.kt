package com.example.rappitest.ui.tvShow

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.rappitest.repository.TvShowRepository
import com.example.rappitest.util.AbsentLiveData
import com.example.rappitest.vo.Resource
import com.example.rappitest.vo.tv.TvShow
import com.example.rappitest.vo.tv.TvShowDetail
import javax.inject.Inject

/**
 * Created by Alexander Fermin (alexfer06@gmail.com) on 06/08/2018.
 */
class TvShowViewModel @Inject constructor(repository: TvShowRepository) : ViewModel() {

    private val _tvShowId: MutableLiveData<TvShowId> = MutableLiveData()

    val tvShow: LiveData<TvShow> = Transformations
            .switchMap(_tvShowId) { input ->
                input.ifExists { id ->
                    repository.loadMovie(id)
                }
            }

    val tvShowDetail: LiveData<Resource<TvShowDetail>> = Transformations
            .switchMap(_tvShowId) { input ->
                input.ifExists { id ->
                    repository.loadMovieDetail(id)
                }
            }


    fun retry() {
        val id = _tvShowId.value?.id
        if (id != null) {
            _tvShowId.value = TvShowId(id)
        }
    }

    fun setId(id: Int) {
        val update = TvShowId(id)
        if (_tvShowId.value == update) {
            return
        }
        _tvShowId.value = update
    }

    data class TvShowId(val id: Int) {
        fun <T> ifExists(f: (Int) -> LiveData<T>): LiveData<T> {
            return if (id == 0) {
                AbsentLiveData.create()
            } else {
                f(id)
            }
        }
    }
}
