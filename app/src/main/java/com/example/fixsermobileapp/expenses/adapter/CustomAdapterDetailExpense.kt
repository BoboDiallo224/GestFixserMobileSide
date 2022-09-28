package com.example.fixsermobileapp.expenses.adapter

import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView

import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import com.example.fixsermobileapp.R
import android.animation.ObjectAnimator

import android.animation.AnimatorSet
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import com.example.fixsermobileapp.expenses.ExpenseDetails
import com.example.fixsermobileapp.expenses.entities.Expense
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

//CustomAdapterExpense
 class CustomAdapterDetailExpense(val mExpenseList:List<Expense> = ArrayList()) : RecyclerView.Adapter<CustomAdapterDetailExpense.MyViewHolder>() {
    var DURATION: Long = 500
    private var on_attach = true
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    private val LOADING = 0
    private val ITEM = 1
    private val isLoadingAdded = false
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtDesignationExpense: TextView? = null
        var txtQuantityExpense: TextView? = null
        var txtUnitPrice: TextView? = null
        var txtTypeExpense: TextView? = null
        var txtDateExpence: TextView? = null
        var txtAmountTotal: TextView? = null
        var txtAmountFresh: TextView? = null
        var txtAmountTotalFresh: TextView? = null
        //var imageViewIcon: ImageView? = null

        init {
            // Define click listener for the ViewHolder's View.
            txtDesignationExpense = view.findViewById(R.id.txt_designExpenseDetail)
            txtQuantityExpense = view.findViewById(R.id.txtQteDetExpense)
            txtUnitPrice = view.findViewById(R.id.txtUnitPriceDetExpense)
            txtTypeExpense = view.findViewById(R.id.txtCategDetExpense)
            txtDateExpence = view.findViewById(R.id.txtDateDetailExpense)
            txtAmountTotal = view.findViewById(R.id.txtAmountTotalDetExpense)
            txtAmountFresh = view.findViewById(R.id.txtAmountDetExpenseFresh)
            txtAmountTotalFresh = view.findViewById(R.id.txtTotAmountExpAndFreshDet)

        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {

        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_layout_expense, viewGroup, false)
        //view.setOnClickListener(ExpensesActivity.myOnClickListener)
        val holder = MyViewHolder(view)

        holder.itemView.setOnClickListener(View.OnClickListener {

            val intent = Intent(viewGroup.context, ExpenseDetails::class.java)
            viewGroup.context.startActivity(intent)

            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_bottom)
        })

        return MyViewHolder(view)

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        val expense = mExpenseList[position]
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE)
        val targetFormat = SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH)
        var dateStr:Date? = null
        /*try {
            dateStr  = formatter.parse(expense.expense_date!!)
        } catch (e: ParseException) {
            e.printStackTrace()
        }*/
        dateStr  = formatter.parse(expense.expense_date!!)
        val formatedDate = targetFormat.format(dateStr!!)
        viewHolder.txtDateExpence!!.text = formatedDate
        viewHolder.txtDesignationExpense!!.text = expense.expense_designation
        viewHolder.txtTypeExpense!!.text = expense.expense_type
        //viewHolder.textViewVersion!!.text = dataSet!![position].version
        viewHolder.txtQuantityExpense!!.text = expense.expense_quantity.toString()
        viewHolder.txtUnitPrice!!.text = expense.expense_unit_price.toString()
        //Calculate
        val totalAmount = expense.expense_unit_price!!.times(expense.expense_quantity!!)
        val totalAmountFresh = expense.freshExpense!!.sumOf { it.fresh_expense_amount!! }

        viewHolder.txtAmountTotal!!.text = totalAmount.toString()
        viewHolder.txtAmountFresh!!.text = totalAmountFresh.toString()
        viewHolder.txtAmountTotalFresh!!.text = totalAmount.plus(totalAmountFresh).toString()

        //viewHolder.imageViewIcon!!.setImageResource(images[position])

        setAnimation(viewHolder.itemView, position)
        //FromLeftToRight(viewHolder.itemView, position)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mExpenseList.size
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                Log.d(TAG, "onScrollStateChanged: Called $newState")
                on_attach = false
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        super.onAttachedToRecyclerView(recyclerView)
    }

    private fun setAnimation(itemView: View, i: Int) {
        var i = i
        if (!on_attach) {
            i = -1
        }
        val isNotFirstItem = i == -1
        i++
        itemView.alpha = 0f
        val animatorSet = AnimatorSet()
        val animator = ObjectAnimator.ofFloat(itemView, "alpha", 0f, 0.5f, 1.0f)
        ObjectAnimator.ofFloat(itemView, "alpha", 0f).start()
        animator.setStartDelay(if (isNotFirstItem) DURATION / 2 else i * DURATION / 3)
        animator.duration = 500
        animatorSet.play(animator)
        animator.start()
    }

    private fun FromLeftToRight(itemView: View, i: Int) {
        var i = i
        if (!on_attach) {
            i = -1
        }
        val not_first_item = i == -1
        i = i + 1
        itemView.translationX = -400f
        itemView.alpha = 0f
        val animatorSet = AnimatorSet()
        val animatorTranslateY = ObjectAnimator.ofFloat(itemView, "translationX", -400f, 0f)
        val animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1f)
        ObjectAnimator.ofFloat(itemView, "alpha", 0f).start()
        animatorTranslateY.startDelay = if (not_first_item) DURATION else i * DURATION
        animatorTranslateY.duration = (if (not_first_item) 2 else 1) * DURATION
        animatorSet.playTogether(animatorTranslateY, animatorAlpha)
        animatorSet.start()
    }
}


