package com.vedas.apna.BankDetails.Model

class UploadBankModel {
    var mobileNumber: String? = null
    var nameasperbank: String? = null
    var accountno: String? = null
    var bankName: String? = null
    var branchName: String? = null
    var ifsc: String? = null
    var village: String? = null
    var dist: String? = null
    var state: String? = null
    //var email: String? = null

    companion object {
        var myObj: UploadBankModel? = null
        val instance: UploadBankModel?
            get() {
                if (myObj == null) {
                    myObj = UploadBankModel()
                }
                return myObj
            }
    }
}