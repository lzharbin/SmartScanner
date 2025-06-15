package com.example.smartscanner.model;

import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.example.smartscanner.db.ContentTypeConverter;
import com.example.smartscanner.util.ContentType;
import com.example.smartscanner.db.DateConverter;

import java.util.Date;

import androidx.room.PrimaryKey;

@Entity(tableName = "scan_results")
@TypeConverters({DateConverter.class, ContentTypeConverter.class})
public class ScanResult {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String content;          // The decoded content (e.g., URL, text)
    private ContentType type;        // Detected type (URL, CONTACT, PRODUCT, etc.)
    private Date timestamp;          // When the scan happened
    private String thumbnailPath;    // Optional image thumbnail path
    private String title;            // User-entered title or tag

    // Constructor
    public ScanResult(String content, ContentType type, Date timestamp, String thumbnailPath, String title) {
        this.content = content;
        this.type = type;
        this.timestamp = timestamp;
        this.thumbnailPath = thumbnailPath;
        this.title = title;
    }

    // Getters
    public int getId() { return id; }

    public String getContent() {
        return content;
    }

    public ContentType getType() {
        return type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public String getTitle() {
        return title;
    }

    // Setters
    public void setId(int id) { this.id = id; }


    public void setContent(String content) {
        this.content = content;
    }

    public void setType(ContentType type) {
        this.type = type;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
