package ee.v22.service;

import static java.lang.String.format;

import java.util.List;

import javax.inject.Inject;

import ee.v22.dao.MaterialDAO;
import ee.v22.model.Material;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ee.v22.model.User;

public class MaterialService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    private MaterialDAO materialDao;

    public Material get(long materialId) {
        return materialDao.findById(materialId);
    }

    public List<Material> getNewestMaterials(int numberOfMaterials) {
        return materialDao.findNewestMaterials(numberOfMaterials);
    }

    public void increaseViewCount(Material material) {
        material.setViews(material.getViews() + 1);
        createOrUpdate(material);
    }

    public void createMaterial(Material material) {
        if (material.getId() != null) {
            throw new IllegalArgumentException("Error creating Material, material already exists.");
        }

        createOrUpdate(material);
    }

    public void update(Material material) {
        Material originalMaterial = materialDao.findById(material.getId());
        validateMaterialUpdate(material, originalMaterial);

        // Should not be able to update view count
        material.setViews(originalMaterial.getViews());
        // Should not be able to update added date, must keep the original
        material.setAdded(originalMaterial.getAdded());

        createOrUpdate(material);
    }

    private void validateMaterialUpdate(Material material, Material originalMaterial) {
        if (originalMaterial == null) {
            throw new IllegalArgumentException("Error updating Material: material does not exist.");
        }

        final String ErrorModifyRepository = "Error updating Material: Not allowed to modify repository.";
        if (material.getRepository() == null && originalMaterial.getRepository() != null) {
            throw new IllegalArgumentException(ErrorModifyRepository);
        }

        if (material.getRepository() != null && !material.getRepository().equals(originalMaterial.getRepository())) {
            throw new IllegalArgumentException(ErrorModifyRepository);
        }
    }

    public byte[] getMaterialPicture(Material material) {
        return materialDao.findPictureByMaterial(material);
    }

    public List<Material> getByCreator(User creator) {
        return materialDao.findByCreator(creator);
    }

    private void createOrUpdate(Material material) {
        Long materialId = material.getId();
        if (materialId != null) {
            logger.info(format("Updating material %s", materialId));
        } else {
            logger.info("Creating material.");
        }

        materialDao.update(material);
    }

    public void delete(Material material) {
        materialDao.delete(material);
    }
}
