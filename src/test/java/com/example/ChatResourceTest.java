package com.example;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChatResourceTest {

    private ChatResource subject;

    @Before
    public void setUp() throws Exception {
        subject = new ChatResource();
    }

    @Test
    public void ptaToken() throws Exception {
        final Response response = subject.ptaToken();
        Assert.assertNotNull("expected non-null response", response);
        final Object entity = response.getEntity();
        Assert.assertNotNull("expected non-null response.getEntity()", entity);
        Assert.assertTrue("expected true", entity instanceof Map);
        final Map<String, String> map = (Map)entity;
        Assert.assertTrue("expected map.containsKey(\"data\")", map.containsKey("data"));
        final String cipherText = map.get("data");
        Assert.assertNotNull("expected non-null", cipherText);
        final String plainText = Crypto.decrypt(cipherText, Crypto.PTA_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        Assert.assertNotNull("expected non-null", plainText);
        final String[] split = plainText.split("&");
        Assert.assertNotNull("expected non-null", split);
        Assert.assertEquals("expected match", 5, split.length);
    }
}
