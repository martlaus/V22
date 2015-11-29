package ee.v22.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import ee.v22.dao.AuthenticatedUserDAO;
import ee.v22.model.AuthenticatedUser;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EasyMockRunner.class)
public class LogoutServiceTest {

    @TestSubject
    private LogoutService logoutService = new LogoutService();

    @Mock
    private AuthenticatedUserDAO authenticatedUserDAO;

    @Test
    public void logout(){
        AuthenticatedUser authenticatedUser = createMock(AuthenticatedUser.class);

        authenticatedUserDAO.delete(authenticatedUser);
        expectLastCall();

        replay(authenticatedUserDAO);

        logoutService.logout(authenticatedUser);

        verify(authenticatedUserDAO);
    }

    @Test
    public void logoutNoUser(){
        replay(authenticatedUserDAO);

        logoutService.logout(null);

        verify(authenticatedUserDAO);
    }
}
