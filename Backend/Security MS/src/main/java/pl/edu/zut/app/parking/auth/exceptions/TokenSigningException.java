/*
 * Copyright (c) 2024  Vladimir Marianciuc. All Rights Reserved.
 *
 * Project: STREAMING SERVICE APP
 * File: TokenSigningException.java
 *
 */

package pl.edu.zut.app.parking.auth.exceptions;

public class TokenSigningException extends SecurityServiceException {
    public TokenSigningException(String message, Throwable cause) {
        super(message, cause);
    }
}