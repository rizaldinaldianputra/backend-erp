package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.model.Notification;
import com.erp.erp.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notification", description = "User Notifications API")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<ApiResponseDto<List<Notification>>> getUnreadNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getUnreadNotificationsForUser(userId);
        return ResponseEntity.ok(ApiResponseDto.<List<Notification>>builder()
                .status("success")
                .message("Retrieved unread notifications successfully")
                .data(notifications)
                .build());
    }

    @PostMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponseDto<Void>> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(ApiResponseDto.<Void>builder()
                .status("success")
                .message("Notification marked as read")
                .build());
    }
}
