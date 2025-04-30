/*
 * Copyright (c) 2024  Vladimir Marianciuc. All Rights Reserved.
 *
 * Project: STREAMING SERVICE APP
 * File: TokenEncryptionException.java
 *
 */

package pl.edu.zut.app.parking.auth.exceptions;

public class TokenEncryptionException extends JWTException {
    public TokenEncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}