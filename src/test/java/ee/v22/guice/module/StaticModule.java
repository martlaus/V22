package ee.v22.guice.module;

import com.google.inject.AbstractModule;

import ee.v22.server.EmbeddedJettyTest;
import ee.v22.guice.GuiceInjector;

@GuiceInjector.Module
public class StaticModule extends AbstractModule {

    @Override
    protected void configure() {
        requestStaticInjection(EmbeddedJettyTest.class);
    }
}
