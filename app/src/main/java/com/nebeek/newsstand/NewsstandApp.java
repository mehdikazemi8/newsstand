package com.nebeek.newsstand;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;

public class NewsstandApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("TAG", "hhh onCreate");

        FirebaseApp.initializeApp(this);
    }
}
