package no.hvl.dat250.pollApp.service;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordManager {

    // Generates a random salt as a Base64-encoded string
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // Hashes a password using the provided salt
    public static String hashing(String passord, String salt) {
        if (passord == null || salt == null) {
            throw new IllegalArgumentException("Password and salt must not be null");
        }

        char[] passordSomChar = passord.toCharArray();
        byte[] saltbytes = Base64.getDecoder().decode(salt);

        byte[] keyhash = null;

        try {
            PBEKeySpec pks = new PBEKeySpec(passordSomChar, saltbytes, 1000, 256);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            keyhash = skf.generateSecret(pks).getEncoded();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while hashing password", e);
        }

        return Base64.getEncoder().encodeToString(keyhash);
    }

    // Validates a password by comparing its hash with the stored hash
    public static boolean validateWithSalt(String password, String salt, String passwordHash) {
        if (password == null || salt == null || passwordHash == null) {
            throw new IllegalArgumentException("Password, salt, and hash must not be null");
        }

        return passwordHash.equals(hashing(password, salt));
    }
}
