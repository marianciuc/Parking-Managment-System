/*
 * Copyright (c) 2024  Vladimir Marianciuc. All Rights Reserved.
 *
 * Project: STREAMING SERVICE APP
 * File: AuthenticationTokenFactory.java
 *
 */

package pl.edu.zut.app.parking.auth.factories;

import org.springframework.security.core.Authentication;
import pl.edu.zut.app.parking.auth.dto.common.AuthToken;

import java.util.function.Function;

public interface AuthenticationTokenFactory extends Function<Authentication, AuthToken> {
}
