package com.vedas.apna.Notifications.Model

import com.google.gson.annotations.SerializedName

data class MemberInbox (
	@SerializedName("notifyInbox") var notifyInbox : List<NotifyInbox>,
	@SerializedName("mobileNumber") var mobileNumber : String
)