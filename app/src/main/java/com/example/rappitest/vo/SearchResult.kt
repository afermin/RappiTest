package com.example.rappitest.vo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.TypeConverters
import com.example.rappitest.db.SearchTypeConverters

/**
 * Created by Alexander Fermin (alexfer06@gmail.com) on 05/08/2018.
 */
@Entity(
        tableName = "search_result",
        primaryKeys = ["category"]
)
@TypeConverters(SearchTypeConverters::class)
data class SearchResult(
        val category: String,
        val repoIds: List<Int>,
        val totalCount: Int,
        val page: Int
)