package pl.edu.zut.app.parking.auth.utils;

import java.security.SecureRandom;

public class ResetPasswordCodeGenerator {

    private static final SecureRandom random = new SecureRandom();

    /**
     * Generates a random 5-digit verification code.
     * The code is generated using a secure random number generator to ensure uniqueness and unpredictability.
     *
     * @return A string representing a 5-digit verification code.
     */
    public static String generateVerificationCode() {
        int code = random.nextInt(90000) + 10000;
        return String.valueOf(code);
    }
}
