package com.example.rappitest.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.rappitest.api.ApiEmptyResponse
import com.example.rappitest.api.ApiErrorResponse
import com.example.rappitest.api.ApiResponse
import com.example.rappitest.api.ApiSuccessResponse
import com.example.rappitest.vo.Resource
import com.example.rappitest.api.MovieService
import com.example.rappitest.db.SearchDB
import com.example.rappitest.vo.SearchResult
import java.io.IOException

/**
 * A task that reads the search result in the database and fetches the page page, if it has one.
 */
class MovieFetchNextSearchPageTask constructor(
        private val movieService: MovieService,
        private val category: String,
        private val db: SearchDB
) : Runnable {
    private val _liveData = MutableLiveData<Resource<Boolean>>()
    val liveData: LiveData<Resource<Boolean>> = _liveData

    override fun run() {
        val current = db.searchResultDao().findSearchResult(category)
        if (current == null) {
            _liveData.postValue(null)
            return
        }
        val page = current.page
        if (page == current.totalCount) {
            _liveData.postValue(Resource.success(false))
            return
        }
        val newValue = try {

            val nextPage = page + 1
            val response = when (category) {
                MovieRepository.Category.MOVIE_TOP_RATED.name ->
                    movieService.getTopRated(nextPage).execute()
                MovieRepository.Category.MOVIE_UPCOMING.name ->
                    movieService.getUpcoming(nextPage).execute()
                else ->
                    movieService.getPopular(nextPage).execute()
            }

            val apiResponse = ApiResponse.create(response)
            when (apiResponse) {
                is ApiSuccessResponse -> {
                    // we merge all ids into 1 list so that it is easier to fetch the
                    // result list.
                    val ids = arrayListOf<Int>()
                    ids.addAll(current.repoIds)

                    ids.addAll(apiResponse.body.results.map { it.id })
                    val merged = SearchResult(
                            category, ids,
                            apiResponse.body.totalPages, apiResponse.body.page
                    )
                    try {
                        db.beginTransaction()
                        db.searchResultDao().insert(merged)
                        db.movieDao().insertMovie(apiResponse.body.results)
                        db.setTransactionSuccessful()
                    } finally {
                        db.endTransaction()
                    }
                    Resource.success(apiResponse.body.page != apiResponse.body.totalPages)
                }
                is ApiEmptyResponse -> {
                    Resource.success(false)
                }
                is ApiErrorResponse -> {
                    Resource.error(apiResponse.errorMessage, true)
                }
            }

        } catch (e: IOException) {
            Resource.error(e.message!!, true)
        }
        _liveData.postValue(newValue)
    }
}
