package com.example.smartscanner.util;

import android.util.Patterns;

public class ContentUtil {

    public static ContentType detectContentType(String content) {
        if (content == null || content.trim().isEmpty()) {
            return ContentType.UNKNOWN;
        }

        content = content.trim();

        if (Patterns.WEB_URL.matcher(content).matches()) {
            return ContentType.URL;
        }

        if (content.startsWith("BEGIN:VCARD") || content.contains("TEL:") || content.contains("EMAIL:")) {
            return ContentType.CONTACT;
        }

        if (content.matches("^\\d{8,14}$")) {
            return ContentType.PRODUCT;
        }

        return ContentType.TEXT;
    }
}
