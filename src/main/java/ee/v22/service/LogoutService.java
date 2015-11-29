package ee.v22.service;

import javax.inject.Inject;

import ee.v22.dao.AuthenticatedUserDAO;
import ee.v22.model.AuthenticatedUser;

public class LogoutService {

    @Inject
    private AuthenticatedUserDAO authenticatedUserDAO;

    public void logout(AuthenticatedUser authenticatedUser) {
        if(authenticatedUser != null) {
            authenticatedUserDAO.delete(authenticatedUser);
        }
    }
}
