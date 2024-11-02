import java.io.*;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONArray;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.net.URLEncoder;

public class MYHtml {
    private MYHead head;
    private MYBody body;
    private String id = "html";

    private Map<String, MYElement> elements;

    public MYHtml() {
        this.head = new MYHead();
        this.body = new MYBody();

        this.elements = new HashMap<>();
        this.elements.put("html", new MYElement("html", "html"));
        this.elements.put("title", new MYElement("title", "title"));
        this.elements.put("body", new MYElement("body", "body"));
        this.elements.put("head", new MYElement("head", "head"));
    }

    public boolean containsKey(String key) {
        return elements.containsKey(key);
    }

    public void insert(String tagName, String idValue, String insertLocation, String textContent) {

        MYElement newElement = new MYElement(idValue, tagName);
        newElement.setText(textContent);
        elements.put(idValue, newElement);
        MYElement insert = findElement(insertLocation);
        body.addElement(newElement, insert); // 将新元素添加到body

    }

    public void append(String tagName, String idValue, String parentElement, String textContent) {

        MYElement newElement = new MYElement(idValue, tagName);
        newElement.setText(textContent);
        elements.put(idValue, newElement);
        MYElement parent = findElement(parentElement);
        body.append(newElement, parent);

    }

    public MYElement findElement(String id) {
        return elements.get(id);
    }

    public void editId(String oldId, String newId) {

        MYElement element = elements.get(oldId);
        elements.remove(oldId);
        element.setId(newId); // 更新元素 ID
        elements.put(newId, element); // 重新添加到元素列表

    }

    public void editText(String elementId, String newTextContent) {

        MYElement element = findElement(elementId);
        if (element != null) {
            element.setText(newTextContent); // 更新文本内容
        }

    }

    public void delete(String elementId) {
        body.removeElement(elementId); // 删除元素
        // System.out.println("html delete");
        removeElement(elementId);

    }

    public void removeElement(String elementId) {

        for (MYElement element : findElement(elementId).getChildren()) {
            removeElement(element.getId());
        }
        elements.remove(elementId);
    }

    public void init() {
        head = new MYHead();
        body = new MYBody();
        head.setTitle("");
    }

    public MYHead getHead() {
        return head;
    }

    public MYBody getBody() {
        return body;
    }

    public void addElement(MYElement element) {
        // 添加元素到 body 或 head
        if (element.getTagName().equals("title")) {
            head.setTitle(element.getText());
        } else {
            body.addElement(element);
        }

    }

    public String toStringIndented() {
        // 返回缩进格式字符串
        return "<html>\n" + head.toStringIndented() + body.toStringIndented("   ") + "</html>";
    }

    public String toStringTree() {
        return "html\n" + head.toStringTree("-  ") + "\n" + body.toStringTree("-  ");
    }

    public void readFromFile(String filepath) {
        if (filepath == null || filepath.isEmpty()) {
            System.out.println("文件路径无效。");
            return;
        }

        try {
            File inputFile = new File(filepath);
            if (!inputFile.exists()) {
                System.out.println("文件不存在：" + filepath);
                return;
            }

            // 解析 HTML 文件
            Document doc = Jsoup.parse(inputFile, "UTF-8");
            this.head.setTitle(doc.title());

            // 解析 body 中的元素并添加到 body
            doc.body().childNodes().forEach(node -> {
                if (node instanceof Element) {
                    System.out.println(node.toString());
                    MYElement newElement = new MYElement(((Element) node).tagName(), ((Element) node).id());
                    elements.put(id, newElement);
                    MYElement parent = findElement(((Element) (node.parent())).id());
                    body.append(newElement, parent); // 可以根据需要插入位置
                } else if (node instanceof TextNode) {
                    if (((Element) (node.parent())).id() != "") {
                        MYTextNode textNode = new MYTextNode(((TextNode) node).getWholeText());
                        MYElement parent = findElement(((Element) (node.parent())).id());
                        parent.setText(textNode.getText());
                    }

                }
            });
            System.out.println("文件已成功读取：" + filepath);
        } catch (FileNotFoundException e) {
            System.out.println("文件未找到：" + e.getMessage());
        } catch (IOException e) {
            System.out.println("读取文件时出错：" + e.getMessage());
        } catch (Exception e) {
            System.out.println("发生意外错误：" + e.getMessage());
        }
    }

    public void writeToFile(String filepath) {
        if (filepath == null || filepath.isEmpty()) {
            System.out.println("文件路径无效。");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            writer.write(toStringIndented());
            System.out.println("文件已成功保存：" + filepath);
        } catch (IOException e) {
            System.out.println("写入文件时出错：" + e.getMessage());
        } catch (Exception e) {
            System.out.println("发生意外错误：" + e.getMessage());
        }
    }

    public void printIndent(int indent) {
        System.out.println(this.toStringIndented());
    }

    public void printTree() {
        System.out.println(this.toStringTree());
    }

    public void spellCheck() {
        // 获取 HTML 中的所有文本内容
        StringBuilder textBuilder = new StringBuilder();
        for (Map.Entry<String, MYElement> entry : elements.entrySet()) {
            textBuilder.append(entry.getValue().getText()).append(" ");
        }

        String textToCheck = textBuilder.toString().trim();
        String apiUrl = "https://api.languagetool.org/v2/check";

        try {
            // 输出待检查的文本
            System.out.println("Text to check: " + textToCheck);

            // 创建连接
            URL url = URI.create(apiUrl).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);

            // 发送请求数据
            String urlParameters = "text=" + URLEncoder.encode(textToCheck, "UTF-8") + "&language=en-US";
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            // 获取响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // 解析 JSON 响应
            // System.out.println("Response: " + response.toString());

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray matches = jsonResponse.getJSONArray("matches");

            // 输出检查结果
            if (matches.length() == 0) {
                System.out.println("No misspelled words found.");
            } else {
                for (int i = 0; i < matches.length(); i++) {
                    JSONObject match = matches.getJSONObject(i);
                    String message = match.getString("message");
                    // System.out.println(match);
                    if (message.contains("This sentence does not start with an uppercase letter.")) {
                        continue; // 跳过这个错误
                    }

                    System.out.println("Potential error at characters " +
                            match.getInt("offset") + "-" + (match.getInt("offset") + match.getInt("length")) + ": " +
                            match.getString("message"));

                    // 获取建议的修改，并只输出前 5 个
                    JSONArray replacements = match.getJSONArray("replacements");
                    System.out.print("Suggested correction(s): ");
                    for (int j = 0; j < Math.min(replacements.length(), 5); j++) {
                        System.out.print(replacements.getJSONObject(j).getString("value")
                                + (j < replacements.length() - 1 ? ", " : ""));
                    }
                    System.out.println(); // 换行
                }
            }
        } catch (Exception e) {
            System.out.println("Error during spell check: " + e.getMessage());
        }
    }

}