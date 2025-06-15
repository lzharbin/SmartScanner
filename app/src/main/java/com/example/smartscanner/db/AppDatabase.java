package com.example.smartscanner.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.smartscanner.model.ScanResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ScanResult.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class, ContentTypeConverter.class}) // Also add converters here
public abstract class AppDatabase extends RoomDatabase {

    public abstract ScanResultDAO scanResultDao();

    private static volatile AppDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "smart_scanner_database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // .fallbackToDestructiveMigration() // Use with caution for production
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}