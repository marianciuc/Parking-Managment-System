package pl.edu.zut.app.parking.notifications.services;

import java.util.UUID;
import jakarta.websocket.Session;

public interface WebSocketService {
    boolean isUserConnected(UUID userId);
    void send(UUID userId, String message);
    void onError(Session session, Throwable throwable);
    void onClose(Session session);
    void onOpen(Session session);
}
