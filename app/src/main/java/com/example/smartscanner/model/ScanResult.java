package com.example.smartscanner.model;

import com.example.smartscanner.util.ContentType;
import java.util.Date;

public class ScanResult {

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

    // Getters and setters
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
