package com.example.rappitest.repository


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.example.rappitest.AppExecutors
import com.example.rappitest.api.ApiResponse
import com.example.rappitest.api.ApiSuccessResponse
import com.example.rappitest.api.SearchResponse
import com.example.rappitest.api.TvShowService
import com.example.rappitest.db.SearchDB
import com.example.rappitest.db.SearchResultDao
import com.example.rappitest.db.TvShowDao
import com.example.rappitest.util.AbsentLiveData
import com.example.rappitest.vo.Resource
import com.example.rappitest.vo.SearchResult
import com.example.rappitest.vo.tv.TvShow
import com.example.rappitest.vo.tv.TvShowDetail
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles Movie instances.
 *
 * Movie - value object name
 * Repository - type of this class.
 */
@Singleton
class TvShowRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val db: SearchDB,
        private val tvShowDao: TvShowDao,
        private val searchResultDao: SearchResultDao,
        private val tvShowService: TvShowService
) : PaginateRepository() {

    enum class Category {
        TV_SHOW_TOP_RATED, TV_SHOW_POPULAR
    }

    fun loadMovie(id: Int): LiveData<TvShow> {
        return tvShowDao.load(id)
    }

    fun loadMovieDetail(id: Int): LiveData<Resource<TvShowDetail>> {
        return object : NetworkBoundResource<TvShowDetail, TvShowDetail>(appExecutors) {
            override fun saveCallResult(item: TvShowDetail): TvShowDetail {
                tvShowDao.insertDetail(item)
                return item
            }

            override fun loadFromDb() = tvShowDao.loadDetail(id)

            override fun createCall() = tvShowService.get(id)
        }.asLiveData()
    }

    override fun searchNextPage(category: String): LiveData<Resource<Boolean>> {
        val fetchNextSearchPageTask = TvShowFetchNextSearchPageTask(
                tvShowService = tvShowService,
                category = category,
                db = db
        )
        appExecutors.networkIO().execute(fetchNextSearchPageTask)
        return fetchNextSearchPageTask.liveData
    }

    fun search(category: Category): LiveData<Resource<List<TvShow>>> {
        return object : NetworkBoundResource<List<TvShow>, SearchResponse<TvShow>>(appExecutors) {

            override fun saveCallResult(item: SearchResponse<TvShow>): List<TvShow> {
                val repoIds = item.results.map { it.id }
                val repoSearchResult = SearchResult(
                        category = category.name,
                        repoIds = repoIds,
                        totalCount = item.totalPages,
                        page = item.page
                )
                db.beginTransaction()
                try {
                    tvShowDao.insertTvShow(item.results)
                    searchResultDao.insert(repoSearchResult)
                    db.setTransactionSuccessful()
                } finally {
                    db.endTransaction()
                }
                return item.results
            }

            override fun loadFromDb(): LiveData<List<TvShow>> {

                return Transformations.switchMap(searchResultDao.search(category.name)) { searchData ->
                    if (searchData == null) {
                        AbsentLiveData.create()
                    } else {
                        tvShowDao.loadOrdered(searchData.repoIds)
                    }
                }
            }

            override fun createCall(): LiveData<ApiResponse<SearchResponse<TvShow>>> {
                return when (category) {
                    Category.TV_SHOW_TOP_RATED -> tvShowService.getTopRated()
                    Category.TV_SHOW_POPULAR -> tvShowService.getPopular()
                }
            }

            override fun processResponse(response: ApiSuccessResponse<SearchResponse<TvShow>>)
                    : SearchResponse<TvShow> {
                val body = response.body
                body.page = response.body.page
                return body
            }
        }.asLiveData()
    }
}