package com.example.rappitest.repository

import com.example.rappitest.api.ApiSuccessResponse
import com.example.rappitest.api.MovieService
import com.example.rappitest.api.SearchResponse
import com.example.rappitest.db.SearchDB
import com.example.rappitest.vo.movie.Movie
import retrofit2.Response

/**
 * A task that reads the search result in the database and fetches the page page, if it has one.
 */
class MovieFetchNextSearchPageTask constructor(
        private val movieService: MovieService,
        private val category: String,
        private val db: SearchDB
) : FetchNextSearchPageTask<Movie>(category, db) {


    override fun insertResult(apiResponse: ApiSuccessResponse<SearchResponse<Movie>>) {
        db.movieDao().insertMovie(apiResponse.body.results)
    }

    override fun getRequest(): Response<SearchResponse<Movie>> =
            when (category) {
                MovieRepository.Category.MOVIE_TOP_RATED.name ->
                    movieService.getTopRated(nextPage).execute()
                MovieRepository.Category.MOVIE_UPCOMING.name ->
                    movieService.getUpcoming(nextPage).execute()
                else ->
                    movieService.getPopular(nextPage).execute()
            }

}
