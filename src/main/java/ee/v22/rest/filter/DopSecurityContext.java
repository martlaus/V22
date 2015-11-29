package ee.v22.rest.filter;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

public class DopSecurityContext implements SecurityContext {

    private UriInfo uriInfo;
    private DopPrincipal v22Principal;

    public DopSecurityContext(DopPrincipal principal, UriInfo uriInfo) {
        this.uriInfo = uriInfo;
        this.v22Principal = principal;
    }

    @Override
    public Principal getUserPrincipal() {
        return v22Principal;
    }

    @Override
    public boolean isUserInRole(String role) {
        return v22Principal != null && v22Principal.isUserInRole(role);
    }

    @Override
    public boolean isSecure() {
        return "https".equals(uriInfo.getRequestUri().getScheme());
    }

    @Override
    public String getAuthenticationScheme() {
        return SecurityContext.CLIENT_CERT_AUTH;
    }
}
