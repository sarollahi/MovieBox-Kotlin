/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.api.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TVShow(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("last_air_date") val lastAirDate: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("genres") val genre: List<Genre>?,
    @SerializedName("original_language") val originalLanguage: String?,
    @SerializedName("origin_country") val country: List<String>?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("vote_average") val rating: Float?,
    @SerializedName("vote_count") val voteCount: Int?,
    @SerializedName("episode_run_time") val runTime: List<String>?,
    @SerializedName("reviews") val reviews: Reviews?,
    @SerializedName("videos") val trailer: Trailers?,
    @SerializedName("external_ids") val externalIds: ExternalId?,
    @SerializedName("credits") val credits: Credits?,
    @SerializedName("spoken_languages") val language: List<Language>?,
    @SerializedName("production_companies") val company: List<Company>?,
    @SerializedName("similar") val similar: SimilarAndRecommendationShows?,
    @SerializedName("recommendations") val recommendations: SimilarAndRecommendationShows?,
    @SerializedName("torrents") val torrents: List<BaseMovie.Torrent>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(Genre),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.createStringArrayList(),
        parcel.readValue(Reviews::class.java.classLoader) as? Reviews,
        parcel.readValue(Trailers::class.java.classLoader) as? Trailers,
        parcel.readValue(ExternalId::class.java.classLoader) as? ExternalId,
        parcel.readValue(Credits::class.java.classLoader) as? Credits,
        parcel.createTypedArrayList(Language),
        parcel.createTypedArrayList(Company),
        parcel.readValue(SimilarAndRecommendationShows::class.java.classLoader) as? SimilarAndRecommendationShows,
        parcel.readValue(SimilarAndRecommendationShows::class.java.classLoader) as? SimilarAndRecommendationShows,
        parcel.createTypedArrayList(BaseMovie.Torrent.CREATOR)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(firstAirDate)
        parcel.writeString(lastAirDate)
        parcel.writeString(backdropPath)
        parcel.writeTypedList(genre)
        parcel.writeString(originalLanguage)
        parcel.writeStringList(country)
        parcel.writeString(overview)
        parcel.writeString(posterPath)
        parcel.writeValue(rating)
        parcel.writeValue(voteCount)
        parcel.writeValue(runTime)
        parcel.writeValue(reviews)
        parcel.writeValue(trailer)
        parcel.writeValue(externalIds)
        parcel.writeValue(credits)
        parcel.writeTypedList(language)
        parcel.writeTypedList(company)
        parcel.writeValue(similar)
        parcel.writeValue(recommendations)
        parcel.writeTypedList(torrents)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TVShow> {
        override fun createFromParcel(parcel: Parcel): TVShow {
            return TVShow(parcel)
        }

        override fun newArray(size: Int): Array<TVShow?> {
            return arrayOfNulls(size)
        }
    }
}

data class SimilarAndRecommendationShows(
    @SerializedName("results") val results: List<TVShow>?,
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(TVShow))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(results)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SimilarAndRecommendationShows> {
        override fun createFromParcel(parcel: Parcel): SimilarAndRecommendationShows {
            return SimilarAndRecommendationShows(parcel)
        }

        override fun newArray(size: Int): Array<SimilarAndRecommendationShows?> {
            return arrayOfNulls(size)
        }
    }
}
