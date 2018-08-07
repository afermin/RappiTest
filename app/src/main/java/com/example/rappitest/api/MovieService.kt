package com.example.rappitest.api

import android.arch.lifecycle.LiveData
import com.example.rappitest.vo.movie.Movie
import com.example.rappitest.vo.movie.MovieDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * REST API access points
 */
interface MovieService {

    @GET("upcoming")
    fun getUpcoming(): LiveData<ApiResponse<SearchResponse<Movie>>>

    @GET("upcoming")
    fun getUpcoming(@Query("page") page: Int): Call<SearchResponse<Movie>>

    @GET("top_rated")
    fun getTopRated(): LiveData<ApiResponse<SearchResponse<Movie>>>

    @GET("top_rated")
    fun getTopRated(@Query("page") page: Int): Call<SearchResponse<Movie>>

    @GET("popular")
    fun getPopular(): LiveData<ApiResponse<SearchResponse<Movie>>>

    @GET("popular")
    fun getPopular(@Query("page") page: Int): Call<SearchResponse<Movie>>

    @GET("{id}")
    fun get(@Path("id") id: Int): LiveData<ApiResponse<MovieDetail>>

}
