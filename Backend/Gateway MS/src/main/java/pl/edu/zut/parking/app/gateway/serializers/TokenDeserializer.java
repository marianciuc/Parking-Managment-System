package pl.edu.zut.parking.app.gateway.serializers;



import pl.edu.zut.parking.app.gateway.dto.Token;

import java.util.function.Function;

public interface TokenDeserializer extends Function<String, Token> {
}
