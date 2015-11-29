package ee.v22.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import ee.v22.dao.MaterialDAO;
import ee.v22.model.Material;
import ee.v22.model.Repository;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EasyMockRunner.class)
public class MaterialServiceTest {

    @TestSubject
    private MaterialService materialService = new MaterialService();

    @Mock
    private MaterialDAO materialDao;

    @Test
    public void createMaterialWithNotNullId() {
        Material material = new Material();
        material.setId(123L);

        replay(materialDao);

        try {
            materialService.createMaterial(material);
            fail("Exception expected.");
        } catch (IllegalArgumentException e) {
            assertEquals("Error creating Material, material already exists.", e.getMessage());
        }

        verify(materialDao);
    }

    @Test
    public void update() {
        DateTime added = new DateTime("2001-10-04T10:15:45.937");
        Long views = 124l;

        Material original = new Material();
        original.setViews(views);
        original.setAdded(added);

        long materialId = 1;
        Material material = createMock(Material.class);
        expect(material.getId()).andReturn(materialId).times(2);
        expect(material.getRepository()).andReturn(null).times(2);
        material.setAdded(added);
        material.setViews(views);

        expect(materialDao.findById(materialId)).andReturn(original);
        expect(materialDao.update(material)).andReturn(material);

        replay(materialDao, material);

        materialService.update(material);

        verify(materialDao, material);
    }

    @Test
    public void updateWhenMaterialDoesNotExist() {
        long materialId = 1;
        Material material = createMock(Material.class);
        expect(material.getId()).andReturn(materialId);
        expect(materialDao.findById(materialId)).andReturn(null);

        replay(materialDao, material);

        try {
            materialService.update(material);
            fail("Exception expected.");
        } catch (IllegalArgumentException ex) {
            assertEquals("Error updating Material: material does not exist.", ex.getMessage());
        }

        verify(materialDao, material);
    }

    @Test
    public void updateAddingRepository() {
        Material original = new Material();

        long materialId = 1;
        Material material = createMock(Material.class);
        expect(material.getId()).andReturn(materialId);
        expect(material.getRepository()).andReturn(new Repository()).times(3);

        expect(materialDao.findById(materialId)).andReturn(original);

        replay(materialDao, material);

        try {
            materialService.update(material);
            fail("Exception expected.");
        } catch (IllegalArgumentException ex) {
            assertEquals("Error updating Material: Not allowed to modify repository.", ex.getMessage());
        }

        verify(materialDao, material);
    }

    @Test
    public void updateChangingRepository() {
        Material original = new Material();
        Repository originalRepository = new Repository();
        originalRepository.setBaseURL("original.com");
        original.setRepository(originalRepository);

        long materialId = 1;
        Material material = createMock(Material.class);
        expect(material.getId()).andReturn(materialId);
        Repository newRepository = new Repository();
        newRepository.setBaseURL("some.com");
        expect(material.getRepository()).andReturn(newRepository).times(3);

        expect(materialDao.findById(materialId)).andReturn(original);

        replay(materialDao, material);

        try {
            materialService.update(material);
            fail("Exception expected.");
        } catch (IllegalArgumentException ex) {
            assertEquals("Error updating Material: Not allowed to modify repository.", ex.getMessage());
        }

        verify(materialDao, material);
    }

    @Test
    public void delete() {
        Material material = createMock(Material.class);
        materialDao.delete(material);

        replay(materialDao, material);

        materialService.delete(material);

        verify(materialDao, material);
    }
}
