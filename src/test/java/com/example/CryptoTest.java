package com.example;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.Assert;
import org.junit.Test;

public class CryptoTest {
    @Test
    public void roundTrip()
            throws UnsupportedEncodingException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException,
            NoSuchPaddingException, InvalidAlgorithmParameterException {

        final String expectedPlainText = String.format("p_userid=%s&p_passwd=%s&p_name.first=%s&p_name.last=%s&p_email.addr=%s", "user", "password",
                "First", "Last", "first.last@example.com");

        final byte[] password = Crypto.PTA_SECRET_KEY.getBytes("UTF-8"); // This is a 256-bit password matches RightNow PTA_SECRET_KEY

        final String cipherText = Crypto.encrypt(expectedPlainText, password);
        final String actualDecryptedText = Crypto.decrypt(cipherText, password);

        Assert.assertEquals("expected match", expectedPlainText, actualDecryptedText);
    }
}
