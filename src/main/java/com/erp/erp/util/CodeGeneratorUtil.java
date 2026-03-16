package com.erp.erp.util;

import com.erp.erp.model.User;
import com.erp.erp.repository.UserRepository;
import com.erp.erp.security.SecurityUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CodeGeneratorUtil {

    private final UserRepository userRepository;

    public CodeGeneratorUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Generate document code in the format: PREFIX-YYYYMMDD-USERID-ORGID-TIMESTAMP
     * NOTE: Currently hardcoding ORGID to 1 until multi-tenant org feature is
     * implemented
     * Added timestamp to ensure uniqueness
     *
     * @param prefix Document Prefix (e.g. "PO", "INV", "SO")
     * @return Generated Code String
     */
    public String generateCode(String prefix) {
        String username = SecurityUtil.getCurrentUsername();
        Long userId = 0L;
        Long orgId = 1L; // HARDCODED for now

        if (username != null) {
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                userId = user.getId();
            }
        }

        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // Add timestamp milliseconds to ensure uniqueness
        long timestamp = System.currentTimeMillis() % 1000000; // Last 6 digits of timestamp

        return String.format("%s-%s-%d-%d-%06d", prefix, dateStr, userId, orgId, timestamp);
    }
}
