package com.example.rappitest.vo.tv

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.TypeConverters
import com.example.rappitest.db.SearchTypeConverters
import com.example.rappitest.vo.ProductionBaseData
import com.google.gson.annotations.SerializedName

@Entity(
        tableName = "tv_show",
        indices = [
                (Index("id"))],
        primaryKeys = ["id"]
)

@TypeConverters(SearchTypeConverters::class)
data class TvShow(
        @field:SerializedName("original_name")
        val originalName: String,
        @field:SerializedName("origin_country")
        val originCountry: List<String>,
        @field:SerializedName("vote_count")
        val voteCount: Int,
        @field:SerializedName("first_air_date")
        val firstAirDate: String
): ProductionBaseData()