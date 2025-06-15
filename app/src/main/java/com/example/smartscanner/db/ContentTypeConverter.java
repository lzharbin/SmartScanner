package com.example.smartscanner.db;

import androidx.room.TypeConverter;
import com.example.smartscanner.util.ContentType;

public class ContentTypeConverter {
    @TypeConverter
    public static ContentType toContentType(String value) {
        return value == null ? null : ContentType.valueOf(value);
    }

    @TypeConverter
    public static String fromContentType(ContentType type) {
        return type == null ? null : type.name();
    }
}