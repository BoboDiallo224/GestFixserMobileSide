package com.example.fixsermobileapp.expenses.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fixsermobileapp.R
import com.example.fixsermobileapp.expenses.OnExpenseItemClickListener
import com.example.fixsermobileapp.expenses.OnSearchExpenseClickListener
import com.example.fixsermobileapp.expenses.entities.Expense
import com.example.fixsermobileapp.expenses.entities.FreshExpense
import com.google.android.material.snackbar.Snackbar
import java.util.ArrayList

class SearchExpenseByDateAdapter(private val mContext: Context,
                                 private val mOnSearchExpenseClickListener: OnSearchExpenseClickListener)
    :RecyclerView.Adapter<SearchExpenseByDateAdapter.SearchExpenseViewHolder>() {

        val expenseList:ArrayList<String> = ArrayList()
        var isSelected:Boolean = false
        var lastClickedPosition:Int = RecyclerView.NO_POSITION
    inner class SearchExpenseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            val txt_searchEnterCategory = itemView.findViewById<TextView>(R.id.txt_searchEnterCategory)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchExpenseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_search_bycategory,parent,false)
        val holder = SearchExpenseViewHolder(view)
        holder.txt_searchEnterCategory.setOnClickListener(View.OnClickListener {

            if (!isSelected){
                isSelected = true
                holder.txt_searchEnterCategory.background = ContextCompat.getDrawable(parent.context, R.drawable.rounded_corner)
                val position = holder.bindingAdapterPosition
                val model = expenseList[position]
                mOnSearchExpenseClickListener.onSearchItem(position, model)
                lastClickedPosition = position
                notifyItemChanged(lastClickedPosition)
            }
            else{
                isSelected = false
                holder.txt_searchEnterCategory.background = ContextCompat.getDrawable(parent.context, R.drawable.btn_rounded_corner)
            }

        })

        /*if (lastClickedPosition == holder.bindingAdapterPosition){
            isSelected = false
            holder.txt_searchEnterCategory.background = ContextCompat.getDrawable(parent.context, R.drawable.btn_rounded_corner)
        }
        else{
            isSelected = true
            holder.txt_searchEnterCategory.background = ContextCompat.getDrawable(parent.context, R.drawable.rounded_corner)

        }*/

        return holder
    }

    override fun onBindViewHolder(holder: SearchExpenseViewHolder, position: Int) {
       val expense = expenseList[position]
        holder.txt_searchEnterCategory.text = expense
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    fun addAll(expenses: ArrayList<String>) {
        for (result in expenses) {
            add(result)
        }
    }

    fun add(expense: String) {
        expenseList.add(expense)
        notifyItemInserted(expenseList.size)
    }
}