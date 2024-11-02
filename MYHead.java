public class MYHead {
    private MYTitle title;
    private String id = "head";

    public MYHead() {
        this.title = new MYTitle();
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public String getTitle() {
        return title.getText();
    }

    public String toStringIndented() {
        return "  <head>\n    " + title.toStringIndented() + "\n  </head>\n";
    }

    public String toStringTree(String prefix) {
        return prefix + "head\n" + title.toStringTree(prefix + "-  ");
    }
    
}
