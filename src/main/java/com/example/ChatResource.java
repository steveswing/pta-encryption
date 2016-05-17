package com.example;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.example.Crypto.PTA_SECRET_KEY;

@Path("chat")
public class ChatResource {

    private static final Logger log = LoggerFactory.getLogger(ChatResource.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("pta-token")
    public Response ptaToken() {
        final Map<String, String> result = new LinkedHashMap<>();
        final UriBuilder uriBuilder = UriBuilder.fromPath("/");
        uriBuilder.queryParam("p_userid", "userid");
        uriBuilder.queryParam("p_passwd", "passwd");
        uriBuilder.queryParam("p_name.first", "First");
        uriBuilder.queryParam("p_name.last", "Last");
        uriBuilder.queryParam("p_email.addr", "first.last@example.com");
        final String encryptedPtaToken;
        try {
            encryptedPtaToken = Crypto.encrypt(uriBuilder.build().getQuery(), PTA_SECRET_KEY.getBytes("UTF-8"));
            result.put("data", encryptedPtaToken);
            return Response.ok(result).build();
        } catch (BadPaddingException e) {
            log.error("Could not encrypt {}", e, e.getMessage());
            return Response.serverError().entity(errorResult(e.getMessage())).build();
        } catch (IllegalBlockSizeException e) {
            log.error("Could not encrypt {}", e, e.getMessage());
            return Response.serverError().entity(errorResult(e.getMessage())).build();
        } catch (InvalidAlgorithmParameterException e) {
            log.error("Could not encrypt {}", e, e.getMessage());
            return Response.serverError().entity(errorResult(e.getMessage())).build();
        } catch (InvalidKeyException e) {
            log.error("Could not encrypt {}", e, e.getMessage());
            return Response.serverError().entity(errorResult(e.getMessage())).build();
        } catch (NoSuchAlgorithmException e) {
            log.error("Could not encrypt {}", e, e.getMessage());
            return Response.serverError().entity(errorResult(e.getMessage())).build();
        } catch (NoSuchPaddingException e) {
            log.error("Could not encrypt {}", e, e.getMessage());
            return Response.serverError().entity(errorResult(e.getMessage())).build();
        } catch (UnsupportedEncodingException e) {
            log.error("Could not encrypt {}", e, e.getMessage());
            return Response.serverError().entity(errorResult(e.getMessage())).build();
        }
    }

    private Map<String, String> errorResult(final String message) {
        final Map<String, String> result = new LinkedHashMap<>();
        result.put("error", message);
        return result;
    }
}
