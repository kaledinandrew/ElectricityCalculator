package com.example.andrey.electricitycalculator;


import android.app.Application;

import com.orm.SugarContext;
import com.orm.SugarRecord;

public class SugarConfig extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
