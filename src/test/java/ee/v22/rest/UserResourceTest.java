package ee.v22.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import ee.v22.common.test.ResourceIntegrationTestBase;
import ee.v22.model.User;

public class UserResourceTest extends ResourceIntegrationTestBase {

    @Test
    public void get() {
        User user = getUser("mati.maasikas");
        assertEquals(Long.valueOf(1), user.getId());
        assertEquals("mati.maasikas", user.getUsername());
        assertEquals("Mati", user.getName());
        assertEquals("Maasikas", user.getSurname());
        // assertNull(user.getIdCode());

        user = getUser("peeter.paan");
        assertEquals(Long.valueOf(2), user.getId());
        assertEquals("peeter.paan", user.getUsername());
        assertEquals("Peeter", user.getName());
        assertEquals("Paan", user.getSurname());
        // assertNull(user.getIdCode());

        user = getUser("voldemar.vapustav");
        assertEquals(Long.valueOf(3), user.getId());
        assertEquals("voldemar.vapustav", user.getUsername());
        assertEquals("Voldemar", user.getName());
        assertEquals("Vapustav", user.getSurname());
        // assertNull(user.getIdCode());
    }

    @Test
    public void getUserWithoutUsername() {
        Response response = doGet("user/getByUsername");
        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void getUserWithBlankUsername() {
        Response response = doGet("user/getByUsername?username=");
        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void getNotExistingUser() {
        String username = "notexisting.user";
        Response response = doGet("user/getByUsername?username=" + username);
        assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void getSignedUserData() {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        headers.add("Authentication", "token");
        headers.add("Username", "mati.maasikas");

        Response response2 = doGet("user/getSignedUserData", headers, MediaType.TEXT_PLAIN_TYPE);
        assertEquals(Status.OK.getStatusCode(), response2.getStatus());

        String encryptedUserData = response2.readEntity(new GenericType<String>() {
        });
        assertNotNull(encryptedUserData);
    }

    @Test
    public void getSignedUserDataNotLoggedIn() {
        Response response = doGet("user/getSignedUserData", MediaType.TEXT_PLAIN_TYPE);
        assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }

    private User getUser(String username) {
        Response response = doGet("user/getByUsername?username=" + username);
        return response.readEntity(new GenericType<User>() {
        });
    }
}
