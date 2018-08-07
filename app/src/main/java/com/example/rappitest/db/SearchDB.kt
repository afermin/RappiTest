package com.example.rappitest.db


import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.rappitest.vo.SearchResult
import com.example.rappitest.vo.movie.Movie
import com.example.rappitest.vo.movie.MovieDetail
import com.example.rappitest.vo.tv.TvShow
import com.example.rappitest.vo.tv.TvShowDetail

/**
 * Main database description.
 */
@Database(
    entities = [
        Movie::class,
        TvShow::class,
        MovieDetail::class,
        TvShowDetail::class,
        SearchResult::class],
    version = 3,
    exportSchema = false
)
abstract class SearchDB : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    abstract fun tvShowDao(): TvShowDao

    abstract fun searchResultDao(): SearchResultDao
}
