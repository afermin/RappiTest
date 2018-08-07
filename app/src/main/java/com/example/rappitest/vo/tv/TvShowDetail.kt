package com.example.rappitest.vo.tv

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.TypeConverters
import com.example.rappitest.db.SearchTypeConverters
import com.google.gson.annotations.SerializedName


@Entity(
        tableName = "tv_show_detail",
        indices = [
                (Index("id"))],
        primaryKeys = ["id"]
)
@TypeConverters(SearchTypeConverters::class)
data class TvShowDetail(
        @field:SerializedName("backdrop_path")
        val backdropPath: String,
        @field:SerializedName("created_by")
        val createdBy: List<CreatedBy>,
        @field:SerializedName("episode_run_time")
        val episodeRunTime: List<Int>,
        @field:SerializedName("first_air_date")
        val firstAirDate: String,
        @field:SerializedName("genres")
        val genres: List<Genre>,
        @field:SerializedName("homepage")
        val homepage: String,
        @field:SerializedName("id")
        val id: Int,
        @field:SerializedName("in_production")
        val inProduction: Boolean,
        @field:SerializedName("languages")
        val languages: List<String>,
        @field:SerializedName("last_air_date")
        val lastAirDate: String,
        @field:SerializedName("name")
        val name: String,
        @field:SerializedName("networks")
        val networks: List<Network>,
        @field:SerializedName("number_of_episodes")
        val numberOfEpisodes: Int,
        @field:SerializedName("number_of_seasons")
        val numberOfSeasons: Int,
        @field:SerializedName("origin_country")
        val originCountry: List<String>,
        @field:SerializedName("original_language")
        val originalLanguage: String,
        @field:SerializedName("original_name")
        val originalName: String,
        @field:SerializedName("overview")
        val overview: String,
        @field:SerializedName("popularity")
        val popularity: Double,
        @field:SerializedName("poster_path")
        val posterPath: String,
        @field:SerializedName("production_companies")
        val productionCompanies: List<ProductionCompany>,
        @field:SerializedName("seasons")
        val seasons: List<Season>,
        @field:SerializedName("status")
        val status: String,
        @field:SerializedName("type")
        val type: String,
        @field:SerializedName("vote_average")
        val voteAverage: Double,
        @field:SerializedName("vote_count")
        val voteCount: Int
) {

    data class Network(
            @field:SerializedName("name")
            val name: String,
            @field:SerializedName("id")
            val id: Int,
            @field:SerializedName("logo_path")
            val logoPath: String,
            @field:SerializedName("origin_country")
            val originCountry: String
    )


    data class Genre(
            @field:SerializedName("id")
            val id: Int,
            @field:SerializedName("name")
            val name: String
    )


    data class Season(
            @field:SerializedName("air_date")
            val airDate: String,
            @field:SerializedName("episode_count")
            val episodeCount: Int,
            @field:SerializedName("id")
            val id: Int,
            @field:SerializedName("name")
            val name: String,
            @field:SerializedName("overview")
            val overview: String,
            @field:SerializedName("poster_path")
            val posterPath: String,
            @field:SerializedName("season_number")
            val seasonNumber: Int
    )


    data class CreatedBy(
            @field:SerializedName("id")
            val id: Int,
            @field:SerializedName("credit_id")
            val creditId: String,
            @field:SerializedName("name")
            val name: String,
            @field:SerializedName("gender")
            val gender: Int,
            @field:SerializedName("profile_path")
            val profilePath: String
    )


    data class ProductionCompany(
            @field:SerializedName("id")
            val id: Int,
            @field:SerializedName("logo_path")
            val logoPath: Any?,
            @field:SerializedName("name")
            val name: String,
            @field:SerializedName("origin_country")
            val originCountry: String
    )
}