package com.aastudio.sarollahi.api.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.*

data class Person(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("biography") val biography: String?,
    @SerializedName("birthday") val birthday: String?,
    @SerializedName("deathday") val deathday: String?,
    @SerializedName("gender") val gender: Int?,
    @SerializedName("homepage") val homepage: String?,
    @SerializedName("imdb_id") val imdb_id: String?,
    @SerializedName("popularity") val popularity: Float?,
    @SerializedName("adult") val adult: Boolean?,
    @SerializedName("profile_path") val profilePath: String?,
    @SerializedName("place_of_birth") val placeOfBirth: String?,
    @SerializedName("department") val department: String?,
    @SerializedName("character") val character: String?,
    @SerializedName("images") val images: ProfileImages?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(ProfileImages::class.java.classLoader) as? ProfileImages
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(biography)
        parcel.writeString(birthday)
        parcel.writeString(deathday)
        parcel.writeValue(gender)
        parcel.writeString(homepage)
        parcel.writeString(imdb_id)
        parcel.writeValue(popularity)
        parcel.writeValue(adult)
        parcel.writeString("$IMAGE_ADDRESS$profilePath")
        parcel.writeString(placeOfBirth)
        parcel.writeString(department)
        parcel.writeString(character)
        parcel.writeValue(images)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Person> {
        override fun createFromParcel(parcel: Parcel): Person {
            return Person(parcel)
        }

        override fun newArray(size: Int): Array<Person?> {
            return arrayOfNulls(size)
        }
    }
}
