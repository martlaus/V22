package ee.v22.service;

import java.util.List;

import javax.inject.Inject;

import ee.v22.dao.LanguageDAO;
import ee.v22.model.Language;

public class LanguageService {

    @Inject
    private LanguageDAO languageDAO;

    public Language getLanguage(String languageCode) {
        return languageDAO.findByCode(languageCode);
    }

    public List<Language> getAll() {
        return languageDAO.findAll();
    }

}
