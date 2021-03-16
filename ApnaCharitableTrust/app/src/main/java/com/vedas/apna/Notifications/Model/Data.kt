package com.vedas.apna.Notifications.Model

import com.google.gson.annotations.SerializedName

data class Data (
	@SerializedName("title") val title : String,
	@SerializedName("body") val body : String,
	@SerializedName("notifyImages") val notifyImages : List<String>,
	@SerializedName("notifyID") val notifyID : String
)