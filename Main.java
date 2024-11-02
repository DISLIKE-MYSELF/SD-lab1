public class Main {
    public static void main(String[] args) {
        MYHtml html = new MYHtml();
        html.getHead().setTitle("My Webpage");

        MYElement h1 = new MYElement("page-title", "h1");
        h1.setText("Welcome to my webpage");
        html.addElement(h1);

        MYElement p = new MYElement("description", "p");
        p.setText("This is a paragraph.");
        html.addElement(p);

        MYElement p1 = new MYElement("description1", "p1");
        p1.setText("This is a paragraph.1");
        h1.addChild(p1);

        System.out.println(html.toStringIndented());
        System.out.println(html.toStringTree());
    }
}
