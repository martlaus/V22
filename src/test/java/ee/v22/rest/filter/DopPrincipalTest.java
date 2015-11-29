package ee.v22.rest.filter;

import ee.v22.model.AuthenticatedUser;
import ee.v22.model.Role;
import ee.v22.model.User;
import org.easymock.EasyMockRunner;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(EasyMockRunner.class)
public class DopPrincipalTest {

    AuthenticatedUser authenticatedUser = getUser();

    @TestSubject
    private DopPrincipal v22Principal = new DopPrincipal(authenticatedUser);

    @Test
    public void getName() {
        assertEquals("Mati Maasikas", v22Principal.getName());
    }

    @Test
    public void getToken() {
        assertNull(v22Principal.getSecurityToken());
    }

    @Test
    public void isUserInRole() {
        assertTrue(v22Principal.isUserInRole("USER"));
    }

    private AuthenticatedUser getUser(){
        AuthenticatedUser authenticatedUser = new AuthenticatedUser();
        User user = new User();
        user.setName("Mati");
        user.setSurname("Maasikas");
        user.setRole(Role.USER);
        authenticatedUser.setUser(user);

        return authenticatedUser;
    }
}
