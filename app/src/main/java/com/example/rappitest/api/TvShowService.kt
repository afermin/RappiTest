package com.example.rappitest.api

import android.arch.lifecycle.LiveData
import com.example.rappitest.vo.tv.TvShow
import com.example.rappitest.vo.tv.TvShowDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Alexander Fermin (alexfer06@gmail.com) on 05/08/2018.
 */
interface TvShowService {

    @GET("top_rated")
    fun getTopRated(): LiveData<ApiResponse<SearchResponse<TvShow>>>

    @GET("top_rated")
    fun getTopRated(@Query("page") page: Int): Call<SearchResponse<TvShow>>

    @GET("popular")
    fun getPopular(): LiveData<ApiResponse<SearchResponse<TvShow>>>

    @GET("popular")
    fun getPopular(@Query("page") page: Int): Call<SearchResponse<TvShow>>

    @GET("{id}")
    fun get(@Path("id") id: Int): LiveData<ApiResponse<TvShowDetail>>


}