package ee.v22.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ee.v22.model.AuthenticatedUser;
import ee.v22.service.LoginService;

@Path("dev/")
public class DevelopmentLoginResource {

    @Inject
    private LoginService loginService;

    @GET
    @Path("/login/{idCode}")
    @Produces(MediaType.APPLICATION_JSON)
    public AuthenticatedUser logIn(@PathParam("idCode") String idCode) {
        return loginService.logIn(idCode, null, null);
    }
}
