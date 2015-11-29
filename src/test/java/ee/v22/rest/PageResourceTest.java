package ee.v22.rest;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import ee.v22.common.test.ResourceIntegrationTestBase;

public class PageResourceTest extends ResourceIntegrationTestBase {

    @Test
    public void getAboutPageInEstonian() {
        String name = "About";
        String languageCode = "est";
        Response response = doGet("page?name=" + name + "&language=" + languageCode);
        Map<String, Object> page = response.readEntity(new GenericType<Map<String, Object>>() {
        });

        assertEquals(4, page.size());
        assertEquals(1, page.get("id"));
        assertEquals("About", page.get("name"));
        assertEquals("<h1>Meist</h1><p>Tekst siin</p>", page.get("content"));
        @SuppressWarnings("unchecked")
        Map<String, String> language = (Map<String, String>) page.get("language");
        assertEquals(languageCode, language.get("code"));
    }

    @Test
    public void getHelpPageInEnglish() {
        String name = "Help";
        String languageCode = "eng";
        Response response = doGet("page?name=" + name + "&language=" + languageCode);
        Map<String, Object> page = response.readEntity(new GenericType<Map<String, Object>>() {
        });

        assertEquals(4, page.size());
        assertEquals(6, page.get("id"));
        assertEquals("Help", page.get("name"));
        assertEquals("<h1>Help</h1><p>Text here</p>", page.get("content"));
        @SuppressWarnings("unchecked")
        Map<String, String> language = (Map<String, String>) page.get("language");
        assertEquals(languageCode, language.get("code"));
    }

    @Test
    public void getPageWithoutParam() {
        Response response = doGet("page");
        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void getNotExistingPage() {
        Response response = doGet("page?name=doesnotExist&language=eng");
        assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void getPageWithNotSupportedLanguage() {
        Response response = doGet("page?name=About&language=notSupported");
        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void getPageWithBlankName() {
        Response response = doGet("page?name=&language=eng");
        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void getPageWithBlankLanguage() {
        Response response = doGet("page?name=About&language=");
        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

}
