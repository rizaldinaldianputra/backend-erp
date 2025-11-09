// src/main/java/com/erp/erp/dto/AssignRequestDto.java
package com.erp.erp.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignRequestDto {
        private String taskId;
        private String module; // "PR"
        private String assigneeUsername;
}
