package ee.v22.service;

import java.util.List;

import javax.inject.Inject;

import ee.v22.dao.ResourceTypeDAO;
import ee.v22.model.ResourceType;

public class ResourceTypeService {

    @Inject
    private ResourceTypeDAO resourceTypeDAO;

    public ResourceType getResourceTypeByName(String name) {
        return resourceTypeDAO.findResourceTypeByName(name);
    }

    public List<ResourceType> getAllResourceTypes() {
        return resourceTypeDAO.findAllResourceTypes();
    }
}
