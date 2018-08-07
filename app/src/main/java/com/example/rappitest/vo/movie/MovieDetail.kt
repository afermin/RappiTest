package com.example.rappitest.vo.movie

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.TypeConverters
import android.support.annotation.NonNull
import com.example.rappitest.db.SearchTypeConverters
import com.google.gson.annotations.SerializedName


@Entity(
        tableName = "movie_detail",
        indices = [
            (Index("id"))],
        primaryKeys = ["id"]
)
@TypeConverters(SearchTypeConverters::class)
data class MovieDetail(
        @field:SerializedName("adult")
        val adult: Boolean,
        @NonNull
        @field:SerializedName("backdrop_path")
        val backdropPath: String,
        @NonNull
        @field:SerializedName("belongs_to_collection")
        val belongsToCollection: BelongsToCollection,
        @field:SerializedName("budget")
        val budget: Int,
        @field:SerializedName("genres")
        val genres: List<Genre>,
        @NonNull
        @field:SerializedName("homepage")
        val homepage: String?,
        @field:SerializedName("id")
        val id: Int,
        @NonNull
        @field:SerializedName("imdb_id")
        val imdbId: String,
        @field:SerializedName("original_language")
        val originalLanguage: String,
        @field:SerializedName("original_title")
        val originalTitle: String,
        @NonNull
        @field:SerializedName("overview")
        val overview: String,
        @field:SerializedName("popularity")
        val popularity: Double,
        @NonNull
        @field:SerializedName("poster_path")
        val posterPath: String,
        @field:SerializedName("production_companies")
        val productionCompanies: List<ProductionCompany>,
        @field:SerializedName("production_countries")
        val productionCountries: List<ProductionCountry>,
        @field:SerializedName("release_date")
        val releaseDate: String,
        @field:SerializedName("revenue")
        val revenue: Int,
        @NonNull
        @field:SerializedName("runtime")
        val runtime: Int,
        @field:SerializedName("spoken_languages")
        val spokenLanguages: List<SpokenLanguage>,
        @field:SerializedName("status")
        val status: String,
        @NonNull
        @field:SerializedName("tagline")
        val tagline: String,
        @field:SerializedName("title")
        val title: String,
        @field:SerializedName("video")
        val video: Boolean,
        @field:SerializedName("vote_average")
        val voteAverage: Double,
        @field:SerializedName("vote_count")
        val voteCount: Int
) {

    @Entity
    @TypeConverters(SearchTypeConverters::class)
    data class SpokenLanguage(
            @field:SerializedName("iso_639_1")
            val iso6391: String,
            @field:SerializedName("name")
            val name: String
    )

    @Entity
    @TypeConverters(SearchTypeConverters::class)
    data class ProductionCompany(
            @field:SerializedName("id")
            val id: Int,
            @NonNull
            @field:SerializedName("logo_path")
            val logoPath: String,
            @field:SerializedName("name")
            val name: String,
            @field:SerializedName("origin_country")
            val originCountry: String
    )

    @Entity
    @TypeConverters(SearchTypeConverters::class)
    data class BelongsToCollection(
            @field:SerializedName("id")
            val id: Int,
            @field:SerializedName("name")
            val name: String,
            @field:SerializedName("poster_path")
            val posterPath: String,
            @field:SerializedName("backdrop_path")
            val backdropPath: String
    )

    @Entity
    @TypeConverters(SearchTypeConverters::class)
    data class Genre(
            @field:SerializedName("id")
            val id: Int,
            @field:SerializedName("name")
            val name: String
    )

    @Entity
    @TypeConverters(SearchTypeConverters::class)
    data class ProductionCountry(
            @field:SerializedName("iso_3166_1")
            val iso31661: String,
            @field:SerializedName("name")
            val name: String
    )
}