package com.example.rappitest.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.rappitest.vo.SearchResult

/**
 * Interface for database access on SearchResult related operations.
 */
@Dao
abstract class SearchResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(result: SearchResult)

    @Query("SELECT * FROM search_result WHERE `category` = :category")
    abstract fun search(category: String): LiveData<SearchResult>

    @Query("SELECT * FROM search_result WHERE `category` = :category")
    abstract fun findSearchResult(category: String): SearchResult?


}