/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.api.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

const val IMAGE_ADDRESS = "https://image.tmdb.org/t/p/original"

data class Movie(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String?,
    @SerializedName("runtime") val runTime: Int?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("imdb_id") val imdbId: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("vote_average") val rating: Float?,
    @SerializedName("vote_count") val voteCount: Int?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("original_language") val originalLanguage: String?,
    @SerializedName("budget") val budget: String?,
    @SerializedName("genres") val genre: List<Genre>?,
    @SerializedName("spoken_languages") val language: List<Language>?,
    @SerializedName("production_countries") val country: List<Country>?,
    @SerializedName("production_companies") val company: List<Company>?,
    @SerializedName("mpa_rating") val mpaRating: String?,
    @SerializedName("credits") val credits: Credits?,
    @SerializedName("reviews") val reviews: Reviews?,
    @SerializedName("videos") val trailer: Trailers?,
    @SerializedName("external_ids") val externalIds: ExternalId?,
    @SerializedName("similar") val similar: SimilarAndRecommendationMovies?,
    @SerializedName("recommendations") val recommendations: SimilarAndRecommendationMovies?,
    @SerializedName("torrents") val torrents: List<BaseMovie.Torrent>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(Genre),
        parcel.createTypedArrayList(Language),
        parcel.createTypedArrayList(Country),
        parcel.createTypedArrayList(Company),
        parcel.readString(),
        parcel.readValue(Credits::class.java.classLoader) as? Credits,
        parcel.readValue(Reviews::class.java.classLoader) as? Reviews,
        parcel.readValue(Trailers::class.java.classLoader) as? Trailers,
        parcel.readValue(ExternalId::class.java.classLoader) as? ExternalId,
        parcel.readValue(SimilarAndRecommendationMovies::class.java.classLoader) as? SimilarAndRecommendationMovies,
        parcel.readValue(SimilarAndRecommendationMovies::class.java.classLoader) as? SimilarAndRecommendationMovies,
        parcel.createTypedArrayList(BaseMovie.Torrent.CREATOR)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeValue(runTime)
        parcel.writeString(overview)
        parcel.writeString(imdbId)
        parcel.writeString(posterPath)
        parcel.writeString(backdropPath)
        parcel.writeValue(rating)
        parcel.writeValue(voteCount)
        parcel.writeString(releaseDate)
        parcel.writeString(originalLanguage)
        parcel.writeString(budget)
        parcel.writeTypedList(genre)
        parcel.writeTypedList(language)
        parcel.writeTypedList(country)
        parcel.writeTypedList(company)
        parcel.writeString(mpaRating)
        parcel.writeValue(credits)
        parcel.writeValue(reviews)
        parcel.writeValue(trailer)
        parcel.writeValue(externalIds)
        parcel.writeValue(similar)
        parcel.writeValue(recommendations)
        parcel.writeTypedList(torrents)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}

data class SimilarAndRecommendationMovies(
    @SerializedName("results") val results: List<Movie>?,
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(Movie))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(results)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SimilarAndRecommendationMovies> {
        override fun createFromParcel(parcel: Parcel): SimilarAndRecommendationMovies {
            return SimilarAndRecommendationMovies(parcel)
        }

        override fun newArray(size: Int): Array<SimilarAndRecommendationMovies?> {
            return arrayOfNulls(size)
        }
    }
}
