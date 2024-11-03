import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class MYHtmlTest {

    private MYHtml html;

    @BeforeEach
    void setUp() {
        html = new MYHtml();
    }

    @Test
    void testConstructor() {
        assertNotNull(html.getHead());
        assertNotNull(html.getBody());
        assertTrue(html.containsKey("html"));
        assertTrue(html.containsKey("title"));
        assertTrue(html.containsKey("body"));
        assertTrue(html.containsKey("head"));
    }

    @Test
    void testInsert() {
        html.insert("div", "testDiv", "body", "Test content");
        assertTrue(html.containsKey("testDiv"));
        assertEquals("Test content", html.findElement("testDiv").getText());
    }

    @Test
    void testAppend() {
        html.append("p", "testP", "body", "Test paragraph");
        assertTrue(html.containsKey("testP"));
        assertEquals("Test paragraph", html.findElement("testP").getText());
    }

    @Test
    void testEditId() {
        html.insert("div", "oldId", "body", "Test content");
        html.editId("oldId", "newId");
        assertFalse(html.containsKey("oldId"));
        assertTrue(html.containsKey("newId"));
    }

    @Test
    void testEditText() {
        html.insert("div", "testDiv", "body", "Old content");
        html.editText("testDiv", "New content");
        assertEquals("New content", html.findElement("testDiv").getText());
    }

    @Test
    void testDelete() {
        html.insert("div", "testDiv", "body", "Test content");
        html.delete("testDiv");
        assertFalse(html.containsKey("testDiv"));
    }

    @Test
    void testInit() {
        html.init();
        assertEquals("", html.getHead().getTitle());
    }

    @Test
    void testToStringIndented() {
        html.append("div", "testDiv", "body", "Test content");
        String result = html.toStringIndented(2);
        assertTrue(result.contains("<html>"));
        assertTrue(result.contains("<div id=\"testDiv\">"));
        assertTrue(result.contains("Test content"));
    }

    @Test
    void testToStringTree() {
        html.append("div", "testDiv", "body", "Test content");
        String result = html.toStringTree();
        assertTrue(result.contains("html"));
        assertTrue(result.contains("div#testDiv"));
        assertTrue(result.contains("Test content"));
    }

    @Test
    void testReadFromFile(@TempDir Path tempDir) throws IOException {
        File testFile = tempDir.resolve("test.html").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("<html>\r\n" + //
                                "    <head>\r\n" + //
                                "        <title>Test Title</title>\r\n" + //
                                "    </head>\r\n" + //
                                "    <body>\r\n" + //
                                "        <div id=\"testDiv\">Test content</div>\r\n" + //
                                "    </body>\r\n" + //
                                "</html>\r\n" + //
                                "");
        }

        html.readFromFile(testFile.getAbsolutePath());
        assertEquals("Test Title", html.getHead().getTitle());
        assertTrue(html.containsKey("testDiv"));
        assertEquals("Test content", html.findElement("testDiv").getText());
    }

    @Test
    void testWriteToFile(@TempDir Path tempDir) throws IOException {
        html.insert("div", "testDiv", "body", "Test content");
        File outputFile = tempDir.resolve("output.html").toFile();
        html.writeToFile(outputFile.getAbsolutePath());
        assertTrue(outputFile.exists());
        // You might want to read the file content and check it here
    }
}