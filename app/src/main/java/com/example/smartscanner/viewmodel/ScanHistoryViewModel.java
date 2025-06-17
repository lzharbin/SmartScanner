package com.example.smartscanner.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.smartscanner.db.AppDatabase;
import com.example.smartscanner.db.ScanResultDAO;
import com.example.smartscanner.model.ScanResult;

import java.util.List;

public class ScanHistoryViewModel extends AndroidViewModel {

    private final ScanResultDAO scanResultDao;
    private final LiveData<List<ScanResult>> allResults;

    public ScanHistoryViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        scanResultDao = db.scanResultDao();
        allResults = scanResultDao.getAllScans(); // Load results on startup
    }

    public LiveData<List<ScanResult>> getAllResults() {
        return allResults;
    }

    public void insert(ScanResult scanResult) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            scanResultDao.insert(scanResult);
        });
    }

    public void update(ScanResult scanResult) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            scanResultDao.update(scanResult);
        });
    }

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
        AppDatabase.databaseWriteExecutor.execute(scanResultDao::deleteAllScans);
    }

    public LiveData<List<ScanResult>> searchScans(String query) {
        return scanResultDao.searchScans(query);
    }

}
