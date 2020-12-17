package edu.aku.hassannaqvi.csvdownloader;

import android.app.Application;
import android.content.Context;

public class MainApp extends Application {


    public static Context appContext;

    public static Context getAppContext() {
        return MainApp.appContext;
    }

    public void onCreate() {
        super.onCreate();
        MainApp.appContext = getApplicationContext();
    }
}
