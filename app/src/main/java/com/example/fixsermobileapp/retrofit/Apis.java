package com.example.fixsermobileapp.retrofit;

import com.example.fixsermobileapp.utils.Constants;

public class Apis {

    public static ExpenseService getExpenseService(){
        return NetWorkClient.Companion.getClient(Constants.BASE_URL).create(ExpenseService.class);
    }
}
