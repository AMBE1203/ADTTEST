package com.ambe.adttest.model

import android.os.Parcel
import android.os.Parcelable

/**
 *  Created by AMBE on 15/8/2019 at 13:41 PM.
 */

data class Homes(
        val created_at: String,
        val updated_at: String,
        val id: Int,
        val orientation: Int,
        val order: Int,
        val time: Int,
        val marketId: Int,
        val deviceId: Int,
        val mediaId: Int,
        val media: Media
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readParcelable(Media::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(created_at)
        parcel.writeString(updated_at)
        parcel.writeInt(id)
        parcel.writeInt(orientation)
        parcel.writeInt(order)
        parcel.writeInt(time)
        parcel.writeInt(marketId)
        parcel.writeInt(deviceId)
        parcel.writeInt(mediaId)
        parcel.writeParcelable(media, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Homes> {
        override fun createFromParcel(parcel: Parcel): Homes {
            return Homes(parcel)
        }

        override fun newArray(size: Int): Array<Homes?> {
            return arrayOfNulls(size)
        }
    }
}