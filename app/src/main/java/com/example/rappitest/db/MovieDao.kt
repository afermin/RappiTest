package com.example.rappitest.db

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.util.SparseIntArray
import com.example.rappitest.vo.movie.Movie
import com.example.rappitest.vo.movie.MovieDetail
import java.util.Collections

/**
 * Interface for database access on Movie related operations.
 */
@Dao
abstract class MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg movie: Movie)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertMovie(movies: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun createMovieIfNotExists(movie: Movie): Long

    @Query("SELECT * FROM movie WHERE id = :id")
    abstract fun load(id: Int): LiveData<Movie>

    @Query("SELECT * FROM movie_detail WHERE id = :id")
    abstract fun loadDetail(id: Int): LiveData<MovieDetail>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertDetail(vararg movie: MovieDetail)

    @Query(
        """
        SELECT * FROM Movie
        WHERE id = :type
        ORDER BY releaseDate DESC"""
    )
    abstract fun loadMovies(type: String): LiveData<List<Movie>>


    fun loadOrdered(movieIds: List<Int>): LiveData<List<Movie>> {
        val order = SparseIntArray()
        movieIds.withIndex().forEach {
            order.put(it.value, it.index)
        }
        return Transformations.map(loadById(movieIds)) { movie ->
            Collections.sort(movie) { r1, r2 ->
                val pos1 = order.get(r1.id)
                val pos2 = order.get(r2.id)
                pos1 - pos2
            }
            movie
        }
    }

    @Query("SELECT * FROM Movie WHERE id in (:movieIds)")
    protected abstract fun loadById(movieIds: List<Int>): LiveData<List<Movie>>
}
