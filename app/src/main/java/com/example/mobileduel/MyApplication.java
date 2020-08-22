package com.example.mobileduel;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MySharedPreferences.initHelper(this);
        adapter_records.initHelper(this, MySharedPreferences.getInstance().getPlayers());
    }
}
