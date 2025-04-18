package org.springframework.samples.gitvision.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Component
@Converter
public class AESConverter implements AttributeConverter<String, String> {
    private static String SECRET_KEY;

    @Value("${aes.secret.key}")
    public void setSecretKey(String secretKey) {
        SECRET_KEY = secretKey;
    }

    private static SecretKeySpec getSecretKeySpec() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, "AES");
    }

    private static IvParameterSpec getIvParameterSpec() {
        try {
            // Derivar IV a partir de la clave secreta usando SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] iv = digest.digest(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            return new IvParameterSpec(iv, 0, 16); // Tomar solo los primeros 16 bytes
        } catch (Exception e) {
            throw new RuntimeException("Error al generar IV", e);
        }
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(), getIvParameterSpec());
            byte[] encryptedBytes = cipher.doFinal(attribute.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(), getIvParameterSpec());
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(dbData));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error al desencriptar", e);
        }
    }
}
