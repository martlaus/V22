package ee.v22.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import ee.v22.model.LicenseType;

public class LicenseTypeDAO {

    @Inject
    private EntityManager entityManager;

    public List<LicenseType> findAll() {
        return entityManager.createQuery("from LicenseType", LicenseType.class).getResultList();
    }

}