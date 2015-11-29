package ee.v22.security;

import ee.v22.utils.FileUtils;
import ee.v22.utils.ConfigurationProperties;
import org.apache.commons.configuration.Configuration;
import org.opensaml.xml.security.Criteria;
import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.credential.KeyStoreCredentialResolver;
import org.opensaml.xml.security.criteria.EntityIDCriteria;
import org.opensaml.xml.security.x509.X509Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static org.apache.commons.io.IOUtils.closeQuietly;

public class KeyStoreUtils {

    private static final Logger logger = LoggerFactory.getLogger(KeyStoreUtils.class);

    private static KeyStore V22keyStore;

    public static KeyStore loadKeystore(String filename, String password) {
        KeyStore keyStore = null;
        InputStream inputStream = null;

        try {
            inputStream = FileUtils.getFileAsStream(filename);
            if (inputStream == null) {
                throw new RuntimeException(format("Failed to load keystore in path: %s", filename));
            }

            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(inputStream, password.toCharArray());
        } catch (Exception e) {
            throw new RuntimeException(format("Failed to load keystore in path: %s", filename), e);
        } finally {
            closeQuietly(inputStream);
        }

        return keyStore;
    }

    public static Credential getSigningCredential(KeyStore keystore, String entityId, String entityPassword) {
        X509Credential credential = null;

        try {
            Map<String, String> passwords = new HashMap<>();
            passwords.put(entityId, entityPassword);
            KeyStoreCredentialResolver resolver = new KeyStoreCredentialResolver(keystore, passwords);

            Criteria criteria = new EntityIDCriteria(entityId);
            CriteriaSet criteriaSet = new CriteriaSet(criteria);

            credential = (X509Credential) resolver.resolveSingle(criteriaSet);
        } catch (Exception e) {
            logger.error("Error while getting signing credential.");
        }

        return credential;
    }

    public static void setKeyStore(KeyStore keyStore) {
        KeyStoreUtils.V22keyStore = keyStore;
    }

    private static KeyStore getV22KeyStore(Configuration configuration) {
        if (V22keyStore == null) {
            String filename = configuration.getString(ConfigurationProperties.KEYSTORE_FILENAME);
            String password = configuration.getString(ConfigurationProperties.KEYSTORE_PASSWORD);
            V22keyStore = KeyStoreUtils.loadKeystore(filename, password);
        }

        return V22keyStore;
    }

    public static Credential getV22SigningCredential(Configuration configuration) {
        String entityId = configuration.getString(ConfigurationProperties.KEYSTORE_SIGNING_ENTITY_ID);
        String entityPassword = configuration.getString(ConfigurationProperties.KEYSTORE_SIGNING_ENTITY_PASSWORD);
        return KeyStoreUtils.getSigningCredential(getV22KeyStore(configuration), entityId, entityPassword);
    }
}
