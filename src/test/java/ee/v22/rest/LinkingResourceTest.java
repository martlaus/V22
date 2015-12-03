package ee.v22.rest;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import ee.v22.common.test.ResourceIntegrationTestBase;

public class LinkingResourceTest extends ResourceIntegrationTestBase {

    private static final String LINK_GOOGLE_URL = "link/google?token=%s";

    @Test
    public void testGoogleLinkingInvalidToken() {
        login("39011220011");

        String token = "test";
        Response response = doPost(format(LINK_GOOGLE_URL, token), null);

        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

}
