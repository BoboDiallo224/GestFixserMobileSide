package com.example.fixsermobileapp.expenses
import com.example.fixsermobileapp.expenses.entities.Expense

interface OnExpenseItemClickListener2 {
    fun getExpenseId(position: Int, model: Expense)
}