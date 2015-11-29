package ee.v22.service;

import java.util.List;

import javax.inject.Inject;

import ee.v22.dao.LicenseTypeDAO;
import ee.v22.model.LicenseType;

public class LicenseTypeService {

    @Inject
    private LicenseTypeDAO licenseTypeDAO;

    public List<LicenseType> getAllLicenseTypes() {
        return licenseTypeDAO.findAll();
    }

}