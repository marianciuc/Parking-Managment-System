package pl.edu.zut.app.parking.auth.utils;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import java.security.SecureRandom;
import java.util.Base64;

public class ApiKeyGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String generateKey() {
        byte[] randomBytes = new byte[32];
        SECURE_RANDOM.nextBytes(randomBytes);
        return Base64.getUrlEncoder().encodeToString(randomBytes);
    }

    public static String hashKey(String key, String secret) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secret).hmacHex(key);
    }
}
