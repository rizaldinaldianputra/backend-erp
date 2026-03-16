package com.erp.erp.service;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Central service for generating sequential, human-readable codes for all
 * entity types.
 * Pattern: PREFIX-YYYYMMDD-SEQ (e.g. CUST-20260221-001)
 * For simple master data (no date), pattern: PREFIX-XXXXXX (e.g. WH-000001)
 */
@Service
public class CodeGeneratorService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * Generate a code with date prefix. Format: {prefix}-{date}-{seq:03d}
     * Example: QUO-20260221-001
     */
    public String generateDateCode(String prefix, long seq) {
        String date = LocalDate.now().format(DATE_FMT);
        return String.format("%s-%s-%03d", prefix, date, seq);
    }

    /**
     * Generate a simple sequential code. Format: {prefix}-{seq:06d}
     * Example: CUST-000001
     */
    public String generateSimpleCode(String prefix, long seq) {
        return String.format("%s-%06d", prefix, seq);
    }
}
