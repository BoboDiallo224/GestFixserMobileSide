package com.example.fixsermobileapp.retrofit;

import com.example.fixsermobileapp.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private final static String baseUrl = "http://192.168.133.72:8080/";
    private static Retrofit retrofit = null;
    public static ExpenseService getClient() {

        // change your base URL
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        //Creating object for our interface
        ExpenseService api = retrofit.create(ExpenseService.class);
        return api; // return the APIInterface object
    }
}
