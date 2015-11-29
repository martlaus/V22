package ee.v22.rest.jackson.map;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import ee.v22.model.Language;

/**
 * Converts JSON date string into LocalDate.
 * 
 * @author Jordan Silva
 *
 */
public class LanguageDeserializer extends JsonDeserializer<Language> {

    @Override
    public Language deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        Language language = new Language();
        language.setCode(jp.getText());
        return language;
    }
}
