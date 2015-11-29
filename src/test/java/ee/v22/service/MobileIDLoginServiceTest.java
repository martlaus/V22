package ee.v22.service;

import static ee.v22.service.MobileIDLoginService.ESTONIAN_CALLING_CODE;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.soap.SOAPException;

import ee.v22.model.AuthenticationState;
import ee.v22.model.Language;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.IAnswer;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;

import ee.v22.dao.AuthenticationStateDAO;
import ee.v22.model.mobileid.MobileIDSecurityCodes;
import ee.v22.model.mobileid.soap.MobileAuthenticateResponse;

@RunWith(EasyMockRunner.class)
public class MobileIDLoginServiceTest {

    @TestSubject
    private MobileIDLoginService mobileIDLoginService = new MobileIDLoginService();

    @Mock
    private MobileIDSOAPService mobileIDSOAPService;

    @Mock
    private AuthenticationStateDAO authenticationStateDAO;

    @Test
    public void authenticate() throws Exception {
        String phoneNumber = "+37255501234";
        String idCode = "88881010888";
        Language language = new Language();
        language.setCode("est");

        MobileAuthenticateResponse mobileAuthenticateResponse = new MobileAuthenticateResponse();
        mobileAuthenticateResponse.setSessionCode("789560251");
        mobileAuthenticateResponse.setStatus("OK");
        mobileAuthenticateResponse.setIdCode(idCode);
        mobileAuthenticateResponse.setName("Jaan");
        mobileAuthenticateResponse.setSurname("Sepp");
        mobileAuthenticateResponse.setCountry("EE");
        mobileAuthenticateResponse.setUserCommonName("JAAN,SEPP,88881010888");
        mobileAuthenticateResponse.setChallengeID("4321");

        expect(mobileIDSOAPService.authenticate(phoneNumber, idCode, language)).andReturn(mobileAuthenticateResponse);

        Capture<AuthenticationState> capturedAuthenticationState = newCapture();
        expectCreateAuthenticationState(capturedAuthenticationState);

        replayAll();

        MobileIDSecurityCodes mobileIDSecurityCodes = mobileIDLoginService.authenticate(phoneNumber, idCode, language);

        verifyAll();

        validateAuthenticationState(capturedAuthenticationState, mobileAuthenticateResponse);
        validateMobileIDSecurityCodes(mobileIDSecurityCodes, mobileAuthenticateResponse, capturedAuthenticationState);
    }

    @Test
    public void authenticateWithoutCallingCode() throws Exception {
        String phoneNumber = "5554321";
        String idCode = "11110000111";
        Language language = new Language();
        language.setCode("est");

        MobileAuthenticateResponse mobileAuthenticateResponse = new MobileAuthenticateResponse();
        mobileAuthenticateResponse.setSessionCode("789560251");
        mobileAuthenticateResponse.setStatus("OK");
        mobileAuthenticateResponse.setIdCode(idCode);
        mobileAuthenticateResponse.setName("Jaan");
        mobileAuthenticateResponse.setSurname("Sepp");
        mobileAuthenticateResponse.setCountry("EE");
        mobileAuthenticateResponse.setUserCommonName("JAAN,SEPP,11110000111");
        mobileAuthenticateResponse.setChallengeID("4321");

        expect(mobileIDSOAPService.authenticate(ESTONIAN_CALLING_CODE + phoneNumber, idCode, language))
                .andReturn(mobileAuthenticateResponse);

        Capture<AuthenticationState> capturedAuthenticationState = newCapture();
        expectCreateAuthenticationState(capturedAuthenticationState);

        replayAll();

        MobileIDSecurityCodes mobileIDSecurityCodes = mobileIDLoginService.authenticate(phoneNumber, idCode, language);

        verifyAll();

        validateAuthenticationState(capturedAuthenticationState, mobileAuthenticateResponse);
        validateMobileIDSecurityCodes(mobileIDSecurityCodes, mobileAuthenticateResponse, capturedAuthenticationState);
    }

    @Test
    public void authenticateNonEstonianPhoneNumber() throws Exception {
        String phoneNumber = "+33355501234";
        String idCode = "99991010888";
        Language language = new Language();
        language.setCode("eng");

        replayAll();

        MobileIDSecurityCodes mobileIDSecurityCodes = mobileIDLoginService.authenticate(phoneNumber, idCode, language);

        verifyAll();

        assertNull(mobileIDSecurityCodes);
    }

    @Test
    public void isAuthenticated() throws SOAPException {
        String token = "someTOKEN";

        AuthenticationState authenticationState = new AuthenticationState();
        authenticationState.setSessionCode("2835728357835");
        authenticationState.setToken(token);

        expect(authenticationStateDAO.findAuthenticationStateByToken(token)).andReturn(authenticationState);
        expect(mobileIDSOAPService.isAuthenticated(authenticationState)).andReturn(true);

        replayAll();

        boolean isAuthenticated = mobileIDLoginService.isAuthenticated(token);

        verifyAll();

        assertTrue(isAuthenticated);
    }

    @Test
    public void isAuthenticatedFalse() throws SOAPException {
        String token = "someTOKEN";

        AuthenticationState authenticationState = new AuthenticationState();
        authenticationState.setSessionCode("2835728357835");
        authenticationState.setToken(token);

        expect(authenticationStateDAO.findAuthenticationStateByToken(token)).andReturn(authenticationState);
        expect(mobileIDSOAPService.isAuthenticated(authenticationState)).andReturn(false);

        replayAll();

        boolean isAuthenticated = mobileIDLoginService.isAuthenticated(token);

        verifyAll();

        assertFalse(isAuthenticated);
    }

    @Test
    public void isAuthenticatedInvalidToken() throws SOAPException {
        String token = "invalidTOKEN";

        expect(authenticationStateDAO.findAuthenticationStateByToken(token)).andReturn(null);

        replayAll();

        boolean isAuthenticated = mobileIDLoginService.isAuthenticated(token);

        verifyAll();

        assertFalse(isAuthenticated);
    }

    private void expectCreateAuthenticationState(Capture<AuthenticationState> capturedAuthenticationState) {
        // Using .andReturn(capturedAuthenticationState.getValue()) would give
        // error saying "Nothing captured yet"
        expect(authenticationStateDAO.createAuthenticationState(EasyMock.capture(capturedAuthenticationState)))
                .andAnswer(new IAnswer<AuthenticationState>() {
                    @Override
                    public AuthenticationState answer() throws Throwable {
                        return capturedAuthenticationState.getValue();
                    }
                });
    }

    private void validateAuthenticationState(Capture<AuthenticationState> capturedAuthenticationState,
            MobileAuthenticateResponse response) {
        assertEquals(response.getIdCode(), capturedAuthenticationState.getValue().getIdCode());
        assertEquals(response.getName(), capturedAuthenticationState.getValue().getName());
        assertEquals(response.getSurname(), capturedAuthenticationState.getValue().getSurname());
        assertEquals(response.getSessionCode(), capturedAuthenticationState.getValue().getSessionCode());
        DateTime created = capturedAuthenticationState.getValue().getCreated();
        assertFalse(created.isAfterNow());
    }

    private void validateMobileIDSecurityCodes(MobileIDSecurityCodes mobileIDSecurityCodes,
            MobileAuthenticateResponse response, Capture<AuthenticationState> capturedAuthenticationState) {
        assertEquals(response.getChallengeID(), mobileIDSecurityCodes.getChallengeId());
        assertNotNull(capturedAuthenticationState.getValue().getToken());
        assertEquals(capturedAuthenticationState.getValue().getToken(), mobileIDSecurityCodes.getToken());
    }

    private void replayAll(Object... mocks) {
        replay(authenticationStateDAO, mobileIDSOAPService);

        if (mocks != null) {
            for (Object object : mocks) {
                replay(object);
            }
        }
    }

    private void verifyAll(Object... mocks) {
        verify(authenticationStateDAO, mobileIDSOAPService);

        if (mocks != null) {
            for (Object object : mocks) {
                verify(object);
            }
        }
    }

}
