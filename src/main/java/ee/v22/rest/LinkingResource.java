package ee.v22.rest;

import java.net.HttpURLConnection;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ee.v22.model.User;
import ee.v22.service.FacebookService;
import ee.v22.service.GoogleService;
import ee.v22.service.UserService;

@Path("link")
@RolesAllowed("USER")
public class LinkingResource extends BaseResource {

    @Inject
    private GoogleService googleService;

    @Inject
    private FacebookService facebookService;

    @Inject
    private UserService userService;

    @POST
    @Path("/google")
    @Produces(MediaType.APPLICATION_JSON)
    public Response linkGoogle(@QueryParam("token") String token) throws Exception {
        String googleID = googleService.getUserID(token);

        if (googleID != null) {
            User user = getAuthenticatedUser().getUser();
            userService.linkGoogleIDToUser(user, googleID);

            return Response.ok(getAuthenticatedUser()).build();
        } else {
            return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).build();
        }
    }

    @POST
    @Path("/facebook")
    @Produces(MediaType.APPLICATION_JSON)
    public Response linkFacebook(@QueryParam("token") String token) throws Exception {
        String facebookID = facebookService.getUserID(token);

        if (facebookID != null) {
            User user = getAuthenticatedUser().getUser();
            userService.linkFacebookIDToUser(user, facebookID);

            return Response.ok(getAuthenticatedUser()).build();
        } else {
            return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).build();
        }
    }

}
