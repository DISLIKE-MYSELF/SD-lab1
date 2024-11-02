import java.util.ArrayList;
import java.util.List;

public class MYElement {
    private String id;
    private String tagName;
    private List<MYElement> children;
    private MYTextNode text;
    private MYElement parent;

    public MYElement(String id, String tagName) {
        this.id = id;
        this.tagName = tagName;
        this.children = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public List<MYElement>getChildren(){
        return children;
    }


    public void addChild(MYElement child) {
        children.add(child);
        child.parent = this;
    }

    public void addChild(MYElement child, String id) {
        int index = findChildIndexById(id);
        if (index != -1) {
            // 如果找到了对应id的子元素，则在其之前插入新子元素
            children.add(index, child);
        } else {
            // 否则，直接添加到最后
            children.add(child);
        }
        child.parent = this;
    }

    private int findChildIndexById(String id) {
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1; // 如果没有找到，返回-1
    }

    public MYElement getParent(){
        return parent;
    }

    public void removeChild(String id) {
        children.removeIf(child -> child.getId().equals(id));
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = new MYTextNode(text);
    }

    public String getText() {
        return text.getText();
    }

    public String toStringIndented(String prefix) {
        StringBuilder sb = new StringBuilder(prefix + "<" + tagName + " id=\"" + id + "\">\n");
        if (text != null) {
            sb.append(prefix + "   ").append(text.getText()).append("\n");
        }
        for (MYElement child : children) {
            sb.append(child.toStringIndented(prefix + "   ")).append("\n");
        }
        sb.append(prefix + "</" + tagName + ">");
        return sb.toString();
    }

    public String toStringTree(String prefix) {
        StringBuilder sb = new StringBuilder(prefix + tagName + "#" + id + "\n");
        if (text != null) {
            sb.append(prefix + "-  " + text.getText() + "\n");
        }
        for (MYElement child : children) {
            sb.append(child.toStringTree(prefix + "-  "));
        }
        return sb.toString();
    }
    

    public String getTagName() {
        return tagName;
    }
}
