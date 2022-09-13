package com.example.fixsermobileapp.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class NetWorkClient {
    //private val baseUrl:String = "http://192.168.133.72:8080/"

     companion object{
         lateinit var retrofit:Retrofit
         fun getClient(url:String):Retrofit{
             val okHttpClient:OkHttpClient = OkHttpClient.Builder().build()
             retrofit = Retrofit.Builder().baseUrl(url).
             addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()
             return retrofit
         }
     }

    /*
    * Api.getClient().createExpense(expense).
                enqueue(object  : Callback<Expense> {
                    override fun onResponse(call: Call<Expense>?, response: Response<Expense>?) {
                        if (response!!.isSuccessful){
                            Toast.makeText(this@AddExpenseActivity,"Inserted",Toast.LENGTH_SHORT).show()
                            //Reset values
                            edtUnitPriceExpense.setText(""); edtDesignationExpense.setText(""); edtQuantityExpense.setText("")
                            expenseFreshAdapter.freshExpenseList.clear(); txtAddExpenseFresh.text = "Ajout Frais"

                            progressDialog.hide()
                        }
                        else{
                            progressDialog.hide()
                            Toast.makeText(this@AddExpenseActivity,"500",Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<Expense>?, t: Throwable?) {
                        progressDialog.hide()
                        Toast.makeText(this@AddExpenseActivity,"faild"+t,Toast.LENGTH_LONG).show()
                    }
                })

        /*val apiInterface = ApiInterface.create().createExpense(expense)
        apiInterface.enqueue(object  : Callback<Expense> {
            override fun onResponse(call: Call<Expense>?, response: Response<Expense>?) {
                    Toast.makeText(this@AddExpenseActivity,"Inserted",Toast.LENGTH_SHORT).show()
                    //progressDialog.hide()

            }

            override fun onFailure(call: Call<Expense>?, t: Throwable?) {
                Toast.makeText(this@AddExpenseActivity,"faild",Toast.LENGTH_SHORT).show()

            }
        })*/*/
}