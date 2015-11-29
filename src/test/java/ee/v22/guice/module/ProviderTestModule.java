package ee.v22.guice.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import ee.v22.guice.GuiceInjector;
import ee.v22.guice.provider.ConfigurationTestProvider;
import ee.v22.guice.provider.EntityManagerFactoryTestProvider;
import ee.v22.guice.provider.ObjectMapperGuiceProvider;
import ee.v22.guice.provider.SOAPConnectionTestProvider;
import org.apache.commons.configuration.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.xml.soap.SOAPConnection;

@GuiceInjector.Module(override = ProviderModule.class)
public class ProviderTestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EntityManagerFactory.class).toProvider(EntityManagerFactoryTestProvider.class);
        bind(Configuration.class).toProvider(ConfigurationTestProvider.class);
        bind(ObjectMapper.class).toProvider(ObjectMapperGuiceProvider.class);
        bind(SOAPConnection.class).toProvider(SOAPConnectionTestProvider.class);
    }
}
