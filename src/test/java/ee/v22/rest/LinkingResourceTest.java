package ee.v22.rest;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Ignore;
import org.junit.Test;

import ee.v22.common.test.ResourceIntegrationTestBase;

public class LinkingResourceTest extends ResourceIntegrationTestBase {

    private static final String LINK_GOOGLE_URL = "link/google?token=%s";
    private static final String LINK_FACEBOOK_URL = "link/facebook?code=%s";

    @Test
    public void testGoogleLinkingInvalidToken() {
        login("39011220011");

        String token = "test";
        Response response = doPost(format(LINK_GOOGLE_URL, token), null);

        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Ignore
    @Test
    public void testFacebookLinking() {
        String code = "";

        login("39011220011");

        Response response = doPost(format(LINK_FACEBOOK_URL, code), null);

        assertNotNull(response);
    }

}
