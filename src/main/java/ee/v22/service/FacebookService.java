package ee.v22.service;

import javax.inject.Inject;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ee.v22.utils.ConfigurationProperties;
import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import facebook4j.conf.ConfigurationBuilder;

public class FacebookService {

    private static Logger logger = LoggerFactory.getLogger(FacebookService.class);

    @Inject
    private Configuration configuration;

    /*
     * Get Facebook user ID using the given access token.
     */
    public String getUserID(String token) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true) //
                .setOAuthAppId(configuration.getString(ConfigurationProperties.FACEBOOK_APP_ID)) //
                .setOAuthAppSecret(configuration.getString(ConfigurationProperties.FACEBOOK_APP_SECRET));
        FacebookFactory ff = new FacebookFactory(cb.build());

        Facebook facebook = ff.getInstance();
        facebook.setOAuthAccessToken(new AccessToken(token));

        String id = null;
        try {
            id = facebook.users().getMe().getId();
        } catch (Exception e) {
            logger.info("Failed to get facebook user id. ");
        }

        return id;
    }

}
