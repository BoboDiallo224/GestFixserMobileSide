package com.example.fixsermobileapp.expenses.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.fixsermobileapp.R
import com.example.fixsermobileapp.expenses.entities.TypeExpense

class ExpenseTypeAdapter(ctx: Context, countries: ArrayList<TypeExpense>) :
        ArrayAdapter<TypeExpense>(ctx, 0, countries) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent);
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent);
    }

    fun createItemView(position: Int, recycledView: View?, parent: ViewGroup): View {
        val expenseType = getItem(position)

        val view = recycledView ?: LayoutInflater.from(context).inflate(
            R.layout.type_expense_adapter_spinner,
            parent,
            false
        )
        val txt_type_expense:TextView = view.findViewById(R.id.txt_type_expense)

        expenseType?.let {
            //view.imageViewFlag.setImageResource(country.flag)
            txt_type_expense.text = expenseType.design_type_expense
        }
        return view
    }
}