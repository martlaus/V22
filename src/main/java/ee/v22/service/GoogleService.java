package ee.v22.service;

import java.util.Arrays;

import javax.inject.Inject;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import ee.v22.utils.ConfigurationProperties;

public class GoogleService {

    private static Logger logger = LoggerFactory.getLogger(GoogleService.class);

    @Inject
    private Configuration configuration;

    public String getUserID(String token) {
        String userID = null;

        try {
            GoogleIdTokenVerifier verifier = newVerifier();

            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                Payload payload = idToken.getPayload();
                if (payload.getHostedDomain()
                        .equals(configuration.getString(ConfigurationProperties.GOOGLE_APPS_DOMAIN_NAME))) {
                    logger.info("User ID: " + payload.getSubject());
                    userID = payload.getSubject();
                } else {
                    logger.info("Invalid ID token.");
                }
            } else {
                logger.info("Invalid ID token.");
            }
        } catch (Exception e) {
            logger.warn("Exception verifying google id token.");
        }

        return userID;
    }

    private GoogleIdTokenVerifier newVerifier() throws Exception {
        HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        return new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Arrays.asList(configuration.getString(ConfigurationProperties.GOOGLE_CLIENT_ID))).build();
    }

}
