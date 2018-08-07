package com.example.rappitest.ui.movie

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.rappitest.repository.MovieRepository
import com.example.rappitest.util.AbsentLiveData
import com.example.rappitest.vo.Resource
import com.example.rappitest.vo.movie.Movie
import com.example.rappitest.vo.movie.MovieDetail
import javax.inject.Inject

class MovieViewModel @Inject constructor(repository: MovieRepository) : ViewModel() {

    private val _movieId: MutableLiveData<MovieId> = MutableLiveData()

    val movie: LiveData<Movie> = Transformations
            .switchMap(_movieId) { input ->
                input.ifExists { id ->
                    repository.loadMovie(id)
                }
            }

    val movieDetail: LiveData<Resource<MovieDetail>> = Transformations
            .switchMap(_movieId) { input ->
                input.ifExists { id ->
                    repository.loadMovieDetail(id)
                }
            }


    fun retry() {
        val id = _movieId.value?.id
        if (id != null) {
            _movieId.value = MovieId(id)
        }
    }

    fun setId(id: Int) {
        val update = MovieId(id)
        if (_movieId.value == update) {
            return
        }
        _movieId.value = update
    }

    data class MovieId(val id: Int) {
        fun <T> ifExists(f: (Int) -> LiveData<T>): LiveData<T> {
            return if (id == 0) {
                AbsentLiveData.create()
            } else {
                f(id)
            }
        }
    }
}
