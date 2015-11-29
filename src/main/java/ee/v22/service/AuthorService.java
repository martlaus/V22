package ee.v22.service;

import ee.v22.dao.AuthorDAO;
import ee.v22.model.Author;

import javax.inject.Inject;

/**
 * Created by mart on 28.10.15.
 */
public class AuthorService {

    @Inject
    private AuthorDAO authorDAO;

    public Author getAuthorByFullName(String name, String surname) {
        return authorDAO.findAuthorByFullName(name, surname);
    }

    public Author createAuthor(String name, String surname) {
        Author author = new Author();
        author.setName(name);
        author.setSurname(surname);
        return authorDAO.create(author);
    }
}
