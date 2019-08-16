package com.ambe.adttest.model

import android.os.Parcel
import android.os.Parcelable

/**
 *  Created by AMBE on 15/8/2019 at 13:33 PM.
 */
data class Media(
    val created_at: String,
    val updated_at: String,
    val id: Int,
    val url: String,
    val type: Int,
    val marketId: Int
):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(created_at)
        parcel.writeString(updated_at)
        parcel.writeInt(id)
        parcel.writeString(url)
        parcel.writeInt(type)
        parcel.writeInt(marketId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Media> {
        override fun createFromParcel(parcel: Parcel): Media {
            return Media(parcel)
        }

        override fun newArray(size: Int): Array<Media?> {
            return arrayOfNulls(size)
        }
    }
}