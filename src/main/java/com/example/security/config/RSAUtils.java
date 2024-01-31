package com.example.security.config;

import com.example.security.entity.TransactionHistory;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class RSAUtils {

    private static Map<String, Object> keyPairMap = new HashMap<>();
    private final String encryptionCipher = "RSA";
    private Cipher cipher;

    /**
     * Generates RSA key pair if not already present.
     */
    @SneakyThrows
    private void generateKeyPair() {
        if (keyPairMap.isEmpty()) {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(encryptionCipher);
            keyPairGenerator.initialize(4096);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            keyPairMap.put("publicKey", keyPair.getPublic());
            keyPairMap.put("privateKey", keyPair.getPrivate());
        }
    }

    /**
     * Initializes and returns the Cipher instance for encryption or decryption.
     */
    @SneakyThrows
    private Cipher getCipher(int encryptMode) {
        generateKeyPair();
        cipher = Cipher.getInstance(encryptionCipher);
        Key key = (encryptMode == Cipher.ENCRYPT_MODE) ? (PublicKey) keyPairMap.get("publicKey") : (PrivateKey) keyPairMap.get("privateKey");
        cipher.init(encryptMode, key);
        return cipher;
    }


    public String encrypt(Object data) {
        try {
            getCipher(Cipher.ENCRYPT_MODE);
            byte[] encryptedBytes = SerializationUtils.serialize(data);
            assert encryptedBytes != null;
            return Base64.getEncoder().encodeToString(cipher.doFinal(encryptedBytes));
        } catch (Exception ex) {
            throw new RuntimeException("Error encoding data with RSA", ex);
        }
    }

    public Object decrypt(String encryptedData) {
        try {
            getCipher(Cipher.DECRYPT_MODE);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return SerializationUtils.deserialize(decryptedBytes);
        } catch (Exception ex) {
            throw new RuntimeException("Error decoding data with RSA", ex);
        }
    }

}
