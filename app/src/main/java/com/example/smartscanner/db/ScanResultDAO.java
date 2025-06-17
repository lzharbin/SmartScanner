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
    LiveData<List<ScanResult>> getAllScans();

    @Query("SELECT * FROM scan_results WHERE id = :id")
    LiveData<ScanResult> getScanById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ScanResult scanResult);

    @Update
    void update(ScanResult scanResult);

    @Delete
    void delete(ScanResult scanResult);

    @Query("DELETE FROM scan_results WHERE id = :scanId")
    void deleteById(int scanId);

    @Query("DELETE FROM scan_results")
    void deleteAllScans();

    // 🔍 New: Filter scans by title, tag, or content (for search functionality)
    //@Query("SELECT * FROM scan_results WHERE title LIKE '%' || :query || '%' OR tag LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    //LiveData<List<ScanResult>> searchScans(String query);

    @Query("SELECT * FROM scan_results WHERE title LIKE '%' || :query || '%'")
    LiveData<List<ScanResult>> searchScans(String query);

}
