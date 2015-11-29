package ee.v22.guice.module;

import com.google.inject.servlet.ServletModule;
import ee.v22.guice.GuiceInjector;
import ee.v22.service.*;
import org.opensaml.saml2.binding.encoding.HTTPRedirectDeflateEncoder;

@GuiceInjector.Module
public class RestModule extends ServletModule {

    @Override
    protected void configureServlets() {
        bind(MaterialService.class);
        bind(TranslationService.class);
        bind(PageService.class);
        bind(LanguageService.class);
        bind(TagService.class);
        bind(TaxonService.class);
        bind(ResourceTypeService.class);
        bind(LicenseTypeService.class);
        bind(UserService.class);
        bind(LoginService.class);
        bind(HTTPRedirectDeflateEncoder.class);
        bind(AuthenticatedUserService.class);
        bind(LogoutService.class);
        bind(MobileIDLoginService.class);
        bind(MobileIDSOAPService.class);
    }
}
