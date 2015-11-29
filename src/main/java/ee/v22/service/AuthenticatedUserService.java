package ee.v22.service;

import ee.v22.dao.AuthenticatedUserDAO;
import ee.v22.model.AuthenticatedUser;
import ee.v22.security.KeyStoreUtils;
import ee.v22.utils.EncryptionUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.Configuration;
import org.joda.time.DateTime;
import org.json.JSONObject;

import javax.inject.Inject;
import java.security.PrivateKey;

public class AuthenticatedUserService {

    public static final String TAAT = "TAAT";

    @Inject
    private AuthenticatedUserDAO authenticatedUserDAO;

    @Inject
    private Configuration configuration;

    public AuthenticatedUser getAuthenticatedUserByToken(String token) {
        return authenticatedUserDAO.findAuthenticatedUserByToken(token);
    }

    public String signUserData(AuthenticatedUser authenticatedUser) {
        DateTime createdAt = DateTime.now();

        JSONObject authenticationContext = new JSONObject();
        authenticationContext.put("schacHomeOrganization", authenticatedUser.getHomeOrganization());
        authenticationContext.put("roles", authenticatedUser.getAffiliations());

        JSONObject userDataObject = new JSONObject();
        userDataObject.put("createdAt", createdAt);
        userDataObject.put("authProvider", TAAT);
        userDataObject.put("authCtx", authenticationContext);

        PrivateKey privateKey = KeyStoreUtils.getV22SigningCredential(configuration).getPrivateKey();
        final byte[] cipherText = EncryptionUtils.encrypt(userDataObject.toString(), privateKey);

        return Base64.encodeBase64String(cipherText);
    }
}
