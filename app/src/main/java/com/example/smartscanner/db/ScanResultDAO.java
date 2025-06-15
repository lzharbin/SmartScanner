package com.example.smartscanner.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;

import com.example.smartscanner.model.ScanResult;

import java.util.List;

@Dao
public interface ScanResultDAO {

    @Query("SELECT * FROM scan_results ORDER BY timestamp DESC")
    LiveData<List<ScanResult>> getAllScans(); // Observe changes with LiveData

    @Query("SELECT * FROM scan_results WHERE id = :id")
    LiveData<ScanResult> getScanById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ScanResult scanResult); // For inserting a single scan

    @Update
    void update(ScanResult scanResult); // For updating a single scan

    @Delete
    void delete(ScanResult scanResult); // For deleting a single scan

    @Query("DELETE FROM scan_results WHERE id = :scanId")
    void deleteById(int scanId); // Method to delete by ID

    @Query("DELETE FROM scan_results")
    void deleteAllScans();
}
