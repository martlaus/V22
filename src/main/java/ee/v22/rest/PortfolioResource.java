package ee.v22.rest;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ee.v22.model.Portfolio;
import ee.v22.model.User;
import ee.v22.model.taxon.Taxon;
import ee.v22.service.PortfolioService;
import ee.v22.service.TaxonService;
import ee.v22.service.UserService;

@Path("portfolio")
public class PortfolioResource extends BaseResource {

    @Inject
    private PortfolioService portfolioService;

    @Inject
    private UserService userService;

    @Inject
    private TaxonService taxonService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Portfolio get(@QueryParam("id") long portfolioId) {
        return portfolioService.get(portfolioId);
    }

    @GET
    @Path("getByCreator")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Portfolio> getByCreator(@QueryParam("username") String username) {
        if (isBlank(username)) {
            throwBadRequestException("Username parameter is mandatory");
        }

        User creator = userService.getUserByUsername(username);
        if (creator == null) {
            throwBadRequestException("Invalid request");
        }

        return portfolioService.getByCreator(creator);
    }

    @GET
    @Path("/getPicture")
    @Produces("image/png")
    public Response getPictureById(@QueryParam("portfolioId") long id) {
        Portfolio portfolio = new Portfolio();
        portfolio.setId(id);
        byte[] pictureData = portfolioService.getPortfolioPicture(portfolio);

        if (pictureData != null) {
            return Response.ok(pictureData).build();
        } else {
            return Response.status(HttpURLConnection.HTTP_NOT_FOUND).build();
        }
    }

    private void throwBadRequestException(String message) {
        throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(message).build());
    }

    @POST
    @Path("increaseViewCount")
    public void increaseViewCount(Portfolio portfolio) {
        portfolioService.incrementViewCount(portfolio);
    }

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Portfolio create(CreatePortfolioForm createPortfolioForm) {
        Portfolio portfolio = createPortfolioForm.getPortfolio();

        if (createPortfolioForm.getTaxon() != null) {
            Taxon taxon = taxonService.getTaxonById(createPortfolioForm.getTaxon());

            if (taxon == null) {
                throw new BadRequestException("Taxon does not exist.");
            }

            portfolio.setTaxon(taxon);
        }

        return portfolioService.create(portfolio, getLoggedInUser());
    }

    /**
     * This is an workaround to bypass JSOG/Jackson problem:
     * https://github.com/jsog/jsog-jackson/pull/8
     */
    public static class CreatePortfolioForm {

        private Long taxon;
        private Portfolio portfolio;

        public Long getTaxon() {
            return taxon;
        }

        public void setTaxon(Long taxon) {
            this.taxon = taxon;
        }

        public Portfolio getPortfolio() {
            return portfolio;
        }

        public void setPortfolio(Portfolio portfolio) {
            this.portfolio = portfolio;
        }
    }
}
