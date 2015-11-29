package ee.v22.service;

import javax.inject.Inject;

import ee.v22.dao.TagDAO;
import ee.v22.model.Tag;

public class TagService {

    @Inject
    private TagDAO tagDAO;

    public Tag getTagByName(String name) {
        return tagDAO.findTagByName(name);
    }
}
