package com.vedas.apna.BankDetails.View

import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import com.vedas.apna.BankDetails.Model.DirectDonations
import com.vedas.apna.BankDetails.Model.IndirectDonations

interface IBankView {
    fun bankresult(Success: String)
    fun ToastMsg(toastmsg: String)
    fun dialogCalender(dialog: MonthYearPickerDialogFragment)
    fun donationAmounts(directAmount:String, IndirectAmount:String, directDonations: List<DirectDonations>, inDirectDonations: List<IndirectDonations>)
}