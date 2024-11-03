public class DeleteCommand extends Command {
    private MYHtml html;
    private String elementId;
    private MYElement element;
    private int index;

    public DeleteCommand(MYHtml html, String elementId) {
        this.html = html;
        this.elementId = elementId;
        this.element = html.findElement(elementId);
        if(this.element.getParent() != null){
            this.index = this.element.getParent().getChildren().indexOf(element);
        }else{
            this.index = this.html.getBody().findIndex(elementId);
        }
        
    }

    @Override
    public void execute() {
        html.delete(elementId);
    }

    @Override
    public void undo() {
        // 需要实现元素恢复的逻辑
        // 这里可以保存删除的元素状态并重新插入
        if(index == 0){
            html.append(element.getTagName(), element.getId(), element.getParent().getId(), elementId);
        }
        if(element.getParent() != null){
            html.insert(element.getTagName(), element.getId(),element.getParent().getChildren().get(index).getId(), element.getText());
        }
        else{
            html.insert(element.getTagName(), element.getId(), html.getBody().get(index).getId(), elementId);
        }
    }

    @Override
    public void redo() {
        execute();
    }
}
