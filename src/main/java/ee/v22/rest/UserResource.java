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
    @Produces(MediaType.APPLICATION_JSON)
    public User get(@QueryParam("username") String username) {
        if (isBlank(username)) {
            throwBadRequestException("Username parameter is mandatory.");
        }

        User user = userService.getUserByUsername(username);
        User newUser = null;

        if (user != null) {
            // Return only some fields
            newUser = new User();
            newUser.setId(user.getId());
            newUser.setUsername(user.getUsername());
            newUser.setName(user.getName());
            newUser.setSurname(user.getSurname());
        }

        return newUser;
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
