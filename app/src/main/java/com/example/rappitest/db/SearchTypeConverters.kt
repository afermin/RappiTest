package com.example.rappitest.db

import android.arch.persistence.room.TypeConverter
import com.example.rappitest.vo.movie.MovieDetail
import com.example.rappitest.vo.tv.TvShowDetail
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import timber.log.Timber


object SearchTypeConverters {
    @TypeConverter
    @JvmStatic
    fun stringToIntList(data: String?): List<Int>? {
        return data?.let {
            it.split(",").map {
                try {
                    it.toInt()
                } catch (ex: Throwable) {
                    Timber.e(ex, "Cannot convert $it to number")
                    null
                }
            }
        }?.filterNotNull()
    }

    @TypeConverter
    @JvmStatic
    fun intListToString(ints: List<Int>?): String? {
        return ints?.joinToString(",")
    }

    @TypeConverter
    @JvmStatic
    fun toStringList(string: String?): List<String>? {
        if (string == null) {
            return null
        }
        val type = object : TypeToken<List<String>>() {

        }.type
        return Gson().fromJson<List<String>>(string, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromStringList(list: List<String>?): String? {
        if (list == null) {
            return null
        }
        val type = object : TypeToken<List<String>>() {

        }.type
        return Gson().toJson(list, type)
    }

    /**
     * MovieDetail
     * */
    @TypeConverter
    @JvmStatic
    fun toString(o: MovieDetail.BelongsToCollection?): String? {
        if (o == null) {
            return null
        }
        val type = object : TypeToken<MovieDetail.BelongsToCollection>() {}.type
        return Gson().toJson(o, type)
    }

    @TypeConverter
    @JvmStatic
    fun toBelongsToCollection(string: String?): MovieDetail.BelongsToCollection? {
        if (string == null) {
            return null
        }
        val type = object : TypeToken<MovieDetail.BelongsToCollection>() {}.type
        return Gson().fromJson(string, type)
    }

    @TypeConverter
    @JvmStatic
    fun productionCompanyToString(o: List<MovieDetail.ProductionCompany>?): String? {
        if (o == null) {
            return null
        }
        val type = object : TypeToken<List<MovieDetail.ProductionCompany>>() {}.type
        return Gson().toJson(o, type)
    }

    @TypeConverter
    @JvmStatic
    fun toProductionCompany(string: String?): List<MovieDetail.ProductionCompany>? {
        if (string == null) {
            return null
        }
        val type = object : TypeToken<List<MovieDetail.ProductionCompany>>() {}.type
        return Gson().fromJson(string, type)
    }

    @TypeConverter
    @JvmStatic
    fun productionCountriesToString(o: List<MovieDetail.ProductionCountry>?): String? {
        if (o == null) {
            return null
        }
        val type = object : TypeToken<List<MovieDetail.ProductionCountry>>() {}.type
        return Gson().toJson(o, type)
    }

    @TypeConverter
    @JvmStatic
    fun toProductionCountry(string: String?): List<MovieDetail.ProductionCountry>? {
        if (string == null) {
            return null
        }
        val type = object : TypeToken<List<MovieDetail.ProductionCountry>>() {}.type
        return Gson().fromJson(string, type)
    }

    @TypeConverter
    @JvmStatic
    fun genresToString(o: List<MovieDetail.Genre>?): String? {
        if (o == null) {
            return null
        }
        val type = object : TypeToken<List<MovieDetail.Genre>>() {}.type
        return Gson().toJson(o, type)
    }

    @TypeConverter
    @JvmStatic
    fun toGenre(string: String?): List<MovieDetail.Genre>? {
        if (string == null) {
            return null
        }
        val type = object : TypeToken<List<MovieDetail.Genre>>() {}.type
        return Gson().fromJson(string, type)
    }

    @TypeConverter
    @JvmStatic
    fun spokenLanguagesToString(o: List<MovieDetail.SpokenLanguage>?): String? {
        if (o == null) {
            return null
        }
        val type = object : TypeToken<List<MovieDetail.SpokenLanguage>>() {}.type
        return Gson().toJson(o, type)
    }

    @TypeConverter
    @JvmStatic
    fun toSpokenLanguage(string: String?): List<MovieDetail.SpokenLanguage>? {
        if (string == null) {
            return null
        }
        val type = object : TypeToken<List<MovieDetail.SpokenLanguage>>() {}.type
        return Gson().fromJson(string, type)
    }

    /**
    * TvShowDetail
    * */
    @TypeConverter
    @JvmStatic
    fun createdBytoString(o: List<TvShowDetail.CreatedBy>?): String? {
        if (o == null) {
            return null
        }
        val type = object : TypeToken<List<TvShowDetail.CreatedBy>>() {}.type
        return Gson().toJson(o, type)
    }

    @TypeConverter
    @JvmStatic
    fun toCreatedBy(string: String?): List<TvShowDetail.CreatedBy>? {
        if (string == null) {
            return null
        }
        val type = object : TypeToken<List<TvShowDetail.CreatedBy>>() {}.type
        return Gson().fromJson(string, type)
    }

    @TypeConverter
    @JvmStatic
    fun tvProductionCompanyToString(o: List<TvShowDetail.ProductionCompany>?): String? {
        if (o == null) {
            return null
        }
        val type = object : TypeToken<List<TvShowDetail.ProductionCompany>>() {}.type
        return Gson().toJson(o, type)
    }

    @TypeConverter
    @JvmStatic
    fun toTvProductionCompany(string: String?): List<TvShowDetail.ProductionCompany>? {
        if (string == null) {
            return null
        }
        val type = object : TypeToken<List<TvShowDetail.ProductionCompany>>() {}.type
        return Gson().fromJson(string, type)
    }

    @TypeConverter
    @JvmStatic
    fun tvGenresToString(o: List<TvShowDetail.Genre>?): String? {
        if (o == null) {
            return null
        }
        val type = object : TypeToken<List<TvShowDetail.Genre>>() {}.type
        return Gson().toJson(o, type)
    }

    @TypeConverter
    @JvmStatic
    fun toTvGenre(string: String?): List<TvShowDetail.Genre>? {
        if (string == null) {
            return null
        }
        val type = object : TypeToken<List<TvShowDetail.Genre>>() {}.type
        return Gson().fromJson(string, type)
    }

    @TypeConverter
    @JvmStatic
    fun tvSeasonToString(o: List<TvShowDetail.Season>?): String? {
        if (o == null) {
            return null
        }
        val type = object : TypeToken<List<TvShowDetail.Season>>() {}.type
        return Gson().toJson(o, type)
    }

    @TypeConverter
    @JvmStatic
    fun toTvSeason(string: String?): List<TvShowDetail.Season>? {
        if (string == null) {
            return null
        }
        val type = object : TypeToken<List<TvShowDetail.Season>>() {}.type
        return Gson().fromJson(string, type)
    }

    @TypeConverter
    @JvmStatic
    fun tvNetworkToString(o: List<TvShowDetail.Network>?): String? {
        if (o == null) {
            return null
        }
        val type = object : TypeToken<List<TvShowDetail.Network>>() {}.type
        return Gson().toJson(o, type)
    }

    @TypeConverter
    @JvmStatic
    fun toTvNetwork(string: String?): List<TvShowDetail.Network>? {
        if (string == null) {
            return null
        }
        val type = object : TypeToken<List<TvShowDetail.Network>>() {}.type
        return Gson().fromJson(string, type)
    }

}
