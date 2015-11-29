package ee.v22.guice.provider;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Configuration;

import ee.v22.guice.GuiceInjector;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Injector;

import ee.v22.common.test.GuiceTestRunner;

@RunWith(GuiceTestRunner.class)
public class HttpClientProviderTest {

    @Test
    public void get() {
        Client client = GuiceInjector.getInjector().getInstance(Client.class);

        assertNotNull(client);
        Configuration configuration = client.getConfiguration();
        assertTrue(configuration.isRegistered(JacksonFeature.class));
        assertTrue(configuration.isRegistered(ObjectMapperProvider.class));
    }

    @Test
    public void getAlwaysReturnSameObject() {
        Injector injector = GuiceInjector.getInjector();
        Client client = injector.getInstance(Client.class);

        for (int i = 0; i < 10; i++) {
            assertSame(client, injector.getInstance(Client.class));
        }
    }
}
