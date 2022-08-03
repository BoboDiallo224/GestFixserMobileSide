package com.example.fixsermobileapp.expenses.entities

class DataModelExpense() {

    var name: String? = null
    var version: String? = null
    var id_ = 0
    var image = 0

    fun DataModel(name: String?, version: String?, id_: Int, image: Int) {
        this.name = name
        this.version = version
        this.id_ = id_
        this.image = image
    }
}