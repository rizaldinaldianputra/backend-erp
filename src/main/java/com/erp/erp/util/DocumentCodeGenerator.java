package com.erp.erp.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DocumentCodeGenerator {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * Generate document code.
     * 
     * Format: PREFIX/YYYYMMDD/TIMESTAMP
     * Example: PURC/20251108/1768026540123
     * 
     * @param prefix Document type prefix, e.g., PURC, PR, PO
     * @return Generated document code
     */
    public static String generateCode(String prefix) {
        String datePart = LocalDate.now().format(DATE_FORMAT);
        long timestamp = System.currentTimeMillis();
        return String.format("%s/%s/%d", prefix, datePart, timestamp);
    }

    // Optional: generate code with custom date (e.g., for backdate or testing)
    public static String generateCode(String prefix, LocalDate date) {
        String datePart = date.format(DATE_FORMAT);
        long timestamp = System.currentTimeMillis();
        return String.format("%s/%s/%d", prefix, datePart, timestamp);
    }

}
