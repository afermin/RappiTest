package com.example.rappitest.vo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.TypeConverters
import com.example.rappitest.db.SearchTypeConverters

@Entity(primaryKeys = ["query"])
@TypeConverters(SearchTypeConverters::class)
data class RepoSearchResult(
    val query: String,
    val repoIds: List<Int>,
    val totalCount: Int,
    val next: Int?
)
