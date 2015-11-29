package ee.v22.common.test;

import ee.v22.guice.GuiceInjector;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * JUnit test runner that initialize Guice.
 * 
 * @author jordan
 */
public class GuiceTestRunner extends BlockJUnit4ClassRunner {

    public GuiceTestRunner(Class<?> klass) throws InitializationError {
        super(klass);

        GuiceInjector.init();
    }

    @Override
    public Object createTest() throws Exception {
        Object test = super.createTest();
        GuiceInjector.getInjector().injectMembers(test);
        return test;
    }
}
