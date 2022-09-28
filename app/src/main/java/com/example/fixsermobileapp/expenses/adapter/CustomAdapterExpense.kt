package com.example.fixsermobileapp.expenses.adapter

import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView

import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import com.example.fixsermobileapp.R
import com.google.android.material.snackbar.Snackbar
import android.animation.ObjectAnimator

import android.animation.AnimatorSet
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.ProgressBar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.fixsermobileapp.expenses.AddExpenseActivity
import com.example.fixsermobileapp.expenses.ExpenseDetails
import com.example.fixsermobileapp.expenses.OnExpenseItemClickListener
import com.example.fixsermobileapp.expenses.OnExpenseItemClickListener2
import com.example.fixsermobileapp.expenses.entities.Expense
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/*class CustomAdapterExpense() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var DURATION: Long = 200
    private var on_attach = true
    private val VIEW_TYPE_ITEM = 1
    private val VIEW_TYPE_LOADING = 0
    private val LOADING = 0
    private val ITEM = 1
    private var isLoadingAdded = false
    private var mExpenseList:ArrayList<Expense>? = ArrayList()
    private lateinit var context:Context
    *//**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     *//*

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
        *//* val view = LayoutInflater.from(viewGroup.context)
             .inflate(R.layout.cards_layout_expense, viewGroup, false)
         //view.setOnClickListener(ExpensesActivity.myOnClickListener)

         return MyViewHolder(view)*//*
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
        val expense = mExpenseList!![position]

        when(getItemViewType(position)){
            ITEM -> {
                val viewHolder: MyViewHolder = holder as MyViewHolder
                // Get element from your dataset at this position and replace the
                // contents of the view with that element
                var expenseDate:String? = null
                *//*try {
                    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss ")
                    val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                    //expenseDate  = sdf.parse(expense.expense_date!!)
                    expenseDate  = sdf.format(format.parse(expense.expense_date!!)!!)

                }catch (e: ParseException){
                    e.printStackTrace()
                }*//*
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
        return mExpenseList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        *//*return if (mExpenseList!![position] == null) {
            LOADING
        } else {
            ITEM
        }*//*
        return if (position == mExpenseList!!.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    *//*override fun getItemViewType(position: Int): Int {
        return if (mExpenseList!![position] == null) LOADING else ITEM
    }*//*

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(Expense())
    }

     fun removeLoadingFooter() {
        isLoadingAdded = false
        //val position =  mExpenseList.size - 1
        val position =  mExpenseList!!.size.minus(1)
        val result = getItem(position)
         println("ok "+result)
         mExpenseList!!.removeAt(position)

         notifyItemRemoved(position)
    }

    fun removeFresh(model: Expense) {
        val position = mExpenseList!!.indexOf(model).minus(1)
        mExpenseList!!.remove(model)
        notifyItemRemoved(position)
    }

    fun add(expense: Expense) {
        mExpenseList!!.add(expense)
        notifyItemInserted(mExpenseList!!.size - 1)
    }

    fun addAll(expenses: ArrayList<Expense>) {
        for (result in expenses) {
            add(result)
        }
    }

    private fun getItem(position: Int): Expense {
        return mExpenseList!![position]
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

}*/




//CustomAdapterExpense
 class CustomAdapterExpense(private val mExpenseList:ArrayList<Expense> = ArrayList(),
                            private val mOnExpenseOnClickListner: OnExpenseItemClickListener2) : RecyclerView.Adapter<CustomAdapterExpense.MyViewHolder>() {
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
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtDesignationExpense: TextView? = view.findViewById(R.id.txtDesignationExpense)
        var txtQuantityExpense: TextView? = view.findViewById(R.id.txtQteExpense)
        var txtUnitPrice: TextView? = view.findViewById(R.id.txtUnitPriceExpense)
        var txtTypeExpense: TextView? = view.findViewById(R.id.txtTypeExpense)
        var txtDateExpence: TextView? = view.findViewById(R.id.txtDateExpense)
        var txtAmountTotal: TextView? = view.findViewById(R.id.txtAmountTotalExpense)
        var txtAmountFresh: TextView? = view.findViewById(R.id.txtAmountExpenseFresh)
        var txtAmountTotalFresh: TextView? = view.findViewById(R.id.txtTotalExpenseFresh)
        var card_viewItemExpense:CardView? = view.findViewById(R.id.card_viewItemExpense)
        var txtIfExpensePay:TextView = view.findViewById(R.id.txtIfExpensePay)
        //var imageViewIcon: ImageView? = null

       /* init {
            // Define click listener for the ViewHolder's View.
            txtDesignationExpense = view.findViewById(R.id.txtDesignationExpense)
            txtQuantityExpense = view.findViewById(R.id.txtQteExpense)
            txtUnitPrice = view.findViewById(R.id.txtUnitPriceExpense)
            txtTypeExpense = view.findViewById(R.id.txtTypeExpense)
            txtDateExpence = view.findViewById(R.id.txtDateExpense)
            txtAmountTotal = view.findViewById(R.id.txtAmountTotalExpense)
            txtAmountFresh = view.findViewById(R.id.txtAmountExpenseFresh)
            txtAmountTotalFresh = view.findViewById(R.id.txtTotalExpenseFresh)
            position2 = bindingAdapterPosition
            view.setOnClickListener { v: View  ->
                val position: Int = getAdapterPosition()


                Snackbar.make(v, "Click detected on item $position",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show()

                *//*val model = mExpenseList[position]
                mOnExpenseOnClickListner.getExpenseId(position, model)*//*
            }
        }*/

    }

    private class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var progressBar: ProgressBar? = null

        init {
             progressBar = itemView.findViewById(R.id.progressBar)
        }
    }

    /*fun CustomAdapterExpense(data: ArrayList<DataModelExpense>?) {
        dataSet = data!!
    }*/

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_layout_expense, viewGroup, false)
        //view.setOnClickListener(ExpensesActivity.myOnClickListener)
        val holder = MyViewHolder(view)

        return MyViewHolder(view)

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        val expense = mExpenseList[position]
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE)
        val targetFormat = SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH)
        val dateStr:Date = formatter.parse(expense.expense_date)
        val formatedDate = targetFormat.format(dateStr)

        viewHolder.txtDateExpence!!.text = formatedDate
        viewHolder.txtDesignationExpense!!.text = expense.expense_designation
        viewHolder.txtTypeExpense!!.text = expense.expense_type
        //viewHolder.textViewVersion!!.text = dataSet!![position].version

        viewHolder.txtQuantityExpense!!.text = expense.expense_quantity.toString()
        viewHolder.txtUnitPrice!!.text = expense.expense_unit_price.toString()
        //Calculate
        //if is expense categorie hangard
        val totalAmount = if (expense.expense_type!! == viewHolder.itemView.resources.getString(R.string.typeHangard)){
            val totalAmountPayment = expense.paymentExpenseTypes!!.sumOf { it.amountPayAddExpense!! }
            val restAmount = expense.expense_unit_price!!.minus(totalAmountPayment)
            viewHolder.txtIfExpensePay.visibility = View.VISIBLE
            if (restAmount != 0.0){
                //viewHolder.card_viewItemExpense!!.background = ContextCompat.getDrawable(viewHolder.itemView.context, R.drawable.shape_app_bar_notradius)
            }
            else viewHolder.txtIfExpensePay.text = "Payer"
            //expense.expense_unit_price!!.plus(totalAmountPayment)
            expense.expense_unit_price!!
        } else{
            expense.expense_unit_price!!.times(expense.expense_quantity!!)
        }

        val totalAmountFresh = expense.freshExpense!!.sumOf { it.fresh_expense_amount!! }

        viewHolder.txtAmountTotal!!.text = totalAmount.toString()
        viewHolder.txtAmountFresh!!.text = totalAmountFresh.toString()
        viewHolder.txtAmountTotalFresh!!.text = totalAmount.plus(totalAmountFresh).toString()

        //viewHolder.imageViewIcon!!.setImageResource(images[position])

        setAnimation(viewHolder.itemView, position)

        viewHolder.itemView.setOnClickListener(View.OnClickListener {

            val model = mExpenseList[position]
            mOnExpenseOnClickListner.getExpenseId(position, model)
        })
        //FromLeftToRight(viewHolder.itemView, position)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mExpenseList.size
    }

    fun addAll(expenses: ArrayList<Expense>) {
        for (result in expenses) {
            add(result)
        }
    }

    fun add(expense: Expense) {
        mExpenseList.add(expense)
        notifyItemInserted(mExpenseList.size)
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


