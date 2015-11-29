package ee.v22.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import ee.v22.model.Language;
import ee.v22.model.LicenseType;
import ee.v22.model.ResourceType;
import ee.v22.model.TargetGroup;
import ee.v22.model.taxon.EducationalContext;
import ee.v22.model.taxon.Taxon;
import ee.v22.service.LanguageService;
import ee.v22.service.LicenseTypeService;
import ee.v22.service.ResourceTypeService;
import ee.v22.service.TaxonService;

@Path("learningMaterialMetadata")
public class LearningMaterialMetadataResource {

    @Inject
    private TaxonService taxonService;

    @Inject
    private LanguageService languageService;

    @Inject
    private ResourceTypeService resourceTypeService;

    @Inject
    private LicenseTypeService licenseTypeService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("educationalContext")
    public List<EducationalContext> getEducationalContext() {
        return taxonService.getAllEducationalContext();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("taxon")
    public Taxon getTaxon(@QueryParam("taxonId") Long taxonId) {
        return taxonService.getTaxonById(taxonId);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("language")
    public List<Language> getAllLanguages() {
        return languageService.getAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("targetGroup")
    public TargetGroup[] getTargetGroups() {
        return TargetGroup.values();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("resourceType")
    public List<ResourceType> getAllResourceTypes() {
        return resourceTypeService.getAllResourceTypes();
    }

    @GET
    @Path("licenseType")
    @Produces(MediaType.APPLICATION_JSON)
    public List<LicenseType> getAllLicenseTypes() {
        return licenseTypeService.getAllLicenseTypes();
    }

}
