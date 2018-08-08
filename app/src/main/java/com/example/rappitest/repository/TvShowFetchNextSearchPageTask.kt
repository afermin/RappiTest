package com.example.rappitest.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.rappitest.api.ApiEmptyResponse
import com.example.rappitest.api.ApiErrorResponse
import com.example.rappitest.api.ApiResponse
import com.example.rappitest.api.ApiSuccessResponse
import com.example.rappitest.vo.Resource
import com.example.rappitest.api.TvShowService
import com.example.rappitest.db.SearchDB
import com.example.rappitest.vo.SearchResult
import java.io.IOException

/*
 * A task that reads the search result in the database and fetches the page page, if it has one.
 */
class TvShowFetchNextSearchPageTask constructor(
        private val tvShowService: TvShowService,
        private val category: TvShowRepository.Category,
        private val db: SearchDB
) : Runnable {
    private val _liveData = MutableLiveData<Resource<Boolean>>()
    val liveData: LiveData<Resource<Boolean>> = _liveData

    override fun run() {
        Log.d("TvShowFetchNext", "run()")
        val current = db.searchResultDao().findSearchResult(category.name)
        if (current == null) {
            _liveData.postValue(null)
            return
        }
        val page = current.page
        if (page == current.totalCount) {
            Log.d("TvShowFetchNext", "nextPage == null")
            _liveData.postValue(Resource.success(false))
            return
        }
        val newValue = try {

            val nextPage = page+1
            val response = when(category){
                TvShowRepository.Category.TV_SHOW_TOP_RATED ->
                    tvShowService.getTopRated(nextPage).execute()
                TvShowRepository.Category.TV_SHOW_POPULAR ->
                    tvShowService.getPopular(nextPage).execute()
            }

            val apiResponse = ApiResponse.create(response)
            Log.d("TvShowFetchNext", "apiResponse")
            when (apiResponse) {
                is ApiSuccessResponse -> {
                    Log.d("TvShowFetchNext", "ApiSuccessResponse")
                    // we merge all ids into 1 list so that it is easier to fetch the
                    // result list.
                    val ids = arrayListOf<Int>()
                    ids.addAll(current.repoIds)

                    ids.addAll(apiResponse.body.results.map { it.id })
                    val merged = SearchResult(
                            category.name, ids,
                            apiResponse.body.totalPages, apiResponse.body.page
                    )
                    Log.d("TvShowFetchNext", "try")
                    try {
                        db.beginTransaction()
                        db.searchResultDao().insert(merged)
                        db.tvShowDao().insertTvShow(apiResponse.body.results)
                        db.setTransactionSuccessful()
                    } finally {
                        db.endTransaction()
                    }
                    Log.d("TvShowFetchNext", "Resource.success")
                    Resource.success(apiResponse.body.page != apiResponse.body.totalPages)
                }
                is ApiEmptyResponse -> {
                    Log.d("TvShowFetchNext", "ApiEmptyResponse")
                    Resource.success(false)
                }
                is ApiErrorResponse -> {
                    Resource.error(apiResponse.errorMessage, true)
                }
            }

        } catch (e: IOException) {
            Resource.error(e.message!!, true)
        }
        Log.d("TvShowFetchNext", "_liveData.postValue(newValue)")
        _liveData.postValue(newValue)
    }
}
