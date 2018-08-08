package com.example.rappitest.repository

import com.example.rappitest.api.ApiSuccessResponse
import com.example.rappitest.api.SearchResponse
import com.example.rappitest.api.TvShowService
import com.example.rappitest.db.SearchDB
import com.example.rappitest.vo.tv.TvShow
import retrofit2.Response

/*
 * A task that reads the search result in the database and fetches the page page, if it has one.
 */
class TvShowFetchNextSearchPageTask constructor(
        private val tvShowService: TvShowService,
        private val category: String,
        private val db: SearchDB
) : FetchNextSearchPageTask<TvShow>(category, db) {


    override fun insertResult(apiResponse: ApiSuccessResponse<SearchResponse<TvShow>>) {
        db.tvShowDao().insertTvShow(apiResponse.body.results)
    }

    override fun getRequest(): Response<SearchResponse<TvShow>> =
            when (category) {
                MovieRepository.Category.MOVIE_TOP_RATED.name ->
                    tvShowService.getTopRated(nextPage).execute()
                else ->
                    tvShowService.getPopular(nextPage).execute()
            }

}
