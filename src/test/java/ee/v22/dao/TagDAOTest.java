package ee.v22.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import ee.v22.common.test.DatabaseTestBase;
import ee.v22.model.Tag;
import org.junit.Test;

/**
 * Created by mart.laus on 24.07.2015.
 */
public class TagDAOTest extends DatabaseTestBase {

    @Inject
    private TagDAO tagDAO;

    @Test
    public void findTagByName() {
        Long id = new Long(1);
        String name = "matemaatika";

        Tag returnedTag = tagDAO.findTagByName(name);

        assertNotNull(returnedTag);
        assertNotNull(returnedTag.getId());
        assertEquals(id, returnedTag.getId());
        assertEquals(name, returnedTag.getName());
    }
}
