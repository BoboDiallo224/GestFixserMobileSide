package com.example.fixsermobileapp.expenses
import com.example.fixsermobileapp.expenses.entities.FreshExpense

interface OnExpenseItemClickListener {
    fun onUpdate(position: Int, model: FreshExpense)
}