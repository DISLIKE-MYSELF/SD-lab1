import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

public class CommandLineHtmlEditorTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    private void runEditor(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        CommandLineHtmlEditor.main(new String[]{});
    }

    @Test
    void testInitCommand() {
        runEditor("init\nexit\n");
        assertTrue(outContent.toString().contains("Editor initialized with empty HTML template."));
    }

    @Test
    void testInsertCommand() {
        runEditor("init\ninsert div test-div body Test content\nexit\n");
        assertTrue(outContent.toString().contains("Inserted element: test-div"));
    }

    @Test
    void testAppendCommand() {
        runEditor("init\nappend p test-p body Test paragraph\nexit\n");
        assertTrue(outContent.toString().contains("Appended element: test-p"));
    }

    @Test
    void testEditIdCommand() {
        runEditor("init\ninsert div old-id body\nedit-id old-id new-id\nexit\n");
        assertTrue(outContent.toString().contains("Updated id from old-id to new-id"));
    }

    @Test
    void testEditTextCommand() {
        runEditor("init\ninsert div test-div body Old content\nedit-text test-div New-content\nexit\n");
        assertTrue(outContent.toString().contains("Updated text of element: test-div"));
    }

    @Test
    void testDeleteCommand() {
        runEditor("init\ninsert div test-div body\ndelete test-div\nexit\n");
        assertTrue(outContent.toString().contains("Deleted element: test-div"));
    }

    @Test
    void testPrintIndentCommand() {
        runEditor("init\ninsert div test-div body\nprint-indent\nexit\n");
        assertTrue(outContent.toString().contains("<html>"));
        assertTrue(outContent.toString().contains("</html>"));
    }

    @Test
    void testPrintTreeCommand() {
        runEditor("init\ninsert div test-div body\nprint-tree\nexit\n");
        assertTrue(outContent.toString().contains("html"));
        assertTrue(outContent.toString().contains("body"));
    }

    @Test
    void testUndoCommand() {
        runEditor("init\ninsert div test-div body\nundo\nexit\n");
        assertTrue(outContent.toString().contains("Undo last command."));
    }

    @Test
    void testRedoCommand() {
        runEditor("init\ninsert div test-div body\nundo\nredo\nexit\n");
        assertTrue(outContent.toString().contains("Redo last command."));
    }

    @Test
    void testSpellCheckCommand() {
        runEditor("init\nappend div test-div body This is a tset\nspell-check\nexit\n");
        System.out.println(outContent.toString());
        assertTrue(outContent.toString().contains("Potential error"));
    }

    @Test
    void testReadCommand() {
        // 注意：这个测试需要一个实际存在的HTML文件
        runEditor("read test.html\nexit\n");
        assertTrue(outContent.toString().contains("Read HTML file: test.html"));
    }

    @Test
    void testSaveCommand() {
        runEditor("init\ninsert div test-div body\nsave test_output.html\nexit\n");
        assertTrue(outContent.toString().contains("Saved HTML file: test_output.html"));
    }

    @Test
    void testUnknownCommand() {
        runEditor("init\nunknown_command\nexit\n");
        assertTrue(outContent.toString().contains("Unknown command: unknown_command"));
    }

    @Test
    void testErrorHandling() {
        runEditor("init\ninsert div test-div non_existent_element\nexit\n");
        assertTrue(outContent.toString().contains("Error: Element with id non_existent_element does not exist."));
    }
}