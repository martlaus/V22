package ee.v22.guice.module;

import org.opensaml.saml2.binding.encoding.HTTPRedirectDeflateEncoder;

import com.google.inject.servlet.ServletModule;

import ee.v22.guice.GuiceInjector;
import ee.v22.service.AuthenticatedUserService;
import ee.v22.service.FacebookService;
import ee.v22.service.GoogleService;
import ee.v22.service.LoginService;
import ee.v22.service.LogoutService;
import ee.v22.service.MobileIDLoginService;
import ee.v22.service.MobileIDSOAPService;
import ee.v22.service.UserService;

@GuiceInjector.Module
public class RestModule extends ServletModule {

    @Override
    protected void configureServlets() {
        bind(UserService.class);
        bind(LoginService.class);
        bind(HTTPRedirectDeflateEncoder.class);
        bind(AuthenticatedUserService.class);
        bind(LogoutService.class);
        bind(MobileIDLoginService.class);
        bind(MobileIDSOAPService.class);
        bind(GoogleService.class);
        bind(FacebookService.class);
    }
}
