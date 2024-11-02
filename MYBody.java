import java.util.ArrayList;
import java.util.List;

public class MYBody {
    private List<MYElement> elements; // 使用 List 存储元素
    private String id = "body";

    public MYBody() {
        elements = new ArrayList<>(); // 初始化 List
    }

    public int findIndex(String id){
        for (MYElement element : elements) {
            if(element.getId().equals(id)){
                return elements.indexOf(element);
            }
        }
        return -1;
    }

    public MYElement get(int i){
        return elements.get(i);
    }

    public void addElement(MYElement element,MYElement locatioElement) {
        MYElement parent = locatioElement.getParent();
        if (parent != null) {
            parent.addChild(element,element.getId()); // 将新元素添加为父元素的子元素
        }
        else  if(parent == null){
            int index = elements.indexOf(locatioElement);
            if (index != -1) {
                // 在指定元素之前插入新元素
                elements.add(index, element);
            }
            if(elements.isEmpty()){
                elements.add(element);
            }
        } 
        
    }

    public void addElement(MYElement element){
        elements.add(element);
    }

    public void removeElement(String id) {
        // 尝试从根元素开始删除
        if (removeElementFromList(elements, id)) {
            return;
        }
        // System.out.println("body delete");
        throw new IllegalArgumentException("Element with id " + id + " does not exist.");
    }
    
    // 辅助方法：递归查找并删除元素
    private boolean removeElementFromList(List<MYElement> elements, String id) {
        if(elements == null){
            return false;
        }
        for (int i = 0; i < elements.size(); i++) {
            MYElement element = elements.get(i);
            // 如果找到匹配的 ID，删除元素
            if (element.getId().equals(id)) {
                elements.remove(i);
                return true;
            }
            // 递归查找子元素
            if (removeElementFromList(element.getChildren(), id)) {
                return true;
            }
        }
        return false; // 没有找到元素
    }
    

    public String toStringIndented(String prefix) {
        StringBuilder sb = new StringBuilder("  <body>\n");
        for (MYElement element : elements) {
            sb.append(element.toStringIndented(prefix + "   ")).append("\n");
        }
        sb.append("  </body>\n");
        return sb.toString();
    }

    public String toStringTree(String prefix) {
        StringBuilder sb = new StringBuilder(prefix + "body\n");
        for (MYElement element : elements) {
            sb.append(element.toStringTree(prefix + "-  "));  // 调整子元素的缩进
        }
        return sb.toString();
    }

    public void append(MYElement newElement, MYElement parentElement) {
        if (parentElement == null || parentElement.getId() == "body") {
            if (!elements.contains(newElement)) {
                elements.add(newElement);
            }
        } else {
            if (parentElement != null) {
                parentElement.addChild(newElement); // 将新元素添加为父元素的子元素
            }
        }  
    }
}
