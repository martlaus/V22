package ee.v22.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;

import ee.v22.common.test.DatabaseTestBase;
import ee.v22.model.LicenseType;

public class LicenseTypeDAOTest extends DatabaseTestBase {

    @Inject
    private LicenseTypeDAO licenseTypeDAO;

    @Test
    public void findAll() {
        List<LicenseType> licenseTypes = licenseTypeDAO.findAll();

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