package ee.v22.rest.filter;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.UriInfo;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EasyMockRunner.class)
public class DopSecurityContextTest {

    @Mock
    private DopPrincipal v22Principal;

    @Mock
    private UriInfo uriInfo;

    @Test
    public void isUserInRoleNull() {
        DopSecurityContext securityContext = getDopSecurityContext(null, uriInfo);

        boolean response = securityContext.isUserInRole("USER");

        assertFalse(response);
    }

    @Test
    public void isUserInRole() {
        DopSecurityContext securityContext = getDopSecurityContext(v22Principal, uriInfo);

        expect(v22Principal.isUserInRole("USER")).andReturn(true);

        replay(v22Principal, uriInfo);

        assertTrue(securityContext.isUserInRole("USER"));

        verify(v22Principal, uriInfo);

    }

    @Test
    public void isSecureFalse() throws URISyntaxException {
        DopSecurityContext securityContext = getDopSecurityContext(v22Principal, uriInfo);
        URI uri = new URI("http://random.org");

        expect(uriInfo.getRequestUri()).andReturn(uri);

        replay(v22Principal, uriInfo);

        assertFalse(securityContext.isSecure());

        verify(v22Principal, uriInfo);
    }

    @Test
    public void isSecure() throws URISyntaxException {
        DopSecurityContext securityContext = getDopSecurityContext(v22Principal, uriInfo);
        URI uri = new URI("https://random.org");

        expect(uriInfo.getRequestUri()).andReturn(uri);

        replay(v22Principal, uriInfo);

        assertTrue(securityContext.isSecure());

        verify(v22Principal, uriInfo);
    }

    private DopSecurityContext getDopSecurityContext(DopPrincipal v22Principal, UriInfo uriInfo) {
        return new DopSecurityContext(v22Principal, uriInfo);
    }
}
