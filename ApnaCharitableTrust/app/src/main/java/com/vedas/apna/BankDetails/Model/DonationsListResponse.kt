package com.vedas.apna.BankDetails.Model

import com.google.gson.annotations.SerializedName


data class DonationsListResponse (

    @SerializedName("response") var response : Int,
    @SerializedName("message") var message : String,
    @SerializedName("results") var results : Results

)