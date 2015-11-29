package ee.v22.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import ee.v22.model.Language;
import ee.v22.model.Page;

public class PageDAO {

    @Inject
    private EntityManager entityManager;

    public Page findByNameAndLanguage(String name, Language language) {

        TypedQuery<Page> findByNameAndLanguage = entityManager.createQuery(
                "SELECT p FROM Page p WHERE p.name = :name AND p.language = :language", Page.class);

        Page page = null;
        try {
            page = findByNameAndLanguage.setParameter("name", name).setParameter("language", language)
                    .getSingleResult();
        } catch (NoResultException ex) {
            // ignore
        }

        return page;

    }

}
