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
import android.util.Log


//CustomAdapterExpense
class CustomAdapterExpense : RecyclerView.Adapter<CustomAdapterExpense.MyViewHolder>() {
    var DURATION: Long = 500
    private var on_attach = true
    private val titles = arrayOf("Chapter One",
        "Chapter Two", "Chapter Three", "Chapter Four",
        "Chapter Five", "Chapter Six", "Chapter Seven",
        "Chapter Eight")

    private val details = arrayOf("Item one details", "Item two details",
        "Item three details", "Item four details",
        "Item file details", "Item six details",
        "Item seven details", "Item eight details")

    private val images = intArrayOf(R.drawable.ic_baseline_search,
                                    R.drawable.ic_hangard,
                                    R.drawable.ic_entrer,
                                    R.drawable.ic_depense_foreground,
                                    R.drawable.ic_baseline_search,
                                    R.drawable.ic_depence,
                                    R.drawable.ic_menu_slideshow,
                                    R.drawable.ic_menu_gallery)
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textViewName: TextView? = null
        var textViewVersion: TextView? = null
        var imageViewIcon: ImageView? = null

        init {
            // Define click listener for the ViewHolder's View.
            //textViewName = view.findViewById(R.id.textViewName)
            textViewName = view.findViewById(R.id.textViewName)
            textViewVersion = view.findViewById(R.id.textViewVersion)
            imageViewIcon = view.findViewById(R.id.imageView)

            view.setOnClickListener { v: View  ->
                val position: Int = getAdapterPosition()

                Snackbar.make(v, "Click detected on item $position",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show()
            }
        }

    }

    /*fun CustomAdapterExpense(data: ArrayList<DataModelExpense>?) {
        dataSet = data!!
    }*/

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

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textViewName!!.text = titles[position]
        //viewHolder.textViewVersion!!.text = dataSet!![position].version
        viewHolder.textViewVersion!!.text = details[position]
        viewHolder.imageViewIcon!!.setImageResource(images[position])
        setAnimation(viewHolder.itemView, position)
        //FromLeftToRight(viewHolder.itemView, position)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return titles.size
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