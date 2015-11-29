package ee.v22.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import ee.v22.exceptions.DuplicateTokenException;
import ee.v22.model.AuthenticationState;

public class AuthenticationStateDAO {

    @Inject
    private EntityManager entityManager;

    public AuthenticationState createAuthenticationState(AuthenticationState authenticationState)
            throws DuplicateTokenException {

        AuthenticationState merged;
        try {
            merged = entityManager.merge(authenticationState);
            entityManager.persist(merged);
        } catch (PersistenceException e) {
            throw new DuplicateTokenException("Duplicate token found when persisting authentication state.");
        }

        return merged;
    }

    public AuthenticationState findAuthenticationStateByToken(String token) {
        TypedQuery<AuthenticationState> findByToken = entityManager
                .createQuery("SELECT a FROM AuthenticationState a WHERE a.token = :token", AuthenticationState.class);

        AuthenticationState authenticationState = null;
        try {
            authenticationState = findByToken.setParameter("token", token).getSingleResult();
        } catch (Exception e) {
            // ignore
        }

        return authenticationState;
    }

    public void delete(AuthenticationState authenticationState) {
        AuthenticationState merged = entityManager.merge(authenticationState);
        entityManager.remove(merged);
    }

}
