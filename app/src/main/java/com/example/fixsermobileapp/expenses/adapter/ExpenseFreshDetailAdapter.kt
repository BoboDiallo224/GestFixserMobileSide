package com.example.fixsermobileapp.expenses.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fixsermobileapp.R
import com.example.fixsermobileapp.expenses.OnExpenseItemClickListener
import com.example.fixsermobileapp.expenses.entities.Expense
import com.example.fixsermobileapp.expenses.entities.FreshExpense
import java.util.ArrayList

class ExpenseFreshDetailAdapter()
    :RecyclerView.Adapter<ExpenseFreshDetailAdapter.ExpenseFreshViewHolder>() {
        /*private val mContext: Context, private val mOnExpenseOnClickListner:OnExpenseItemClickListener*/
        val freshExpenseList:ArrayList<FreshExpense> = ArrayList()

        inner class ExpenseFreshViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            val txtFreshDesignation = itemView.findViewById<TextView>(R.id.txtDesignExpenseDetFresh)
            val txtAmountFresh = itemView.findViewById<TextView>(R.id.txt_amountDetFreshExpense)
            //val imgBtnRemoveFresh = itemView.findViewById<ImageButton>(R.id.ibtn_delet_itemExpenseFresh)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseFreshViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_detail_fresh_expense,parent,false)
        val holder = ExpenseFreshViewHolder(view)

        holder.itemView.setOnClickListener(View.OnClickListener {
            val position = holder.bindingAdapterPosition
            val model = freshExpenseList[position]
            //mOnExpenseOnClickListner.onUpdate(position,model)
        })

        /*holder.imgBtnRemoveFresh.setOnClickListener(View.OnClickListener {
            val position = holder.adapterPosition
            val model = freshExpenseList[position]
            removeFresh(model)
        })*/

        return holder
    }

    override fun onBindViewHolder(holder: ExpenseFreshViewHolder, position: Int) {
       val freshExpense = freshExpenseList[position]
        holder.txtFreshDesignation.text = freshExpense.fresh_expense_designation
        holder.txtAmountFresh.text = freshExpense.fresh_expense_amount.toString()
    }

    override fun getItemCount(): Int {
        return freshExpenseList.size
    }

    fun removeFresh(model: FreshExpense) {
        val position = freshExpenseList.indexOf(model)
        freshExpenseList.remove(model)
        notifyItemRemoved(position)
    }

    fun updateFreshExpense(model: FreshExpense?) {

        if (model == null) return println("we cannot update the value because it is null") // we cannot update the value because it is null

        for (item in freshExpenseList) {
            // search by id
            if (item.id_forUpdateList == model.id_forUpdateList) {
                val position = freshExpenseList.indexOf(model)
                freshExpenseList[position] = model
                notifyItemChanged(position)
                break // we don't need to continue any more iterations
            }
        }
    }

    fun addFreshExpense(model: FreshExpense) {
        println(model)
        freshExpenseList.add(model)
        // notifyDataSetChanged() // this method is costly I avoid it whenever possible
        notifyItemInserted(freshExpenseList.size)
    }

    /*fun addAll(model: FreshExpense){

    }*/

    fun addAll(expenses: ArrayList<FreshExpense>) {
        for (result in expenses) {
            add(result)
        }
    }

    fun add(expense: FreshExpense) {
        freshExpenseList.add(expense)
        notifyItemInserted(freshExpenseList.size)
    }

    fun getNextItemId(): Int {
        var id = 1
        if (freshExpenseList.isNotEmpty()) {
            // .last is equivalent to .size() - 1
            // we want to add 1 to that id and return it
            id = (freshExpenseList.last().id_forUpdateList)!!.toInt() + 1
        }
        return id
    }

}