public class InsertCommand extends Command {
    private MYHtml html;
    private String tagName;
    private String idValue;
    private String insertLocation;
    private String textContent;
    private MYElement element;

    public InsertCommand(MYHtml html, String tagName, String idValue, String insertLocation, String textContent) {
        this.html = html;
        this.tagName = tagName;
        this.idValue = idValue;
        this.insertLocation = insertLocation;
        this.textContent = textContent;
    }

    @Override
    public void execute() {
        element = new MYElement(idValue, tagName);
        element.setText(textContent);
        html.insert(tagName, idValue, insertLocation, textContent);
    }

    @Override
    public void undo() {
        html.delete(idValue);
    }

    @Override
    public void redo() {
        execute();
    }
}
