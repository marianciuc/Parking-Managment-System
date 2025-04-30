package pl.edu.zut.app.parking.auth.serializers;


import pl.edu.zut.app.parking.auth.dto.common.AuthToken;

import java.util.function.Function;

public interface TokenDeserializer extends Function<String, AuthToken> {
}
