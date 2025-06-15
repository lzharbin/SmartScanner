package com.example.smartscanner;

import android.app.Application;

import com.example.smartscanner.db.AppDatabase;

public class ApplicationSetup extends Application {
     // We can also use this class for other app-wide initializations.

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize database to be ready app-wide
        AppDatabase.getDatabase(this);
    }
}