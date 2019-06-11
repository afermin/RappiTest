package com.example.rappitest.db

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.util.SparseIntArray
import com.example.rappitest.vo.movie.MovieDetail
import com.example.rappitest.vo.tv.TvShow
import com.example.rappitest.vo.tv.TvShowDetail
import java.util.*

/**
 * Interface for database access on TvShow related operations.
 */
@Dao
abstract class TvShowDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(vararg tvShow: TvShow)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertTvShow(tvShows: List<TvShow>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun createTvShowIfNotExists(tvShow: TvShow): Long

    @Query("SELECT * FROM tv_show WHERE id = :id")
    abstract fun load(id: Int): LiveData<TvShow>

    @Query("SELECT * FROM tv_show_detail WHERE id = :id")
    abstract fun loadDetail(id: Int): LiveData<TvShowDetail>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertDetail(vararg tvShowDetail: TvShowDetail)

    @Query(
            """
        SELECT * FROM tv_show
        WHERE id = :type
        ORDER BY firstAirDate DESC"""
    )
    abstract fun loadTvShows(type: String): LiveData<List<TvShow>>


    fun loadOrdered(tvShowIds: List<Int>): LiveData<List<TvShow>> {
        val order = SparseIntArray()
        tvShowIds.withIndex().forEach {
            order.put(it.value, it.index)
        }
        return Transformations.map(loadById(tvShowIds)) { tvShow ->
            Collections.sort(tvShow) { r1, r2 ->
                val pos1 = order.get(r1.id)
                val pos2 = order.get(r2.id)
                pos1 - pos2
            }
            tvShow
        }
    }

    @Query("SELECT * FROM tv_show WHERE id in (:ids)")
    protected abstract fun loadById(ids: List<Int>): LiveData<List<TvShow>>

}
