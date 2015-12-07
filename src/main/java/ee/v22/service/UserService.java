package ee.v22.service;

import static java.lang.String.format;

import java.text.Normalizer;

import javax.inject.Inject;

import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ee.v22.dao.UserDAO;
import ee.v22.model.Role;
import ee.v22.model.User;

public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Inject
    private UserDAO userDAO;

    public User getUserByIdCode(String idCode) {
        return userDAO.findUserByIdCode(idCode);
    }

    public User getUserByUsername(String username) {
        return userDAO.findUserByUsername(username);
    }

    public User getUserByGoogleID(String googleID) {
        return userDAO.findUserByGoogleID(googleID);
    }

    public User create(String idCode, String name, String surname) {
        User user = new User();
        user.setIdCode(idCode);
        user.setName(name);
        user.setSurname(surname);

        return create(user);
    }

    public synchronized User create(User user) {
        user.setName(WordUtils.capitalizeFully(user.getName(), ' ', '-'));
        user.setSurname(WordUtils.capitalizeFully(user.getSurname(), ' ', '-'));
        String generatedUsername = generateUsername(user.getName(), user.getSurname());
        user.setUsername(generatedUsername);
        user.setRole(Role.USER);

        logger.info(format("Creating user: username = %s; name = %s; surname = %s; idCode = %s", user.getUsername(),
                user.getName(), user.getSurname(), user.getIdCode()));

        return userDAO.update(user);
    }

    public synchronized User linkGoogleIDToUser(User user, String googleID) {
        unlinkGoogleID(googleID);
        user.setGoogleID(googleID);
        return userDAO.update(user);
    }

    private void unlinkGoogleID(String googleID) {
        userDAO.unlinkGoogleID(googleID);
    }

    protected String generateUsername(String name, String surname) {
        String username = name.trim().toLowerCase() + "." + surname.trim().toLowerCase();
        username = username.replaceAll("\\s+", ".");

        // Normalize the username and remove all non-ascii characters
        username = Normalizer.normalize(username, Normalizer.Form.NFD);
        username = username.replaceAll("[^\\p{ASCII}]", "");

        Long count = userDAO.countUsersWithSameUsername(username);
        if (count > 0) {
            username += String.valueOf(count + 1);
        }

        return username;
    }

}
