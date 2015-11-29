package ee.v22.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import ee.v22.common.test.DatabaseTestBase;
import ee.v22.model.ResourceType;
import org.junit.Test;

public class ResourceTypeDAOTest extends DatabaseTestBase {

    @Inject
    private ResourceTypeDAO resourceTypeDAO;

    @Test
    public void findResourceTypeByName() {
        Long id = new Long(1001);
        String name = "TEXTBOOK1";

        ResourceType returnedResourceType = resourceTypeDAO.findResourceTypeByName(name);

        assertNotNull(returnedResourceType);
        assertNotNull(returnedResourceType.getId());
        assertEquals(id, returnedResourceType.getId());
        assertEquals(name, returnedResourceType.getName());
    }

}
