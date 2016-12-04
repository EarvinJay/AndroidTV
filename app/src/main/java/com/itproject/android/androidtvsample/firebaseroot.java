package com.itproject.android.androidtvsample;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by srthg on 11/24/2016.
 */

public class firebaseroot extends Application {

    public void onCreate()
    {
        super.onCreate();
        Firebase.setAndroidContext(this);

    }

}
