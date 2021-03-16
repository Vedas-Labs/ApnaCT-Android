package com.vedas.apna.BankDetails.Model

import com.google.gson.annotations.SerializedName

data class Results (

    @SerializedName("totalDirectDonation") var totalDirectDonation : String,
    @SerializedName("totalInDirctDonations") var totalInDirctDonations : String,
    @SerializedName("DirectDonations") var DirectDonations : List<DirectDonations>,
    @SerializedName("indirectDonations") var indirectDonations : List<IndirectDonations>

)