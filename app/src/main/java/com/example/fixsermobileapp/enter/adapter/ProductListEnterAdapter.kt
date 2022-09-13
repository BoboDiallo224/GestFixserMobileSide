package com.example.fixsermobileapp.enter.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fixsermobileapp.R
import com.example.fixsermobileapp.enter.AddEnterActivity
import com.example.fixsermobileapp.enter.OnProductAddEnterClickListener
import com.example.fixsermobileapp.enter.model.ProductAddEnterModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProductListEnterAdapter (
    private val mContext: Context,
    private val mOnProductClickListener: OnProductAddEnterClickListener,
    public val mProductList: ArrayList<ProductAddEnterModel> = ArrayList()
        ): RecyclerView.Adapter<ProductListEnterAdapter.ProductViewHolder>() {

    /**
     * ViewHolder implementation for holding the mapped views.
     */
    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.txt_design_addprod_enter)
        val quantity:TextView = itemView.findViewById(R.id.txt_qte_addprod_enter)
        val productPrice: TextView = itemView.findViewById(R.id.txt_pu_addprod_enter)
        val montantTotal:TextView = itemView.findViewById(R.id.txt_total_addprod_enter)
        val ibtn_removeItem:ImageButton = itemView.findViewById(R.id.ibtn_delet_itemprod_enter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_selectproduct_add_enter, parent, false)
        val holder = ProductViewHolder(view)


        // item view is the root view for each row
        holder.itemView.setOnClickListener {

            // adapterPosition give the actual position of the item in the RecyclerView
            val position = holder.adapterPosition
            val model = mProductList[position]

            // remove the Rs. prefix before sending the model for editing
            //model.puAddProdEnter = model.puAddProdEnter.substringAfterLast(" ")
            model.puListProdEnter = model.puListProdEnter
            mOnProductClickListener.onUpdate(position, model)

           /* val dialog = BottomSheetDialog(parent.context)
            val viewDailog = inflater.inflate(R.layout.bottom_sheet_dialog_addlistprod_enter,null)
            dialog.setContentView(viewDailog)
            dialog.show()*/

        }

        // to delete the item in recycler view
        holder.ibtn_removeItem.setOnClickListener {
            val position = holder.adapterPosition
            val model = mProductList[position]
            mOnProductClickListener.onDelete(model)
        }

        return holder

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        // get the product at position
        val product = mProductList[position]

        holder.productName.text = product.designationListProdEnter
        //holder.productPrice.text = "GNF. ${product.puAddProdEnter}"
        holder.productPrice.text = product.puListProdEnter.toString()
        holder.quantity.text = product.qteListProdEnter.toString()
        holder.montantTotal.text = product.montTotListProdEnter.toString()
    }

    override fun getItemCount(): Int {
        return mProductList.size
    }

    fun addProduct(model: ProductAddEnterModel) {
        println(model)
        mProductList.add(model)
        // notifyDataSetChanged() // this method is costly I avoid it whenever possible
        notifyItemInserted(mProductList.size)
    }

    fun updateProduct(model: ProductAddEnterModel?) {

        if (model == null) return // we cannot update the value because it is null

        for (item in mProductList) {
            // search by id
            if (item.id == model.id) {
                val position = mProductList.indexOf(model)
                mProductList[position] = model
                notifyItemChanged(position)
                break // we don't need to continue any more iterations
            }
        }
    }

    fun removeProduct(model: ProductAddEnterModel) {
        val position = mProductList.indexOf(model)
        mProductList.remove(model)
        notifyItemRemoved(position)
    }

    fun getNextItemId(): Int {
        var id = 1
        if (mProductList.isNotEmpty()) {
            // .last is equivalent to .size() - 1
            // we want to add 1 to that id and return it
            id = (mProductList.last().id)!!.toInt() + 1
        }
        return id
    }

}