public class EditIdCommand extends Command {
    private MYHtml html;
    private String oldId;
    private String newId;

    public EditIdCommand(MYHtml html, String oldId, String newId) {
        this.html = html;
        this.oldId = oldId;
        this.newId = newId;
    }

    @Override
    public void execute() {
        html.editId(oldId, newId);
    }

    @Override
    public void undo() {
        System.out.println("Undoing ID change: " + newId + " back to " + oldId);
        html.editId(newId, oldId); // 恢复到旧 ID
    }

    @Override
    public void redo() {
        execute();
    }
}
