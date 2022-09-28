package com.example.fixsermobileapp.retrofit

import com.example.fixsermobileapp.expenses.entities.Expense
import com.example.fixsermobileapp.expenses.entities.PaymentExpenseType
import com.example.fixsermobileapp.utils.Constants.BASE_URL
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ExpenseService{

    //@FormUrlEncoded
    @POST("expense/create_expense")
    fun createExpense(@Body expense:Expense):Call<Expense>

    @GET("expense/expenses")
    fun getAllExpenses():Call<ArrayList<Expense>>

    @GET("expense/find_by_category/{category}")
    fun getExpenseByCategory(@Path("category") category:String):Call<ArrayList<Expense>>

    @GET("expense/find_by_id/{id_expense}")
    fun getExpenseById(@Path("id_expense") id_expense:Long):Call<Expense>

    @GET("expense/findcategories")
    fun getExpenseCategories():Call<ArrayList<String>>

    @GET("expense/find_between_dates/{startDate}/{endDate}")
    fun getAllExpenseBetweenDates(@Path("startDate")startDate:String,
                                  @Path("endDate") endDate:String):Call<ArrayList<Expense>>

    @GET("expense/find_by_date/{date}")
    fun getAllExpenseByDate(@Path("date")date:String):Call<ArrayList<Expense>>
    //Expense Payement

    @POST("expense/payment/add/{id_expense}")
    fun addExpensePayment(@Body paymentExpenseType: PaymentExpenseType,
                          @Path("id_expense") id_expense: Long):Call<PaymentExpenseType>

}