package org.springframework.samples.gitvision.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import io.github.cdimascio.dotenv.Dotenv;

@Component
public class AESUtil {

    private static final String ALGORITHM = "AES";
    private static final String ENCODING = "UTF-8";


    // Método para generar una clave secreta de 128 bits
    public static SecretKey generateSecretKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(128);  // Tamaño de la clave (128 bits)
        return keyGenerator.generateKey();
    }

    // Método para cifrar texto usando una clave secreta
    public static String encrypt(String plainText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(ENCODING));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String encrypt(String plainText, String keyString) throws Exception {
        SecretKey secretKey = getSecretKeyFromString(keyString);
        return encrypt(plainText, secretKey);
    }

    public static String encrypt_github(String plainText) throws Exception {
        Dotenv dotenv = Dotenv.configure().load();
        return encrypt(plainText, dotenv.get("ENCRYPTION_SECRET_GITHUB"));
    }

    public static String encrypt_clockify(String plainText) throws Exception {
        Dotenv dotenv = Dotenv.configure().load();
        return encrypt(plainText, dotenv.get("ENCRYPTION_SECRET_CLOCKIFY"));
    }

    // Método para descifrar texto usando una clave secreta
    public static String decrypt(String encryptedText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, ENCODING);
    }

    public static String decrypt(String encryptedText, String keyString) throws Exception {
        SecretKey secretKey = getSecretKeyFromString(keyString);
        return decrypt(encryptedText, secretKey);
    }

    public static String decrypt_github(String encryptedText) throws Exception {
        Dotenv dotenv = Dotenv.configure().load();
        return decrypt(encryptedText, dotenv.get("ENCRYPTION_SECRET_GITHUB"));
    }

    public static String decrypt_clockify(String encryptedText) throws Exception {
        Dotenv dotenv = Dotenv.configure().load();
        return decrypt(encryptedText, dotenv.get("ENCRYPTION_SECRET_CLOCKIFY"));
    }

    // Convierte una cadena de texto a una clave secreta (se utiliza cuando se guarda una clave en la base de datos)
    public static SecretKey getSecretKeyFromString(String keyString) {
        return new SecretKeySpec(keyString.getBytes(), ALGORITHM);
    }
}
