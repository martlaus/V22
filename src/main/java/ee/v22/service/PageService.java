package ee.v22.service;

import javax.inject.Inject;

import ee.v22.dao.PageDAO;
import ee.v22.model.Language;
import ee.v22.model.Page;

public class PageService {

    @Inject
    private PageDAO pageDao;

    public Page getPage(String name, Language language) {
        return pageDao.findByNameAndLanguage(name, language);
    }
}
