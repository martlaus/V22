package ee.v22.guice.module;

import com.google.inject.AbstractModule;

import ee.v22.common.test.ResourceIntegrationTestBase;
import ee.v22.guice.GuiceInjector;

@GuiceInjector.Module()
public class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        requestStaticInjection(ResourceIntegrationTestBase.class);
    }
}
