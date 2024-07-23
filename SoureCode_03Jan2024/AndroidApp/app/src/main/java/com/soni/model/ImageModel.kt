package com.soni.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class ImageModel() : Parcelable {
    var isFromLocal = true

    @SerializedName("id")
    var id: Int? = null

    @SerializedName("asset_type_id")
    var asset_type_id: Int? = null

    @SerializedName("asset_url")
    var asset_url: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("category_type_id")
    var category_type_id: Int? = null

    @SerializedName("created_at")
    var created_at: String? = null

    @SerializedName("deleted_at")
    var deleted_at: String? = null

    @SerializedName("is_active")
    var is_active: Int? = null

    @SerializedName("property_id")
    var property_id: Int? = null

    @SerializedName("room_id")
    var room_id: Int? = null

    @SerializedName("updated_at")
    var updated_at: String? = null

    constructor(parcel: Parcel) : this() {
        isFromLocal = parcel.readByte() != 0.toByte()
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        asset_type_id = parcel.readValue(Int::class.java.classLoader) as? Int
        asset_url = parcel.readString()
        name = parcel.readString()
        category_type_id = parcel.readValue(Int::class.java.classLoader) as? Int
        created_at = parcel.readString()
        deleted_at = parcel.readString()
        is_active = parcel.readValue(Int::class.java.classLoader) as? Int
        property_id = parcel.readValue(Int::class.java.classLoader) as? Int
        room_id = parcel.readValue(Int::class.java.classLoader) as? Int
        updated_at = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isFromLocal) 1 else 0)
        parcel.writeValue(id)
        parcel.writeValue(asset_type_id)
        parcel.writeString(asset_url)
        parcel.writeString(name)
        parcel.writeValue(category_type_id)
        parcel.writeString(created_at)
        parcel.writeString(deleted_at)
        parcel.writeValue(is_active)
        parcel.writeValue(property_id)
        parcel.writeValue(room_id)
        parcel.writeString(updated_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageModel> {
        override fun createFromParcel(parcel: Parcel): ImageModel {
            return ImageModel(parcel)
        }

        override fun newArray(size: Int): Array<ImageModel?> {
            return arrayOfNulls(size)
        }
    }
}