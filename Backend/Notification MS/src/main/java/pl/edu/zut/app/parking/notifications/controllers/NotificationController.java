package pl.edu.zut.app.parking.notifications.controllers;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.notifications.dto.NotificationContent;
import pl.edu.zut.app.parking.notifications.dto.NotificationDto;
import pl.edu.zut.app.parking.notifications.services.NotificationService;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping
  public ResponseEntity<Page<NotificationDto>> getNotifications(
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "size", defaultValue = "10") Integer size,
      @RequestParam(name = "userId", required = true) UUID userId) {
    return ResponseEntity.ok(notificationService.getNotifications(userId, 0, 10));
  }

  // check permissions to do this action
  @PutMapping("/{notificationId}")
  public ResponseEntity<NotificationDto> markAsRead(@PathVariable UUID notificationId) {
    return ResponseEntity.ok(notificationService.markAsRead(notificationId));
  }

  // check permissions to do this action
  @DeleteMapping("/{notificationId}")
  public ResponseEntity<Void> deleteNotification(@PathVariable UUID notificationId) {
    notificationService.deleteNotification(notificationId);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{userId}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<Void> sendNotification(
      @PathVariable UUID userId, @RequestBody NotificationContent notificationContent) {
    notificationService.sendPersonalNotification(
        userId, notificationContent.title(), notificationContent.content());
    return ResponseEntity.status(201).build();
  }
}
