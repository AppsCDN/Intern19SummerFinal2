package com.example.intern19summerfinal2.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Manga(
    @SerializedName("a") var alias: String? = null,
    @SerializedName("c") var category: ArrayList<String>? = null,
    @SerializedName("h") var hits: Int = 0,
    @SerializedName("i") var id: String? = null,
    @SerializedName("im") var image: String? = null,
    @SerializedName("ld") var lastChapterDate: Long = 0,
    @SerializedName("s") var status: Int = 0,
    @SerializedName("t") var title: String? = null
) : Parcelable {

    override fun writeToParcel(parcel: Parcel?, i: Int) {
        parcel?.writeString(alias)
        parcel?.writeInt(hits)
        parcel?.writeString(id)
        parcel?.writeString(image)
        parcel?.writeLong(lastChapterDate)
        parcel?.writeInt(status)
        parcel?.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    constructor(parcel: Parcel) : this() {
        alias = parcel.readString()
        hits = parcel.readInt()
        id = parcel.readString()
        image = parcel.readString()
        lastChapterDate = parcel.readLong()
        status = parcel.readInt()
        title = parcel.readString()
    }

    companion object CREATOR : Parcelable.Creator<Manga> {
        override fun createFromParcel(parcel: Parcel): Manga {
            return Manga(parcel)
        }

        override fun newArray(size: Int): Array<Manga?> {
            return arrayOfNulls(size)
        }
    }
}
