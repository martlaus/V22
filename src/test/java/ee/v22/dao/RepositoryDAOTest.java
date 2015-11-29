package ee.v22.dao;

import ee.v22.common.test.DatabaseTestBase;
import ee.v22.model.Repository;
import org.joda.time.DateTime;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class RepositoryDAOTest extends DatabaseTestBase {

    @Inject
    private RepositoryDAO repositoryDAO;

    @Test
    public void findAll() {
        List<Repository> repositories = repositoryDAO.findAll();

        assertEquals(2, repositories.size());
        assertEquals("http://repo1.ee", repositories.get(0).getBaseURL());
        assertNull(repositories.get(0).getLastSynchronization());
    }

    @Test
    public void updateRepositoryData() {
        Repository repository = repositoryDAO.findAll().get(0);
        assertNull(repository.getLastSynchronization());

        repository.setLastSynchronization(new DateTime());
        repositoryDAO.updateRepository(repository);

        Repository repository2 = repositoryDAO.findAll().get(0);
        assertNotNull(repository2.getLastSynchronization());

        repository.setLastSynchronization(null);
        repositoryDAO.updateRepository(repository);
    }
}
