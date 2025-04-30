package pl.edu.zut.app.parking.notifications.services.impl;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.notifications.services.WebSocketService;
import pl.edu.zut.app.parking.notifications.subscribers.NotificationSubscriber;
import redis.clients.jedis.Jedis;

@Slf4j
@Service
@ServerEndpoint("/ws/notifications")
public class WebSocketServiceImpl implements WebSocketService {

  private static final Map<String, Session> activeSessions = new ConcurrentHashMap<>();

  private static final String REDIS_CHANNEL = "notifications";
  private final Jedis redisClient;
  private final Thread subscriberThread;

  public WebSocketServiceImpl(RedisProperties redisProperties) {
    this.redisClient = new Jedis(redisProperties.getHost(), redisProperties.getPort());

    subscriberThread =
        new Thread(
            () -> {
              Jedis subscriberJedis =
                  new Jedis(redisProperties.getHost(), redisProperties.getPort());
              subscriberJedis.subscribe(new NotificationSubscriber(activeSessions), REDIS_CHANNEL);
            });
    subscriberThread.start();
  }

  @Override
  public boolean isUserConnected(UUID userId) {
    Session session = activeSessions.get(userId.toString());
    return session != null && session.isOpen();
  }

  @Override
  public void send(UUID userId, String message) {
    String notification = userId + ":" + message;
    redisClient.publish(REDIS_CHANNEL, notification);
    log.info("Published message to Redis: {}", notification);
  }

  @Override
  @OnError
  public void onError(Session session, Throwable throwable) {
    UUID userId = getUserIdFromQuery(session);
    if (userId != null) {
      log.error("WebSocket error for user {}: {}", userId, throwable.getMessage());
    }
  }

  @Override
  @OnClose
  public void onClose(Session session) {
    UUID userId = getUserIdFromQuery(session);
    if (userId != null) {
      activeSessions.remove(userId);
      log.info("User {} disconnected from WebSocket", userId);
    }
  }

  @Override
  @OnOpen
  public void onOpen(Session session) {
    UUID userId = getUserIdFromQuery(session);
    if (userId != null) {
      activeSessions.put(userId.toString(), session);
      log.info("User {} connected to WebSocket", userId);
    } else {
      log.error("Failed to extract user ID from WebSocket session");
      try {
        session.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Extracts the user ID from the query string of the given session.
   *
   * @param session the WebSocket session containing the query string
   * @return the extracted user ID as a UUID, or null if the query string does not contain a valid
   *     user ID
   */
  private UUID getUserIdFromQuery(Session session) {
    String query = session.getQueryString();
    if (query != null && query.contains("userId=")) {
      String[] params = query.split("&");
      for (String param : params) {
        if (param.startsWith("userId=")) {
          try {
            return UUID.fromString(param.replace("userId=", ""));
          } catch (IllegalArgumentException e) {
            log.error("Failed to parse userId from query string", e);
            return null;
          }
        }
      }
    }
    return null;
  }

  @PreDestroy
  public void cleanUp() {
    log.info("Closing Redis subscriber...");
    subscriberThread.interrupt();
    redisClient.close();
  }
}
