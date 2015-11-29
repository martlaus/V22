package ee.v22.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.TypedQuery;

import ee.v22.model.taxon.EducationalContext;
import ee.v22.model.taxon.Taxon;

public class TaxonDAO extends BaseDAO {

    public Taxon findTaxonById(Long id) {
        TypedQuery<Taxon> findById = createQuery("FROM Taxon t WHERE t.id = :id", Taxon.class) //
                .setParameter("id", id);

        return getSingleResult(findById);
    }

    public EducationalContext findEducationalContextByName(String name) {
        TypedQuery<Taxon> findByName = createQuery(
                "FROM Taxon t WHERE t.name = :name and level = 'EDUCATIONAL_CONTEXT'", Taxon.class) //
                .setParameter("name", name);

        return (EducationalContext) getSingleResult(findByName);
    }

    public List<EducationalContext> findAllEducationalContext() {
        List<Taxon> resultList = createQuery("FROM Taxon t WHERE level = 'EDUCATIONAL_CONTEXT'", Taxon.class)
                .getResultList();

        List<EducationalContext> educationalContexts = new ArrayList<>();

        for (Taxon taxon : resultList) {
            educationalContexts.add((EducationalContext) taxon);
        }

        return educationalContexts;
    }

    public Taxon findTaxonByRepoName(String name, String repoTable, Class level) {
        List<Taxon> taxons = createQuery(
                "SELECT t.taxon FROM " + repoTable + " t WHERE t.name = :name", Taxon.class) //
                .setParameter("name", name).getResultList();
        List<Taxon> res = taxons
                .stream()
                .filter(t -> (t.getClass().equals(level)))
                .collect(Collectors.toList());

        if (res != null) {
            return res.get(0);
        }

        return null;
    }
}
