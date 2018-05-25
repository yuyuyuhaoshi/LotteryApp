package com.yhslib.lottery.Application;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyApplication extends Application {
    private static RequestQueue requestQueue;
    private static Context application;

    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        application=getApplicationContext();
    }

    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public static Context getContext() {
        return  application;
    }

}