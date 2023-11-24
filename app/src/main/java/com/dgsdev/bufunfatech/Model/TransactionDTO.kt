package com.dgsdev.bufunfatech.Model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class TransactionDTO (
    var type: String,
    var category: String,
    var title: String,
    var amount: Double,
    var date: String,
    var day:Int,
    var month:Int,
    var year:Int,
    var note: String
): Parcelable








