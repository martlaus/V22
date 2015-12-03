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

import ee.v22.service.GoogleService;

@Path("link")
@RolesAllowed("USER")
public class LinkingResource extends BaseResource {

    @Inject
    private GoogleService googleService;

    @POST
    @Path("/google")
    @Produces(MediaType.APPLICATION_JSON)
    public Response linkGoogle(@QueryParam("token") String token) throws Exception {
        String googleID = googleService.getUserID(token);
        if (googleID != null) {
            getAuthenticatedUser().getUser().setGoogleID(googleID);
            return Response.ok().build();
        } else {
            return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).build();
        }
    }

}