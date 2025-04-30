package pl.edu.zut.app.parking.owners.kafka.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

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
