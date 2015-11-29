package ee.v22.service;

import java.util.Map;

import javax.inject.Inject;

import ee.v22.dao.LanguageDAO;
import ee.v22.dao.TranslationDAO;
import ee.v22.model.Language;
import ee.v22.model.TranslationGroup;

public class TranslationService {

    @Inject
    private TranslationDAO translationDAO;

    @Inject
    private LanguageDAO languageDAO;

    public Map<String, String> getTranslationsFor(String languageCode) {
        if (languageCode == null) {
            return null;
        }

        Language language = languageDAO.findByCode(languageCode);
        if (language == null) {
            return null;
        }

        TranslationGroup translationGroupFor = translationDAO.findTranslationGroupFor(language);
        if (translationGroupFor == null) {
            return null;
        }

        return translationGroupFor.getTranslations();
    }

}
