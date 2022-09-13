package com.example.fixsermobileapp.expenses.adapter

import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView

import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.ImageView
import com.example.fixsermobileapp.R
import com.google.android.material.snackbar.Snackbar
import android.animation.ObjectAnimator

import android.animation.AnimatorSet
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.ProgressBar
import com.example.fixsermobileapp.expenses.entities.Expense
import android.graphics.Movie
import android.widget.Toast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CustomAdapterExpense() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var DURATION: Long = 200
    private var on_attach = true
    /*private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1*/
    private val LOADING = 0
    private val ITEM = 1
    private var isLoadingAdded = false
    private var mExpenseList:ArrayList<Expense> = ArrayList()
    private lateinit var context:Context
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */

    fun CustomAdapterExpense(context: Context) {
        this.context = context
        mExpenseList = ArrayList()
    }

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
            txtDesignationExpense = view.findViewById(R.id.txtDesignationExpense)
            txtQuantityExpense = view.findViewById(R.id.txtQteExpense)
            txtUnitPrice = view.findViewById(R.id.txtUnitPriceExpense)
            txtTypeExpense = view.findViewById(R.id.txtTypeExpense)
            txtDateExpence = view.findViewById(R.id.txtDateExpense)
            txtAmountTotal = view.findViewById(R.id.txtAmountTotalExpense)
            txtAmountFresh = view.findViewById(R.id.txtAmountExpenseFresh)
            txtAmountTotalFresh = view.findViewById(R.id.txtTotalExpenseFresh)


            view.setOnClickListener { v: View  ->
                val position: Int = getAdapterPosition()

                Snackbar.make(v, "Click detected on item $position",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show()
            }
        }

    }

     class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var progressBar: ProgressBar? = null

        init {
            progressBar = itemView.findViewById(R.id.progressBar)
        }
    }



    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        // Create a new view, which defines the UI of the list item
        /* val view = LayoutInflater.from(viewGroup.context)
             .inflate(R.layout.cards_layout_expense, viewGroup, false)
         //view.setOnClickListener(ExpensesActivity.myOnClickListener)

         return MyViewHolder(view)*/
        var myViewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(viewGroup.context)


        when(viewType){
            ITEM-> {
                val view = inflater.inflate(R.layout.cards_layout_expense, viewGroup, false)
                myViewHolder = MyViewHolder(view)
            }
            LOADING-> {
                val view = inflater.inflate(R.layout.item_loading, viewGroup, false)
                myViewHolder = LoadingViewHolder(view)
            }
        }
        return myViewHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val expense = mExpenseList[position]

        when(getItemViewType(position)){
            ITEM -> {
                val viewHolder: MyViewHolder = holder as MyViewHolder
                // Get element from your dataset at this position and replace the
                // contents of the view with that element
                var expenseDate:String? = null
                /*try {
                    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss ")
                    val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                    //expenseDate  = sdf.parse(expense.expense_date!!)
                    expenseDate  = sdf.format(format.parse(expense.expense_date!!)!!)

                }catch (e: ParseException){
                    e.printStackTrace()
                }*/
                viewHolder.txtDateExpence!!.text = expense.expense_date
                viewHolder.txtTypeExpense!!.text = expense.expense_type
                viewHolder.txtDesignationExpense!!.text = expense.expense_designation
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

            LOADING -> {
                val viewHolder: LoadingViewHolder = holder as LoadingViewHolder
                viewHolder.progressBar!!.visibility = View.VISIBLE
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mExpenseList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mExpenseList.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(Expense())
    }

     fun removeLoadingFooter() {
        isLoadingAdded = false
        //val position =  mExpenseList.size - 1
        val position =  mExpenseList.size.minus(1)
        val result = getItem(position)
         println("ok "+result)
         if (result != null) {

            mExpenseList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun add(expense: Expense) {
        mExpenseList.add(expense)
        notifyItemInserted(mExpenseList.size - 1)
    }

    fun addAll(expenses: ArrayList<Expense>) {
        for (result in expenses) {
            add(result)
        }
    }

    private fun getItem(position: Int): Expense {
        return mExpenseList.get(position)
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

}




//CustomAdapterExpense
 /*class CustomAdapterExpense(val mExpenseList:List<Expense> = ArrayList()) : RecyclerView.Adapter<CustomAdapterExpense.MyViewHolder>() {
    var DURATION: Long = 500
    private var on_attach = true
    *//*private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1*//*
    private val LOADING = 0
    private val ITEM = 1
    private val isLoadingAdded = false
    *//**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     *//*
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
            txtDesignationExpense = view.findViewById(R.id.txtDesignationExpense)
            txtQuantityExpense = view.findViewById(R.id.txtQteExpense)
            txtUnitPrice = view.findViewById(R.id.txtUnitPriceExpense)
            txtTypeExpense = view.findViewById(R.id.txtTypeExpense)
            txtDateExpence = view.findViewById(R.id.txtDateExpense)
            txtAmountTotal = view.findViewById(R.id.txtAmountTotalExpense)
            txtAmountFresh = view.findViewById(R.id.txtAmountExpenseFresh)
            txtAmountTotalFresh = view.findViewById(R.id.txtTotalExpenseFresh)


            view.setOnClickListener { v: View  ->
                val position: Int = getAdapterPosition()

                Snackbar.make(v, "Click detected on item $position",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show()
            }
        }

    }

    private class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var progressBar: ProgressBar? = null

        init {
             progressBar = itemView.findViewById(R.id.progressBar)
        }
    }

    *//*fun CustomAdapterExpense(data: ArrayList<DataModelExpense>?) {
        dataSet = data!!
    }*//*

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {

        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.cards_layout_expense, viewGroup, false)
        //view.setOnClickListener(ExpensesActivity.myOnClickListener)

        return MyViewHolder(view)

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        val expense = mExpenseList[position]
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.txtDateExpence!!.text = expense.expense_date.toString()
        viewHolder.txtDesignationExpense!!.text = expense.expense_designation
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
}*/


