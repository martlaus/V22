package ee.v22.guice.provider;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Guice provider of SOAPConnection.
 */
@Singleton
public class SOAPConnectionTestProvider implements Provider<SOAPConnection> {

    @Override
    public synchronized SOAPConnection get() {
        return new SOAPConnectionMock();
    }
}

class SOAPConnectionMock extends SOAPConnection {

    private static final Map<String, Map<String, String>> mobileAuthenticateResponses;
    private static final Map<String, Map<String, String>> getMobileAuthenticateStatusResponses;

    static {
        // The key is phone number
        mobileAuthenticateResponses = new HashMap<>();
        Map<String, String> response;

        response = new HashMap<>();
        response.put("Sesscode", "8927225289");
        response.put("Status", "OK");
        response.put("UserIDCode", "22334455667");
        response.put("UserGivenname", "Matt");
        response.put("UserSurname", "Smith");
        response.put("UserCountry", "EE");
        response.put("UserCN", "MATT,SMITH,22334455667");
        response.put("ChallengeID", "1111");
        mobileAuthenticateResponses.put("+37255551234", response);

        response = new HashMap<>();
        response.put("Sesscode", "782652658");
        response.put("Status", "OK");
        response.put("UserIDCode", "33445566778");
        response.put("UserGivenname", "Matt");
        response.put("UserSurname", "Smith");
        response.put("UserCountry", "EE");
        response.put("UserCN", "MATT,SMITH,33445566778");
        response.put("ChallengeID", "1111");
        mobileAuthenticateResponses.put("+37244441234", response);

        response = new HashMap<>();
        response.put("Sesscode", "894689260456");
        mobileAuthenticateResponses.put("+37233331234", response);

        response = new HashMap<>();
        response.put("Sesscode", "653568853");
        response.put("Status", "OK");
        response.put("UserIDCode", "66778899001");
        response.put("UserGivenname", "Jon");
        response.put("UserSurname", "Smith");
        response.put("UserCountry", "LT");
        response.put("UserCN", "JON,SMITH,66778899001");
        response.put("ChallengeID", "1111");
        mobileAuthenticateResponses.put("+37077778888", response);

        // The key is session code (Sesscode)
        getMobileAuthenticateStatusResponses = new HashMap<>();

        response = new HashMap<>();
        response.put("Status", "USER_AUTHENTICATED");
        getMobileAuthenticateStatusResponses.put("8927225289", response);

        response = new HashMap<>();
        response.put("Status", "NOT_VALID");
        getMobileAuthenticateStatusResponses.put("782652658", response);

        response = new HashMap<>();
        response.put("Status", "USER_AUTHENTICATED");
        getMobileAuthenticateStatusResponses.put("894689260456", response);

        response = new HashMap<>();
        response.put("Status", "USER_AUTHENTICATED");
        getMobileAuthenticateStatusResponses.put("653568853", response);
    }

    @Override
    public SOAPMessage call(SOAPMessage request, Object to) throws SOAPException {
        SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
        SOAPElement requestElement = (SOAPElement) body.getChildElements().next();
        String elementName = requestElement.getElementName().getLocalName();

        switch (elementName) {
            case "MobileAuthenticate":
                return mobileAuthenticate(parseRequest(request));
            case "GetMobileAuthenticateStatus":
                return getMobileAuthenticateStatus(parseRequest(request));
        }

        return null;
    }

    private SOAPMessage mobileAuthenticate(Map<String, String> request) throws SOAPException {
        String phoneNumber = request.get("PhoneNo");
        if (mobileAuthenticateResponses.containsKey(phoneNumber)) {
            return createSOAPMessage("MobileAuthenticateResponse", mobileAuthenticateResponses.get(phoneNumber));
        }
        return createSOAPFault("SOAP-ENV:Client", "301", "User is not a Mobile-ID client");
    }

    private SOAPMessage getMobileAuthenticateStatus(Map<String, String> request) throws SOAPException {
        String sessionCode = request.get("Sesscode");
        if (getMobileAuthenticateStatusResponses.containsKey(sessionCode)) {
            return createSOAPMessage("GetMobileAuthenticateStatusResponse",
                    getMobileAuthenticateStatusResponses.get(sessionCode));
        }
        return createSOAPFault("SOAP-ENV:Client", "101", "No session or session timeout");
    }

    private Map<String, String> parseRequest(SOAPMessage request) throws SOAPException {
        SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
        SOAPElement responseElement = (SOAPElement) body.getChildElements().next();

        @SuppressWarnings("unchecked")
        Iterator<SOAPElement> iterator = responseElement.getChildElements();
        Map<String, String> message = new HashMap<>();
        while (iterator.hasNext()) {
            SOAPElement element = iterator.next();
            message.put(element.getElementName().getLocalName(), element.getValue());
        }
        return message;
    }

    private SOAPMessage createSOAPMessage(String messageName, Map<String, String> childElements) throws SOAPException {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage message = messageFactory.createMessage();

        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        SOAPBody body = envelope.getBody();
        envelope.getHeader().detachNode();

        String namespacePrefix = "pre";
        String namespaceURI = "http://www.example.com/Service/Service.wsdl";

        Name name = envelope.createName(messageName, namespacePrefix, namespaceURI);
        SOAPBodyElement element = body.addBodyElement(name);

        for (Map.Entry<String, String> child : childElements.entrySet()) {
            Name elementName = envelope.createName(child.getKey());
            SOAPElement soapElement = element.addChildElement(elementName);
            soapElement.addTextNode(child.getValue());
        }

        return message;
    }

    private SOAPMessage createSOAPFault(String faultCode, String faultString, String detailMessage)
            throws SOAPException {
        String message = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" //
                + "<SOAP-ENV:Header/>" //
                + "<SOAP-ENV:Body>" //
                + "<SOAP-ENV:Fault>" //
                + "<faultcode>" + faultCode + "</faultcode>" //
                + "<faultstring xml:lang=\"en\">" + faultString + "</faultstring>" //
                + "<detail><message>" + detailMessage + "</message></detail>" //
                + "</SOAP-ENV:Fault>" //
                + "</SOAP-ENV:Body>" //
                + "</SOAP-ENV:Envelope>";
        InputStream is = new ByteArrayInputStream(message.getBytes());
        SOAPMessage faultMessage = null;
        try {
            faultMessage = MessageFactory.newInstance().createMessage(null, is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create SOAP Fault message.");
        }
        return faultMessage;
    }

    @Override
    public void close() throws SOAPException {
    }

}