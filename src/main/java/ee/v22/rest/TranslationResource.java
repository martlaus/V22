package ee.v22.rest;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import ee.v22.service.TranslationService;

@Path("translation")
public class TranslationResource {

    @Inject
    private TranslationService translationService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> getTranslation(@QueryParam("lang") String language) {
        return translationService.getTranslationsFor(language);
    }
}
