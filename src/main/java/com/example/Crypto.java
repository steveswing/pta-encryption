package com.example;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*
    Current RightNow Settings
    PTA_ENABLED: Yes
    PTA_ENCRYPTION_KEYGEN: 3
    PTA_ENCRYPTION_METHOD: aes128
    PTA_ENCRYPTION_PADDING: 1
    PTA_ERROR_URL: http://localhost:8080/error_code/%error_code%/error/%error%
    PTA_EXTERNAL_LOGIN_URL:
    PTA_EXTERNAL_LOGOUT_SCRIPT_URL:
    PTA_EXTERNAL_POST_LOGOUT_URL:
    PTA_IGNORE_CONTACT_PASSWORD: Yes
    PTA_SECRET_KEY: jm2OqbUO5iQSt/mcR/3MPwSYwg4ERB2g
    PTA_ENCRYPTION_IV:
    PTA_ENCRYPTION_SALT:
*/

public class Crypto {
    public static final String PTA_SECRET_KEY = "jm2OqbUO5iQSt/mcR/3MPwSYwg4ERB2g"; // Matches RightNow configuration to work with RSSL_KEYGEN_NONE
    private static final Logger log = LoggerFactory.getLogger(Crypto.class);
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    public static String encrypt(final String plainText, final byte[] password)
            throws BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, UnsupportedEncodingException {
        final SecretKeySpec secret = generateSecretKey(password);
        assert null != password && 32 == password.length; // requires 256-bit key to be compatible with PTA_ENCRYPTION_KEYGEN = 3 - RSSL_KEYGEN_NONE

/*
        PTA_ENCRYPTION_PADDING = 1 - RSSL_PAD_PKCS7
        This link https://en.wikipedia.org/wiki/Padding_(cryptography)#PKCS7 says:
        PKCS#5 padding is identical to PKCS#7 padding, except that it has only been defined for block ciphers that use a 64-bit (8 byte) block
        size. In practice the two can be used interchangeably.

        PTA_ENCRYPTION_METHOD = aes128

        According to this link: https://paragonie.com/blog/2015/05/if-you-re-typing-word-mcrypt-into-your-code-you-re-doing-it-wrong

        MCRYPT_RIJNDAEL_256 doesn't mean AES-256.

        All variants of AES use a 128-bit block size with varying key lengths (128, 192, or 256). This means that MCRYPT_RIJNDAEL_128 is the only
        correct choice if you want AES.

        MCRYPT_RIJNDAEL_192 and MCRYPT_RIJNDAEL_256 instead refer to non-standard, less-studied variants of the Rijndael block cipher that operate on
        larger blocks.
*/

        final Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(
                new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0})); // PTA_ENCRYPTION_IV is - Blank : Empty IV (all zeros)
        final byte[] cipherText = cipher.doFinal(plainText.getBytes("UTF-8"));

        // PTA_ENCRYPTION_SALT - Blank : No salt.
        final String result = encodeSpecialChars(DatatypeConverter.printBase64Binary(cipherText));
        log.debug("cipherText[{}]: {}", result.length(), result);
        return result;
    }

    public static String decrypt(final String cipherText, final byte[] password)
            throws BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, UnsupportedEncodingException {
        final SecretKey secretKey = generateSecretKey(password);
        final Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
        return new String(cipher.doFinal(DatatypeConverter.parseBase64Binary(decodeSpecialChars(cipherText))), "UTF-8");
    }

    private static SecretKeySpec generateSecretKey(final byte[] password) {
        return new SecretKeySpec(password, "AES");
    }

    private static String encodeSpecialChars(final String s) {
        return StringUtils.replaceChars(s, "+/=", "_~*");
    }

    private static String decodeSpecialChars(final String s) {
        return StringUtils.replaceChars(s, "_~*", "+/=");
    }
}
