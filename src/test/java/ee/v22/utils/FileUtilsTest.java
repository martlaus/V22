package ee.v22.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.easymock.EasyMockRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EasyMockRunner.class)
public class FileUtilsTest {

    @Test
    public void getFileAsStreamNoFile() {
        InputStream inputStream = FileUtils.getFileAsStream("wrong.xml");
        assertNull(inputStream);
    }

    @Test
    public void getFileAsStream() throws IOException {
        // create tmp file
        File tempFile = File.createTempFile("testFileForGetFileAsStrem", ".v22");
        PrintWriter writer = new PrintWriter(tempFile, "UTF-8");
        writer.println("bla bla bla");
        writer.close();
        tempFile.deleteOnExit();

        InputStream inputStream = FileUtils.getFileAsStream(tempFile.getAbsolutePath());
        assertNotNull(inputStream);
    }
}
