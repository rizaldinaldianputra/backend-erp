// src/main/java/com/erp/erp/dto/TaskResponseDto.java
package com.erp.erp.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {
        private String taskId;
        private String name;
        private String assignee;
        private String processInstanceId;
        private String documentNumber;
        private String created;
}
