public class AppendCommand extends Command {
    private MYHtml html;
    private String tagName;
    private String idValue;
    private String parentElement;
    private String textContent;
    private MYElement element;

    public AppendCommand(MYHtml html, String tagName, String idValue, String parentElement, String textContent) {
        this.html = html;
        this.tagName = tagName;
        this.idValue = idValue;
        this.parentElement = parentElement;
        this.textContent = textContent;
    }

    @Override
    public void execute() {
        element = new MYElement(idValue, tagName);
        element.setText(textContent);
        html.append(tagName, idValue, parentElement, textContent);
    }

    @Override
    public void undo() {
        html.delete(idValue); // 删除追加的元素
    }

    @Override
    public void redo() {
        execute();
    }
}
