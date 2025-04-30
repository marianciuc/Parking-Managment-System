package pl.edu.zut.app.parking.notifications.subscribers;

import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPubSub;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class NotificationSubscriber extends JedisPubSub {

  private final Map<String, Session> activeSessions;

  @Override
  public void onMessage(String channel, String message) {
    if ("notifications".equals(channel)) {
      log.info("Received message from Redis: {}", message);

      String[] parts = message.split(":", 2);
      if (parts.length == 2) {
        String userId = parts[0];
        String notification = parts[1];

        Session session = activeSessions.get(userId);
        if (session != null && session.isOpen()) {
          try {
            session.getAsyncRemote().sendText(notification);
            log.info("Message sent to userId = {}", userId);
          } catch (Exception e) {
            log.error("Failed to send message to userId = {}, removing session", userId, e);
              try {
                  activeSessions.remove(userId).close();
              } catch (IOException ex) {
                  throw new RuntimeException(ex);
              }
          }
        } else {
          log.info("No active WebSocket session for userId = {}", userId);
        }
      } else {
        log.error("Invalid message format: {}", message);
      }
    }
  }
}
