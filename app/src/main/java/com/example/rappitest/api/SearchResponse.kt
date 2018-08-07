package com.example.rappitest.api

import com.google.gson.annotations.SerializedName

/**
 * Simple object to hold repo search responses. This is different from the Entity in the database
 * because we are keeping a search result in 1 row and denormalizing list of results into a single
 * column.
 */
data class SearchResponse<T>(
        @SerializedName("results")
        val results: List<T>,
        @SerializedName("page")
        val page: Int,
        @SerializedName("total_results")
        val totalResults: Int,
        @SerializedName("dates")
        val dates: Dates,
        @SerializedName("total_pages")
        val totalPages: Int
) {
    var nextPage: Int? = null

    data class Dates(
            @field:SerializedName("maximum")
            val maximum: String,
            @field:SerializedName("minimum")
            val minimum: String
    )
}
