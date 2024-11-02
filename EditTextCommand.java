public class EditTextCommand extends Command {
    private MYHtml html;
    private String elementId;
    private String oldText;
    private String newText;

    public EditTextCommand(MYHtml html, String elementId, String newText) {
        this.html = html;
        this.elementId = elementId;
        this.newText = newText;
    }

    @Override
    public void execute() {
        // 先记录旧文本，以便于撤销
        MYElement element = html.findElement(elementId);
        if (element != null) {
            oldText = element.getText(); // 保存当前文本
            html.editText(elementId, newText);
        }
    }

    @Override
    public void undo() {
        html.editText(elementId, oldText); // 恢复旧文本
    }

    @Override
    public void redo() {
        execute();
    }
}
