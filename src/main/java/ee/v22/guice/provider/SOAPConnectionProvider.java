package ee.v22.guice.provider;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;

import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Guice provider of SOAPConnection.
 */
@Singleton
public class SOAPConnectionProvider implements Provider<SOAPConnection> {

    @Override
    public synchronized SOAPConnection get() {
        try {
            SOAPConnectionFactory connectionFactory = SOAPConnectionFactory.newInstance();
            return connectionFactory.createConnection();
        } catch (Exception e) {
            throw new RuntimeException("Error creating new SOAP connection.");
        }
    }
}