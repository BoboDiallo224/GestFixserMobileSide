package com.example.fixsermobileapp.enter

import com.example.fixsermobileapp.enter.model.ProductAddEnterModel

interface OnProductAddEnterClickListener {
    /**
     * When the user clicks on each row this method will be invoked.
     */
    fun onUpdate(position: Int, model: ProductAddEnterModel)

    /**
     * when the user clicks on delete icon this method will be invoked to remove item at position.
     */
    fun onDelete(model: ProductAddEnterModel)
}