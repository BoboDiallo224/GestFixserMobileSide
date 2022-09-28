package com.example.fixsermobileapp.expenses.entities

import com.google.gson.annotations.SerializedName

class PaymentExpenseType (
    @SerializedName("id_payment_expense")
    val id_payment_expense:Long? = null,
    @SerializedName("amountPayAddExpense")
    var amountPayAddExpense:Double? = null ,
    var payment_expense_date:String? = null
)
