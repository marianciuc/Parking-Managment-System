/*
 * Copyright (c) 2024  Vladimir Marianciuc. All Rights Reserved.
 *
 * Project: STREAMING SERVICE APP
 * File: TokenFactory.java
 *
 */

package pl.edu.zut.app.parking.auth.factories;


import pl.edu.zut.app.parking.auth.dto.common.AuthToken;

import java.util.function.Function;

public interface TokenFactory extends Function<AuthToken, AuthToken> {
}
