package ee.v22.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.GenericType;

import org.junit.Test;

import ee.v22.common.test.ResourceIntegrationTestBase;
import ee.v22.model.Language;
import ee.v22.model.LicenseType;
import ee.v22.model.ResourceType;
import ee.v22.model.TargetGroup;
import ee.v22.model.taxon.Domain;
import ee.v22.model.taxon.EducationalContext;
import ee.v22.model.taxon.Subject;
import ee.v22.model.taxon.Taxon;
import ee.v22.model.taxon.Topic;

public class LearningMaterialMetadataResourceTest extends ResourceIntegrationTestBase {

    private static final String GET_EDUCATIONAL_CONTEXT_URL = "learningMaterialMetadata/educationalContext";
    private static final String GET_TAXON_URL = "learningMaterialMetadata/taxon?taxonId=%s";
    private static final String GET_LANGUAGES_URL = "learningMaterialMetadata/language";
    private static final String GET_TARGET_GROUPS_URL = "learningMaterialMetadata/targetGroup";
    private static final String GET_RESOURCE_TYPES_URL = "learningMaterialMetadata/resourceType";
    private static final String GET_LICENSE_TYPES_URL = "learningMaterialMetadata/licenseType";

    @Test
    public void getEducationalContext() {
        List<EducationalContext> educationalContexts = doGet(GET_EDUCATIONAL_CONTEXT_URL,
                new GenericType<List<EducationalContext>>() {
                });

        assertEquals(9, educationalContexts.stream().distinct().count());

        int domains = 0, subjects = 0;

        for (EducationalContext educationalContext : educationalContexts) {
            if (educationalContext.getName().equals("PRESCHOOLEDUCATION")) {
                for (Domain domain : educationalContext.getDomains()) {
                    domains++;
                    if (domain.getName().equals("Mathematics")) {
                        for (Subject subject : domain.getSubjects()) {
                            subjects++;
                            if (subject.getName().equals("Mathematics")) {
                                assertEquals(2, subject.getTopics().size());
                                Topic[] topics = new Topic[2];
                                subject.getTopics().toArray(topics);
                                assertTrue(topics[0].getName().equals("Algebra")
                                        || topics[0].getName().equals("Trigonometria"));
                                assertTrue(topics[1].getName().equals("Algebra")
                                        || topics[1].getName().equals("Trigonometria"));
                            }
                        }
                    }
                }
            }
        }

        assertEquals(2, domains);
        assertEquals(2, subjects);
    }

    @Test
    public void getTaxon() {
        Long id = 10L;
        Domain taxon = (Domain) doGet(String.format(GET_TAXON_URL, id), Taxon.class);
        assertEquals(id, taxon.getId());
        assertNotNull(taxon.getEducationalContext());
    }

    @Test
    public void getAllLanguages() {
        List<Language> languages = doGet(GET_LANGUAGES_URL, new GenericType<List<Language>>() {
        });

        assertEquals(6, languages.stream().distinct().count());

        List<String> expectedNames = Arrays.asList("Estonian", "Russian", "English", "Arabic", "Portuguese", "French");
        List<String> actualNames = languages.stream().map(l -> l.getName()).collect(Collectors.toList());
        assertTrue(actualNames.containsAll(expectedNames));
    }

    @Test
    public void getTargetGroups() {
        TargetGroup[] result = doGet(GET_TARGET_GROUPS_URL, new GenericType<TargetGroup[]>() {
        });

        assertEquals(11, result.length);

        List<TargetGroup> expectedTargetGroups = Arrays.asList(TargetGroup.values());
        List<TargetGroup> actualTargetGroups = Arrays.asList(result);

        assertTrue(actualTargetGroups.containsAll(expectedTargetGroups));
    }

    @Test
    public void getResourceTypesGroups() {
        List<ResourceType> result = doGet(GET_RESOURCE_TYPES_URL, new GenericType<List<ResourceType>>() {
        });

        assertEquals(7, result.size());

        List<String> expected = Arrays.asList("TEXTBOOK1", "EXPERIMENT1", "COURSE");
        List<String> actual = result.stream().map(r -> r.getName()).collect(Collectors.toList());

        assertTrue(actual.containsAll(expected));
    }

    @Test
    public void getAllLicenseTypes() {
        List<LicenseType> licenseTypes = doGet(GET_LICENSE_TYPES_URL, new GenericType<List<LicenseType>>() {
        });

        assertEquals(3, licenseTypes.size());
        for (int i = 0; i < licenseTypes.size(); i++) {
            assertValidLicenseType(licenseTypes.get(i));
        }
    }

    private void assertValidLicenseType(LicenseType licenseType) {
        Map<Long, String> licenseTypes = new HashMap<>();
        licenseTypes.put(1L, "CCBY");
        licenseTypes.put(2L, "CCBYSA");
        licenseTypes.put(3L, "CCBYND");

        assertNotNull(licenseType.getId());
        assertNotNull(licenseType.getName());
        if (licenseTypes.containsKey(licenseType.getId())) {
            assertEquals(licenseTypes.get(licenseType.getId()), licenseType.getName());
        } else {
            fail("LicenseType with unexpected id.");
        }
    }

}
