package com.example.fixsermobileapp.expenses.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fixsermobileapp.R
import com.example.fixsermobileapp.expenses.entities.PaymentExpenseType
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ExpensePaymentDetailAdapter()
    :RecyclerView.Adapter<ExpensePaymentDetailAdapter.ExpensePaymentViewHolder>() {

   /* private val mContext: Context,
    private val mOnExpenseOnClickListner:OnExpenseItemClickListener*/
        val paymentExpenseList:ArrayList<PaymentExpenseType> = ArrayList()

        inner class ExpensePaymentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            val txtAmountExpensePayment = itemView.findViewById<TextView>(R.id.txtAmountExpensePayment)
            val txtExpenseDatePayment = itemView.findViewById<TextView>(R.id.txtExpenseDatePayment)
            //val imgBtnRemoveFresh = itemView.findViewById<ImageButton>(R.id.ibtn_delet_itemExpenseFresh)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensePaymentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_detail_expense_paiement,parent,false)
        val holder = ExpensePaymentViewHolder(view)

        holder.itemView.setOnClickListener(View.OnClickListener {
            val position = holder.bindingAdapterPosition
            val model = paymentExpenseList[position]
            //mOnExpenseOnClickListner.onUpdate(position,model)
        })

        /*holder.imgBtnRemoveFresh.setOnClickListener(View.OnClickListener {
            val position = holder.adapterPosition
            val model = freshExpenseList[position]
            removeFresh(model)
        })*/

        return holder
    }

    override fun onBindViewHolder(holder: ExpensePaymentViewHolder, position: Int) {

       val paymentExpenseType = paymentExpenseList[position]

        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE)
        val targetFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val dateStr: Date? = formatter.parse(paymentExpenseType.payment_expense_date!!)
        val formatedDate = targetFormat.format(dateStr!!)

        holder.txtExpenseDatePayment.text = formatedDate.toString()
        holder.txtAmountExpensePayment.text = paymentExpenseType.amountPayAddExpense.toString()
    }

    override fun getItemCount(): Int {
        return paymentExpenseList.size
    }

    fun removeFresh(model: PaymentExpenseType) {
        val position = paymentExpenseList.indexOf(model)
        paymentExpenseList.remove(model)
        notifyItemRemoved(position)
    }

    /*fun updateFreshExpense(model: PaymentExpenseType?) {

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
    }*/

    fun add(model: PaymentExpenseType) {
        paymentExpenseList.add(model)
        // notifyDataSetChanged() // this method is costly I avoid it whenever possible
        notifyItemInserted(paymentExpenseList.size)
    }

    fun addAll(model: ArrayList<PaymentExpenseType>){

        for (result in model){
            add(result)
        }

        /*for (i in 0 until  2){
            for (result in model){
                add(result)
            }
        }*/

    }

    /*fun addAll(expenses: ArrayList<Expense>) {
        for (result in expenses) {
            add(result)
        }
    }*/

}