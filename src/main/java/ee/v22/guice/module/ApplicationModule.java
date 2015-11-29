package ee.v22.guice.module;

import com.google.inject.AbstractModule;

import ee.v22.ApplicationLauncher;
import ee.v22.ApplicationManager;
import ee.v22.guice.GuiceInjector;

@GuiceInjector.Module
public class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        requestStaticInjection(ApplicationLauncher.class);
        requestStaticInjection(ApplicationManager.class);
    }
}
