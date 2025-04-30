package pl.edu.zut.app.parking.notifications.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.notifications.services.ExpoPushNotificationService;
import pl.edu.zut.app.parking.utils.SecurityContextUtil;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications/expo")
@RequiredArgsConstructor
public class ExpoPushNotificationController {

    private final ExpoPushNotificationService expoPushNotificationService;

    @PostMapping
    public ResponseEntity<Void> registerDevice(@RequestParam String token) {
        UUID userId = SecurityContextUtil.extractUserIdFromSecurityContext();
        expoPushNotificationService.addDeviceToken(userId, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeDevice(@RequestParam String token) {
        UUID userId = SecurityContextUtil.extractUserIdFromSecurityContext();
        expoPushNotificationService.removeDeviceToken(userId, token);
        return ResponseEntity.ok().build();
    }
}
