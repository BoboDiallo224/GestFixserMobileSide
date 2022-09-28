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

}