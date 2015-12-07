package ee.v22.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import ee.v22.model.User;

public class UserDAO {

    @Inject
    private EntityManager entityManager;

    public User findUserByIdCode(String idCode) {
        TypedQuery<User> findByIdCode = entityManager.createQuery("SELECT u FROM User u WHERE u.idCode = :idCode",
                User.class);

        User user = null;
        try {
            user = findByIdCode.setParameter("idCode", idCode).getSingleResult();
        } catch (Exception e) {
            // ignore
        }

        return user;
    }

    public User findUserByUsername(String username) {
        TypedQuery<User> findByUsername = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username",
                User.class);

        User user = null;
        try {
            user = findByUsername.setParameter("username", username).getSingleResult();
        } catch (Exception e) {
            // ignore
        }

        return user;
    }

    public User findUserByGoogleID(String googleID) {
        TypedQuery<User> findByGoogleID = entityManager.createQuery("SELECT u FROM User u WHERE u.googleID = :googleID",
                User.class);

        User user = null;
        try {
            user = findByGoogleID.setParameter("googleID", googleID).getSingleResult();
        } catch (Exception e) {
            // ignore
        }

        return user;
    }

    /*
     * Remove this googleID from whoever has it
     */
    public void unlinkGoogleID(String googleID) {
        User previouslyLinkedUser = findUserByGoogleID(googleID);
        if (previouslyLinkedUser != null) {
            previouslyLinkedUser.setGoogleID(null);
            update(previouslyLinkedUser);
        }

        // If we don't flush, we might get an unique constraint violation when
        // someone tries to use the same googleID on a different user in the
        // same transaction.
        entityManager.flush();
    }

    /**
     * Counts the amount of users who have the same username, excluding the
     * number at the end. For example, users <i>john.smith</i> and
     * <i>john.smith2</i> are considered to have the same username.
     * 
     * @param username
     *            the username to search for
     * @return the count of users with the same username, excluding the number
     */
    public Long countUsersWithSameUsername(String username) {
        TypedQuery<User> findByUsername = entityManager
                .createQuery("SELECT u FROM User u WHERE u.username LIKE :username", User.class);

        List<User> users = findByUsername.setParameter("username", username + "%").getResultList();

        return users.stream() //
                .filter(user -> user.getUsername().matches(username + "\\d*")) //
                .count();
    }

    public User update(User user) {
        User merged;
        merged = entityManager.merge(user);
        entityManager.persist(merged);
        return merged;
    }

    public void delete(User user) {
        entityManager.remove(user);
    }

}
