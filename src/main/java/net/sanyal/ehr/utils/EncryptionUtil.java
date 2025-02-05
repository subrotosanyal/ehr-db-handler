package net.sanyal.ehr.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EncryptionUtil {
    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;
    private static SecretKey secretKey;

    static {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(KEY_SIZE);
            secretKey = keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            log.error("Failed to initialize encryption key", e);
            throw new RuntimeException("Encryption initialization failed", e);
        }
    }

    public static byte[] encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(data.getBytes());
        } catch (Exception e) {
            log.error("Encryption failed", e);
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public static String decrypt(byte[] encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(encryptedData);
            return new String(decryptedBytes);
        } catch (Exception e) {
            log.error("Decryption failed", e);
            throw new RuntimeException("Decryption failed", e);
        }
    }

    public static String getKeyAsString() {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public static void setKeyFromString(String keyStr) {
        byte[] decodedKey = Base64.getDecoder().decode(keyStr);
        secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
    }
}
