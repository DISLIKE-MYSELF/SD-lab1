public class MYTitle {
    private MYTextNode text;
    private String id = "title";

    public void setText(String text) {
        this.text = new MYTextNode(text);
    }

    public String getText() {
        return text.getText();
    }

    public String toStringIndented() {
        return "<title>\n      " + text.getText() + "\n    </title>";
    }

    public String toStringTree(String prefix) {
        return prefix + "title\n" + prefix + "-  " + text.getText();
    }
    
}
