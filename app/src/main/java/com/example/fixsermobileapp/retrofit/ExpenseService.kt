package com.example.fixsermobileapp.retrofit

import com.example.fixsermobileapp.expenses.entities.Expense
import com.example.fixsermobileapp.utils.Constants.BASE_URL
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ExpenseService{

    //@FormUrlEncoded
    @POST("expense/create_expense")
    fun createExpense(@Body expense:Expense):Call<Expense>

    @GET("expense/expenses")
    fun getAllExpenses():Call<ArrayList<Expense>>

   /* companion object {

        //var BASE_URL = "http://192.168.133.72:8080/"

        fun create() : ExpenseService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ExpenseService::class.java)

        }
    }*/
}