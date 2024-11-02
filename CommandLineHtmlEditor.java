import java.util.Scanner;
import java.util.Stack;

public class CommandLineHtmlEditor {

    public static void main(String[] args) {
        MYHtml html = new MYHtml();
        Scanner scanner = new Scanner(System.in);
        Stack<Command> undoStack = new Stack<>();
        Stack<Command> redoStack = new Stack<>();
        boolean isInitialized = false; // 追踪是否已初始化或读取文件
        String command;
        while (true) {
            System.out.print("> "); // 提示用户输入命令
            command = scanner.nextLine().trim();
           

            // 处理命令
            String[] parts = command.split("\\s+");
            String cmd = parts[0].toLowerCase();

            try {
                switch (cmd) {
                    case "init":
                        html.init();
                        System.out.println("Editor initialized with empty HTML template.");
                        isInitialized = true;
                        break;

                    case "insert":
                        if (!isInitialized) {
                            System.out.println("Please initialize or read a file first.");
                            break;
                        }
                        if (parts.length < 4) {
                            System.out.println("Usage: insert tagName idValue insertLocation [textContent]");
                            break;
                        }
                        String insertTagName = parts[1];
                        String insertId = parts[2];
                        String insertLocation = parts[3];
                        String insertText = parts.length == 5 ? parts[4] : "";
                        if (html.containsKey(insertId)) {
                            throw new IllegalArgumentException("Element with id " + insertId + " already exists.");
                        }
                        if (!html.containsKey(insertLocation)) {
                            throw new IllegalArgumentException(
                                    "Element with id " + insertLocation + " does not exist.");
                        }
                        Command insertCommand = new InsertCommand(html, insertTagName, insertId, insertLocation,
                                insertText);
                        insertCommand.execute();
                        undoStack.push(insertCommand);
                        redoStack.clear();
                        System.out.println("Inserted element: " + insertId);
                        break;

                    case "append":
                        if (!isInitialized) {
                            System.out.println("Please initialize or read a file first.");
                            break;
                        }
                        if (parts.length < 4) {
                            System.out.println("Usage: append tagName idValue parentElement [textContent]");
                            break;
                        }

                        String appendTagName = parts[1];
                        String appendId = parts[2];
                        String parentElement = parts[3];
                        String appendText = parts.length == 5 ? parts[4] : "";
                        if (html.containsKey(appendId)) {
                            throw new IllegalArgumentException("Element with id " + appendId + " already exists.");
                        }
                        if (!html.containsKey(parentElement)) {
                            throw new IllegalArgumentException("Element with id " + parentElement + " does not exist.");
                        }
                        Command appendcommand = new AppendCommand(html, appendTagName, appendId, parentElement,
                                appendText);
                        appendcommand.execute();
                        undoStack.push(appendcommand);
                        redoStack.clear(); // 清空重做栈
                        System.out.println("Appended element: " + appendId);
                        break;

                    case "edit-id":
                        if (!isInitialized) {
                            System.out.println("Please initialize or read a file first.");
                            break;
                        }
                        if (parts.length != 3) {
                            System.out.println("Usage: edit-id oldId newId");
                            break;
                        }
                        if (html.containsKey(parts[2])) {
                            throw new IllegalArgumentException("Element with new id " + parts[2] + " already exists.");
                        }

                        if (!html.containsKey(parts[1])) {
                            throw new IllegalArgumentException("Element with old id " + parts[1] + " does not exist.");
                        }
                        Command EDIcommand = new EditIdCommand(html, parts[1], parts[2]);
                        EDIcommand.execute();
                        undoStack.push(EDIcommand);
                        redoStack.clear(); // 清空重做栈
                        System.out.println("Updated id from " + parts[1] + " to " + parts[2]);
                        break;

                    case "edit-text":
                        if (!isInitialized) {
                            System.out.println("Please initialize or read a file first.");
                            break;
                        }
                        if (parts.length < 2) {
                            System.out.println("Usage: edit-text element [newTextContent]");
                            break;
                        }
                        String elementId = parts[1];
                        String newTextContent = parts.length == 3 ? parts[2] : "";
                        if (!html.containsKey(elementId)) {
                            throw new IllegalArgumentException("Element with id " + elementId + " does not exist.");
                        }
                        Command EDTcommand = new EditTextCommand(html, elementId, newTextContent);
                        EDTcommand.execute();
                        undoStack.push(EDTcommand);
                        redoStack.clear(); // 清空重做栈
                        System.out.println("Updated text of element: " + elementId);
                        break;

                    case "delete":
                        if (!isInitialized) {
                            System.out.println("Please initialize or read a file first.");
                            break;
                        }
                        if (parts.length != 2) {
                            System.out.println("Usage: delete element");
                            break;
                        }
                        String deleteelementId = parts[1];
                        if (!html.containsKey(deleteelementId)) {
                            throw new IllegalArgumentException(
                                    "Element with id " + deleteelementId + " does not exist.");
                        }
                        if (deleteelementId.equals("body") || deleteelementId.equals("html")
                                || deleteelementId.equals("head") || deleteelementId.equals("title")) {
                            throw new IllegalArgumentException(
                                    "Element with id " + deleteelementId + " can not be deleted");
                        }
                        Command deletecommand = new DeleteCommand(html, deleteelementId);
                        deletecommand.execute();
                        undoStack.push(deletecommand);
                        redoStack.clear(); // 清空重做栈
                        System.out.println("Deleted element: " + deleteelementId);
                        break;

                    case "print-indent":
                        if (!isInitialized) {
                            System.out.println("Please initialize or read a file first.");
                            break;
                        }
                        int indent = (parts.length == 2) ? Integer.parseInt(parts[1]) : 2;
                        html.printIndent(indent);
                        break;

                    case "print-tree":
                        if (!isInitialized) {
                            System.out.println("Please initialize or read a file first.");
                            break;
                        }
                        html.printTree();
                        break;

                    case "spell-check":
                        if (!isInitialized) {
                            System.out.println("Please initialize or read a file first.");
                            break;
                        }
                        html.spellCheck();
                        break;

                    case "read":

                        if (parts.length != 2) {
                            System.out.println("Usage: read filepath");
                            break;
                        }
                        html.readFromFile(parts[1]);
                        System.out.println("Read HTML file: " + parts[1]);
                        redoStack.clear();
                        undoStack.clear();
                        isInitialized = true; // 追踪是否已初始化或读取文件
                        break;

                    case "save":
                        if (!isInitialized) {
                            System.out.println("Please initialize or read a file first.");
                            break;
                        }
                        if (parts.length != 2) {
                            System.out.println("Usage: save filepath");
                            break;
                        }
                        html.writeToFile(parts[1]);
                        System.out.println("Saved HTML file: " + parts[1]);
                        redoStack.clear();
                        undoStack.clear();
                        break;

                    case "undo":
                        if (!isInitialized) {
                            System.out.println("Please initialize or read a file first.");
                            break;
                        }
                        if (!undoStack.isEmpty()) {
                            Command lastCommand = undoStack.pop();
                            lastCommand.undo();
                            System.out.println(lastCommand.toString());
                            redoStack.push(lastCommand); // 推入重做栈
                            System.out.println("Undo last command.");
                        } else {
                            System.out.println("Undo Stack empty");
                        }

                        break;

                    case "redo":
                        if (!isInitialized) {
                            System.out.println("Please initialize or read a file first.");
                            break;
                        }
                        if (!redoStack.isEmpty()) {
                            Command commandToRedo = redoStack.pop();
                            commandToRedo.redo();
                            undoStack.push(commandToRedo); // 推入撤销栈
                            System.out.println("Redo last command.");
                        } else {
                            System.out.println("Redo Stack empty");
                        }

                        break;

                    case "exit":
                        System.out.println("Exiting editor.");
                        scanner.close();
                        return;

                    default:
                        if (!isInitialized) {
                            System.out.println("Please initialize or read a file first.");
                            break;
                        }
                        System.out.println("Unknown command: " + cmd);
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
