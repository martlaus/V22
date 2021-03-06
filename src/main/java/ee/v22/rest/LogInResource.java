package ee.v22.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.soap.SOAPException;

import ee.v22.model.AuthenticatedUser;
import ee.v22.model.mobileid.MobileIDSecurityCodes;
import ee.v22.service.AuthenticatedUserService;
import ee.v22.service.LoginService;

@Path("login")
public class LogInResource extends BaseResource {

    @Inject
    private LoginService loginService;

    @Inject
    private AuthenticatedUserService authenticatedUserService;

    @GET
    @Path("/idCard")
    @Produces(MediaType.APPLICATION_JSON)
    public AuthenticatedUser idCardLogin() {
        AuthenticatedUser authenticatedUser = null;

        if (isAuthValid()) {
            authenticatedUser = loginService.logIn(getIdCodeFromRequest(), getNameFromRequest(),
                    getSurnameFromRequest());
        }

        return authenticatedUser;
    }

    @GET
    @Path("/getAuthenticatedUser")
    @Produces(MediaType.APPLICATION_JSON)
    public AuthenticatedUser getAuthenticatedUser(@QueryParam("token") String token) {
        return authenticatedUserService.getAuthenticatedUserByToken(token);
    }

    @GET
    @Path("/mobileId")
    @Produces(MediaType.APPLICATION_JSON)
    public MobileIDSecurityCodes mobileIDAuthenticate(@QueryParam("phoneNumber") String phoneNumber,
            @QueryParam("idCode") String idCode) throws Exception {
        return loginService.mobileIDAuthenticate(phoneNumber, idCode);
    }

    @GET
    @Path("/mobileId/isValid")
    @Produces(MediaType.APPLICATION_JSON)
    public AuthenticatedUser validateMobileIDAuthentication(@QueryParam("token") String token) throws SOAPException {
        return loginService.validateMobileIDAuthentication(token);
    }

    @GET
    @Path("/google")
    @Produces(MediaType.APPLICATION_JSON)
    public AuthenticatedUser googleLogin(@QueryParam("token") String token) {
        return loginService.googleAuthenticate(token);
    }

    @GET
    @Path("/facebook")
    @Produces(MediaType.APPLICATION_JSON)
    public AuthenticatedUser facebookLogin(@QueryParam("token") String token) {
        return loginService.facebookAuthenticate(token);
    }

    protected String getIdCodeFromRequest() {
        String[] values = getRequest().getHeader("SSL_CLIENT_S_DN").split(",");
        return values[0].split("=")[1];
    }

    protected String getNameFromRequest() {
        String[] values = getRequest().getHeader("SSL_CLIENT_S_DN").split(",");
        return values[1].split("=")[1];
    }

    protected String getSurnameFromRequest() {
        String[] values = getRequest().getHeader("SSL_CLIENT_S_DN").split(",");
        return values[2].split("=")[1];
    }

    private boolean isAuthValid() {
        return "SUCCESS".equals(getRequest().getHeader("SSL_AUTH_VERIFY"));
    }

}
