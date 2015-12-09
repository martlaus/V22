package ee.v22.rest;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.net.HttpURLConnection;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ee.v22.model.AuthenticatedUser;
import ee.v22.model.User;
import ee.v22.service.AuthenticatedUserService;
import ee.v22.service.UserService;

@Path("user")
public class UserResource extends BaseResource {

    @Inject
    private UserService userService;

    @Inject
    private AuthenticatedUserService authenticatedUserService;

    @GET
    @Path("/getByUsername")
    @Produces(MediaType.APPLICATION_JSON)
    public User getByUsername(@QueryParam("username") String username) {
        if (isBlank(username)) {
            throwBadRequestException("Username parameter is mandatory.");
        }

        return userService.getUserByUsername(username);
    }

    @GET
    @Path("/getByGoogleEmail")
    @Produces(MediaType.APPLICATION_JSON)
    public User getByGoogleEmail(@QueryParam("googleEmail") String googleEmail) {
        if (isBlank(googleEmail)) {
            throwBadRequestException("Google email parameter is mandatory.");
        }

        return userService.getUserByGoogleEmail(googleEmail);
    }

    @GET
    @Path("/getByFacebookID")
    @Produces(MediaType.APPLICATION_JSON)
    public User getByFacebookID(@QueryParam("facebookID") String facebookID) {
        if (isBlank(facebookID)) {
            throwBadRequestException("Facebook ID parameter is mandatory.");
        }

        return userService.getUserByFacebookID(facebookID);
    }

    @GET
    @Path("getSignedUserData")
    @RolesAllowed("USER")
    @Produces(MediaType.TEXT_PLAIN)
    public String getSignedUserData() {
        AuthenticatedUser authenticatedUser = getAuthenticatedUser();

        return authenticatedUserService.signUserData(authenticatedUser);
    }

    private void throwBadRequestException(String message) {
        throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(message).build());
    }
}
