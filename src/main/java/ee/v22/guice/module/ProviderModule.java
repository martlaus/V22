package ee.v22.guice.module;

import com.google.inject.AbstractModule;
import ee.v22.guice.GuiceInjector;
import ee.v22.guice.provider.*;
import org.apache.commons.configuration.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.client.Client;
import javax.xml.soap.SOAPConnection;

@GuiceInjector.Module
public class ProviderModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Configuration.class).toProvider(ConfigurationProvider.class);
        bind(EntityManagerFactory.class).toProvider(EntityManagerFactoryProvider.class);
        bind(EntityManager.class).toProvider(EntityManagerProvider.class);
        bind(Client.class).toProvider(HttpClientProvider.class);
        bind(SOAPConnection.class).toProvider(SOAPConnectionProvider.class);
    }
}
