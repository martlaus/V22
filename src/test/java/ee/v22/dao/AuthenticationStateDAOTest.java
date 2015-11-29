package ee.v22.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import javax.inject.Inject;

import ee.v22.model.AuthenticationState;
import org.joda.time.DateTime;
import org.junit.Test;

import ee.v22.common.test.DatabaseTestBase;
import ee.v22.exceptions.DuplicateTokenException;

public class AuthenticationStateDAOTest extends DatabaseTestBase {

    @Inject
    private AuthenticationStateDAO authenticationStateDAO;

    @Test
    public void createAuthenticationState() {
        AuthenticationState authenticationState = getAuthenticationState();

        AuthenticationState returnedAuthenticationState = authenticationStateDAO
                .createAuthenticationState(authenticationState);

        assertEquals(returnedAuthenticationState.getToken(), authenticationState.getToken());

        authenticationStateDAO.delete(returnedAuthenticationState);
    }

    @Test
    public void createAuthenticationStateSameToken() {
        AuthenticationState authenticationState = getAuthenticationState();

        AuthenticationState returnedAuthenticationState = authenticationStateDAO
                .createAuthenticationState(authenticationState);

        AuthenticationState authenticationState2 = new AuthenticationState();
        authenticationState2.setToken("superTOKEN");

        try {
            authenticationStateDAO.createAuthenticationState(authenticationState);
            fail("Exception expected");
        } catch (DuplicateTokenException e) {
            // expected
        }

        authenticationStateDAO.delete(returnedAuthenticationState);
    }

    @Test
    public void findAuthenticationStateByToken() {
        String token = "testTOKEN";

        AuthenticationState authenticationState = authenticationStateDAO.findAuthenticationStateByToken(token);
        assertEquals(token, authenticationState.getToken());
    }

    @Test
    public void delete() {
        AuthenticationState authenticationState = getAuthenticationState();

        AuthenticationState returnedAuthenticationState = authenticationStateDAO
                .createAuthenticationState(authenticationState);

        authenticationStateDAO.delete(returnedAuthenticationState);
        assertNull(authenticationStateDAO.findAuthenticationStateByToken("superTOKEN"));
    }

    private AuthenticationState getAuthenticationState() {
        AuthenticationState authenticationState = new AuthenticationState();
        authenticationState.setCreated(new DateTime());
        authenticationState.setToken("superTOKEN");
        return authenticationState;
    }

}
