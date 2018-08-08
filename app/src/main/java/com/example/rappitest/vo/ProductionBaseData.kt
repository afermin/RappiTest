package com.example.rappitest.vo

import com.google.gson.annotations.SerializedName

/**
 * Created by Alexander Fermin (alexfer06@gmail.com) on 06/08/2018.
 */
abstract class ProductionBaseData {

    @field:SerializedName("id")
    var id: Int = 0
    @field:SerializedName("name", alternate = ["title"])
    var name: String = ""
    @field:SerializedName("genre_ids")
    var genreIds: List<Int>? = null
    @field:SerializedName("backdrop_path")
    var backdropPath: String? = null
    @field:SerializedName("popularity")
    var popularity: Double = 0.0
    @field:SerializedName("original_language")
    var originalLanguage: String = ""
    @field:SerializedName("vote_average")
    var voteAverage: Double = 0.0
    @field:SerializedName("overview")
    var overview: String = ""
    @field:SerializedName("poster_path")
    var posterPath: String = ""

}