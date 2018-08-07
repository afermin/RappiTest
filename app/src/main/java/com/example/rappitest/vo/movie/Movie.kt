package com.example.rappitest.vo.movie

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.TypeConverters
import android.support.annotation.NonNull
import com.example.rappitest.db.SearchTypeConverters
import com.example.rappitest.vo.ProductionBaseData
import com.google.gson.annotations.SerializedName

@Entity(
        indices = [
            (Index("id"))],
        primaryKeys = ["id"]
)
@TypeConverters(SearchTypeConverters::class)
data class Movie(
        @field:SerializedName("vote_count")
        val vote_count: Int,
        @field:SerializedName("video")
        val video: Boolean,
        @field:SerializedName("original_title")
        val originalTitle: String,
        @field:SerializedName("adult")
        val adult: Boolean,
        @field:SerializedName("release_date")
        val releaseDate: String

        /*@field:SerializedName("id")
        val id: Int,
        @field:SerializedName("vote_count")
        val vote_count: Int,
        @field:SerializedName("video")
        val video: Boolean,
        @field:SerializedName("vote_average")
        val voteAverage: Double,
        @field:SerializedName("title")
        val title: String,
        @field:SerializedName("popularity")
        val popularity: Double,
        @NonNull
        @field:SerializedName("poster_path")
        val posterPath: String,
        @NonNull
        @field:SerializedName("backdrop_path")
        val backdropPath: String,
        @field:SerializedName("original_language")
        val originalLanguage: String,
        @field:SerializedName("original_title")
        val originalTitle: String,
        @NonNull
        @field:SerializedName("genre_ids")
        val genreIds: List<Int>,
        @field:SerializedName("adult")
        val adult: Boolean,
        @field:SerializedName("overview")
        val overview: String,
        @field:SerializedName("release_date")
        val releaseDate: String*/
): ProductionBaseData()


