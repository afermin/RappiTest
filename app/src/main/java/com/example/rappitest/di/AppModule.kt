package com.example.rappitest.di

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.example.rappitest.api.MovieService
import com.example.rappitest.api.TvShowService
import com.example.rappitest.db.MovieDao
import com.example.rappitest.db.SearchDB
import com.example.rappitest.db.SearchResultDao
import com.example.rappitest.db.TvShowDao
import com.example.rappitest.util.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton


@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideMovieService(okHttpClient: OkHttpClient): MovieService {
        return Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/movie/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
                .create(MovieService::class.java)
    }

    @Singleton
    @Provides
    fun provideTvShowService(okHttpClient: OkHttpClient): TvShowService {
        return Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/tv/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
                .create(TvShowService::class.java)
    }

    @Singleton
    @Provides
    fun provideInterceptor(httpLoggingInterceptor: HttpLoggingInterceptor, cache: Cache): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()
            val originalHttpUrl = original.url()

            val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", "e1d1664f3b9b94a69f1ae5f1dfeff48a")
                    .build()

            // Request customization: add request headers
            val requestBuilder = original.newBuilder()
                    .url(url)

            val request = requestBuilder.build()
             chain.proceed(request)
        }.cache(cache).addInterceptor(httpLoggingInterceptor).build()
    }

    @Singleton
    @Provides
    fun provideLoginInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        return httpLoggingInterceptor
    }

    @Singleton
    @Provides
    fun provideCache(file: File): Cache {
        val cacheSize : Long = 10 * 1024 * 1024 // 10 MB
        return Cache(file, cacheSize)
    }


    @Singleton
    @Provides
    fun provideCacheFile(app: Application): File =
            File(app.cacheDir, "okhttp-cache")


    @Singleton
    @Provides
    fun provideDb(app: Application): SearchDB {
        return Room
                .databaseBuilder(app.applicationContext, SearchDB::class.java, "search.db")
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(db: SearchDB): MovieDao {
        return db.movieDao()
    }

    @Singleton
    @Provides
    fun provideTvShowDao(db: SearchDB): TvShowDao {
        return db.tvShowDao()
    }

    @Singleton
    @Provides
    fun provideSearchResultDao(db: SearchDB): SearchResultDao {
        return db.searchResultDao()
    }
}
