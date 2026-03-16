package com.erp.erp.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private Long supervisorId; // cuma ID

    private String avatarUrl;
    private String role;

    private Long organizationId;
    private String organizationName;

    private boolean active;
}
