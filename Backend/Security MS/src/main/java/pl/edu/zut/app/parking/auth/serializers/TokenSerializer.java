package pl.edu.zut.app.parking.auth.serializers;

import pl.edu.zut.app.parking.auth.dto.common.AuthToken;

import java.util.function.Function;

public interface TokenSerializer  extends Function<AuthToken, String> {
}
