package com.example.smartscanner.viewmodel; // Or your preferred package

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.smartscanner.db.AppDatabase;
import com.example.smartscanner.db.ScanResultDAO;
import com.example.smartscanner.model.ScanResult;
import java.util.List;

public class ScanHistoryViewModel extends AndroidViewModel {

    private ScanResultDAO scanResultDao;
    private LiveData<List<ScanResult>> allScans;

    public ScanHistoryViewModel (Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        scanResultDao = db.scanResultDao();
        allScans = scanResultDao.getAllScans(); // This loads the data initially
    }

    // Method to get all scans, which UI can observe
    public LiveData<List<ScanResult>> getAllScans() {
        return allScans;
    }

    // Method to insert a new scan (you might already do this directly in ScanActivity)
    public void insert(ScanResult scanResult) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            scanResultDao.insert(scanResult);
        });
    }

    // Method to update an existing scan
    public void update(ScanResult scanResult) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            scanResultDao.update(scanResult);
        });
    }

    // Method to delete a scan
    public void delete(ScanResult scanResult) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            scanResultDao.delete(scanResult);
        });
    }

    public void deleteById(int scanId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            scanResultDao.deleteById(scanId);
        });
    }

    public void deleteAllScans() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            scanResultDao.deleteAllScans();
        });
    }
}