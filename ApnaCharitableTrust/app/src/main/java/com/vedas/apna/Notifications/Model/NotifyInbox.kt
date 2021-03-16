package com.vedas.apna.Notifications.Model

import com.google.gson.annotations.SerializedName

data class NotifyInbox (

	@SerializedName("notification") val notification : Notification,
	@SerializedName("data") val data : Data,
	@SerializedName("timeStamp") var timeStamp : String,
	@SerializedName("isRead") val isRead : Boolean
)