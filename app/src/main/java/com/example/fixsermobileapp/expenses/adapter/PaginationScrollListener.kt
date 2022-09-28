package com.example.fixsermobileapp.expenses.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener

abstract class PaginationScrollListener : OnScrollListener() {

    var layoutManager: LinearLayoutManager? = null


    fun PaginationScrollListener(layoutManager: LinearLayoutManager){
        this.layoutManager = layoutManager
    }

    override fun onScrolled(recyclerView:RecyclerView, dx: Int, dy: Int){
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager!!.childCount
        val totalItemCount = layoutManager!!.itemCount
        val firstVisibleItemPosition = layoutManager!!.findFirstVisibleItemPosition()
        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                && firstVisibleItemPosition >= 0) {
                loadMoreItems()
            }
        }
    }



    abstract fun loadMoreItems()

    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean

}