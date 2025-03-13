package com.lihkin16.foodit_3.model

import android.os.Parcel
import android.os.Parcelable

data class OrderDetails(
    var userUid: String? = null,
    var userName: String? = null,
    var userAddress: String? = null,
    var userEmail: String? = null,
    var userPhoneNo: String? = null,
    var totalPrice: String? = null,
    var paymentRecieved: Boolean = false,
    var itemPushKey: String? = null,
    var currentTime: Long = 0,
    var orderAccepted: Boolean = false,
    var profileImage: String? = null,
    var orderReceived: Boolean = false,
    var foodItems: List<FoodItems>? = null
) : Parcelable {
    constructor(parcel: Parcel) : this() {
        userUid = parcel.readString()
        userName = parcel.readString()
        userAddress = parcel.readString()
        userEmail = parcel.readString()
        userPhoneNo = parcel.readString()
        totalPrice = parcel.readString()
        paymentRecieved = parcel.readByte() != 0.toByte()
        itemPushKey = parcel.readString()
        currentTime = parcel.readLong()
        orderAccepted = parcel.readByte() != 0.toByte()
        profileImage = parcel.readString()
        orderReceived = parcel.readByte() != 0.toByte()
        foodItems = parcel.createTypedArrayList(FoodItems)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userUid)
        parcel.writeString(userName)
        parcel.writeString(userAddress)
        parcel.writeString(userEmail)
        parcel.writeString(userPhoneNo)
        parcel.writeString(totalPrice)
        parcel.writeByte(if (paymentRecieved) 1 else 0)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentTime)
        parcel.writeByte(if (orderAccepted) 1 else 0)
        parcel.writeString(profileImage)
        parcel.writeByte(if (orderReceived) 1 else 0)
        parcel.writeTypedList(foodItems)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<OrderDetails> {
        override fun createFromParcel(parcel: Parcel) = OrderDetails(parcel)
        override fun newArray(size: Int) = arrayOfNulls<OrderDetails?>(size)
    }
}


data class FoodItems(
    val imageUrl: String,
    val name: String,
    val description: String,
    val price: String,
    val quantity: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imageUrl)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(price)
        parcel.writeString(quantity)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<FoodItems> {
        override fun createFromParcel(parcel: Parcel) = FoodItems(parcel)
        override fun newArray(size: Int) = arrayOfNulls<FoodItems?>(size)
    }
}
