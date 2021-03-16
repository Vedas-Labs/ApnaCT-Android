package com.vedas.apna.BankDetails.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class DirectDonations (

    @SerializedName("Donar_Name") var DonarName : String,
    @SerializedName("Donation_Amount") var DonationAmount : Int,
    @SerializedName("payDate") var payDate : String,
    @SerializedName("directSharedAmount") var directSharedAmount : Double,
    @SerializedName("paymentType") var paymentType : String,
    @SerializedName("isAprove") var isAprove : Boolean):Serializable