package pl.edu.zut.app.parking.admin_ms.kafka.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreatedUserMessage {
    private UUID userId;
    private String email;

    @JsonCreator
    public CreatedUserMessage(@JsonProperty("userId") UUID userId, @JsonProperty("email") String email) {
        this.userId = userId;
        this.email = email;
    }

    @Override
    public String toString() {
        return "CreatedUserMessage{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                '}';
    }
}
