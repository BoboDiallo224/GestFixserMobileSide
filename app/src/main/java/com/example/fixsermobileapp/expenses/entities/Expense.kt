package com.example.fixsermobileapp.expenses.entities

import com.google.gson.annotations.SerializedName
import java.util.*


data class Expense (
    @SerializedName("id_expense")
    val id_expense:Long? = null,
    @SerializedName("expense_designation")
    var expense_designation:String? = null,
    @SerializedName("expense_quantity")
    var expense_quantity:Int? = null,
    @SerializedName("expense_unit_price")
    var expense_unit_price:Double? = null,
    @SerializedName("expense_type")
    var expense_type:String? = null,
    //@SerializedName("expense_date")
    var expense_date: String? = null,
    @SerializedName("freshExpense")
    var freshExpense:List<FreshExpense>? = emptyList()
)