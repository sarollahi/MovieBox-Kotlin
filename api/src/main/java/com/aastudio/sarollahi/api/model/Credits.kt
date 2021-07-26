package com.aastudio.sarollahi.api.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Credits(
    @SerializedName("cast") val castList: List<Person>?,
    @SerializedName("crew") val crewList: List<Person>?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(Person),
        parcel.createTypedArrayList(Person)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(castList)
        parcel.writeTypedList(crewList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Credits> {
        override fun createFromParcel(parcel: Parcel): Credits {
            return Credits(parcel)
        }

        override fun newArray(size: Int): Array<Credits?> {
            return arrayOfNulls(size)
        }
    }
}
