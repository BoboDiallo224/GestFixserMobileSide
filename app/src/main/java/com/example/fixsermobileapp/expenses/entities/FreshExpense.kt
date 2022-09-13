package com.example.fixsermobileapp.expenses.entities

class FreshExpense (
    val id_fresh_expense:Long? = null,
    val id_forUpdateList:Long? = null,
    var fresh_expense_designation:String? = null,
    var fresh_expense_amount:Double? = null,
    var expense: Expense? = null
)