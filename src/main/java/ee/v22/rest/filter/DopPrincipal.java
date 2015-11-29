package ee.v22.rest.filter;

import java.security.Principal;

import ee.v22.model.AuthenticatedUser;
import ee.v22.model.User;

public class DopPrincipal implements Principal {

    private AuthenticatedUser authenticatedUser;

    public DopPrincipal(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    @Override
    public String getName() {
        return getUser().getName() + " " + getUser().getSurname();
    }

    public User getUser() {
        return authenticatedUser.getUser();
    }

    public String getSecurityToken() {
        return authenticatedUser.getToken();
    }

    public boolean isUserInRole(String role) {
        return authenticatedUser != null && authenticatedUser.getUser().getRole().toString().equals(role);
    }

    public AuthenticatedUser getAuthenticatedUser() {
        return authenticatedUser;
    }
}
