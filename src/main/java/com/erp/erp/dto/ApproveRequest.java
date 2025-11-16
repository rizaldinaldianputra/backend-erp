// src/main/java/com/erp/erp/dto/ApproveRequestDto.java
package com.erp.erp.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApproveRequest {
        private String taskId;
        private String module; // "PR"
        private String result; // "APPROVE" or "REJECT"
}
