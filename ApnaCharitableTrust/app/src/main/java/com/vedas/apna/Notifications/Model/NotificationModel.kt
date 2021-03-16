package com.vedas.apna.Notifications.Model

import com.google.gson.annotations.SerializedName

data class NotificationModel (
    @SerializedName("response") val response : Int,
    @SerializedName("message") val message : String,
    @SerializedName("memberInbox") val memberInbox : List<MemberInbox>
)