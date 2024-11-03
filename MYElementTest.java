import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class MYElementTest {
    private MYElement rootElement;
    private MYElement childElement1;
    private MYElement childElement2;

    @BeforeEach
    void setUp() {
        rootElement = new MYElement("root", "div");
        childElement1 = new MYElement("child1", "p");
        childElement2 = new MYElement("child2", "span");
    }

    @Test
    void testConstructor() {
        assertEquals("root", rootElement.getId());
        assertEquals("div", rootElement.getTagName());
        assertTrue(rootElement.getChildren().isEmpty());
    }

    @Test
    void testAddChild() {
        rootElement.addChild(childElement1);
        assertEquals(1, rootElement.getChildren().size());
        assertEquals(childElement1, rootElement.getChildren().get(0));
        assertEquals(rootElement, childElement1.getParent());
    }

    @Test
    void testAddChildWithId() {
        rootElement.addChild(childElement1);
        rootElement.addChild(childElement2, "child1");
        assertEquals(2, rootElement.getChildren().size());
        assertEquals(childElement2, rootElement.getChildren().get(0));
        assertEquals(childElement1, rootElement.getChildren().get(1));
    }

    @Test
    void testAddChildWithNonExistentId() {
        rootElement.addChild(childElement1, "non-existent");
        assertEquals(1, rootElement.getChildren().size());
        assertEquals(childElement1, rootElement.getChildren().get(0));
    }

    @Test
    void testRemoveChild() {
        rootElement.addChild(childElement1);
        rootElement.addChild(childElement2);
        rootElement.removeChild("child1");
        assertEquals(1, rootElement.getChildren().size());
        assertEquals(childElement2, rootElement.getChildren().get(0));
    }

    @Test
    void testSetId() {
        rootElement.setId("new-root");
        assertEquals("new-root", rootElement.getId());
    }

    @Test
    void testSetAndGetText() {
        rootElement.setText("Root text");
        assertEquals("Root text", rootElement.getText());
    }

    @Test
    void testToStringIndented() {
        rootElement.setText("Root text");
        rootElement.addChild(childElement1);
        childElement1.setText("Child text");
        String expected = "<div id=\"root\">\n   Root text\n   <p id=\"child1\">\n      Child text\n   </p>\n</div>";
        assertEquals(expected, rootElement.toStringIndented(""));
    }

    @Test
    void testToStringTree() {
        rootElement.setText("Root text");
        rootElement.addChild(childElement1);
        childElement1.setText("Child text");
        String expected = "div#root\n-  Root text\n-  p#child1\n-  -  Child text\n";
        assertEquals(expected, rootElement.toStringTree(""));
    }

    @Test
    void testGetTagName() {
        assertEquals("div", rootElement.getTagName());
    }

    @Test
    void testGetParent() {
        rootElement.addChild(childElement1);
        assertEquals(rootElement, childElement1.getParent());
        assertNull(rootElement.getParent());
    }

    @Test
    void testGetChildren() {
        rootElement.addChild(childElement1);
        rootElement.addChild(childElement2);
        List<MYElement> children = rootElement.getChildren();
        assertEquals(2, children.size());
        assertTrue(children.contains(childElement1));
        assertTrue(children.contains(childElement2));
    }
}