package com.example.rappitest.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.example.rappitest.AppExecutors
import com.example.rappitest.api.ApiResponse
import com.example.rappitest.api.ApiSuccessResponse
import com.example.rappitest.util.AbsentLiveData
import com.example.rappitest.vo.Resource
import com.example.rappitest.api.MovieService
import com.example.rappitest.api.SearchResponse
import com.example.rappitest.db.MovieDao
import com.example.rappitest.db.SearchDB
import com.example.rappitest.db.SearchResultDao
import com.example.rappitest.vo.SearchResult
import com.example.rappitest.vo.movie.Movie
import com.example.rappitest.vo.movie.MovieDetail
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles Movie instances.
 *
 * Movie - value object name
 * Repository - type of this class.
 */
@Singleton
class MovieRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val db: SearchDB,
        private val movieDao: MovieDao,
        private val searchResultDao: SearchResultDao,
        private val movieService: MovieService
) {

    enum class Category {
        MOVIE_TOP_RATED, MOVIE_UPCOMING, MOVIE_POPULAR
    }

    fun loadMovie(id: Int): LiveData<Movie> {
        return movieDao.load(id)
    }

    fun loadMovieDetail(id: Int): LiveData<Resource<MovieDetail>> {
        return object : NetworkBoundResource<MovieDetail, MovieDetail>(appExecutors) {
            override fun saveCallResult(item: MovieDetail) {
                movieDao.insertDetail(item)
            }

            override fun shouldFetch(data: MovieDetail?) = data == null

            override fun loadFromDb() = movieDao.loadDetail(id)

            override fun createCall() = movieService.get(id)
        }.asLiveData()
    }

    fun searchNextPage(category: Category): LiveData<Resource<Boolean>> {
        val fetchNextSearchPageTask = MovieFetchNextSearchPageTask(
                movieService = movieService,
                category = category,
                db = db
        )
        appExecutors.networkIO().execute(fetchNextSearchPageTask)
        return fetchNextSearchPageTask.liveData
    }

    fun search(category: Category): LiveData<Resource<List<Movie>>> {
        return object : NetworkBoundResource<List<Movie>, SearchResponse<Movie>>(appExecutors) {

            override fun saveCallResult(item: SearchResponse<Movie>) {
                val repoIds = item.results.map { it.id }
                val repoSearchResult = SearchResult(
                        category = category.name,
                        repoIds = repoIds,
                        totalCount = item.totalPages,
                        next = item.nextPage
                )
                db.beginTransaction()
                try {
                    movieDao.insertMovie(item.results)
                    searchResultDao.insert(repoSearchResult)
                    db.setTransactionSuccessful()
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<Movie>?) = data == null

            override fun loadFromDb(): LiveData<List<Movie>> {

                return Transformations.switchMap(searchResultDao.search(category.name)) { searchData ->
                    if (searchData == null) {
                        AbsentLiveData.create()
                    } else {
                        movieDao.loadOrdered(searchData.repoIds)
                    }
                }
            }

            override fun createCall(): LiveData<ApiResponse<SearchResponse<Movie>>> {
                return when (category) {
                    Category.MOVIE_TOP_RATED -> movieService.getTopRated()
                    Category.MOVIE_UPCOMING -> movieService.getUpcoming()
                    Category.MOVIE_POPULAR -> movieService.getPopular()
                }

            }

            override fun processResponse(response: ApiSuccessResponse<SearchResponse<Movie>>)
                    : SearchResponse<Movie> {
                val body = response.body
                body.nextPage = response.nextPage
                return body
            }
        }.asLiveData()
    }
}
