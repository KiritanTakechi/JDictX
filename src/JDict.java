import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class JDict {
    private JLabel label;
    private JTextField textField;
    private JButton button0;
    private JTextArea textArea;
    private JButton button1;
    private JPanel panel;
    private JButton button2;

    public JDict() {
        JFrame window = new JFrame("英汉大辞典");
        window.setContentPane(this.panel);
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(800, 600);
        window.setLocationRelativeTo(null);
        this.textArea.setEditable(false);
        this.textArea.setLineWrap(true);
        this.textArea.setWrapStyleWord(true);

        button0.addActionListener(e -> {
            Search();
        });
        button1.addActionListener(e -> textArea.setText(""));
        button2.addActionListener(e -> new Thread(PyCheck::new).start());
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() ==KeyEvent.VK_ENTER) {
                    Search();
                }
            }
        });
    }

    private void processResult(String result) {
        try {
            JSONObject obj = JSON.parseObject(result);
            textArea.append("单词：" + obj.get("word") + "\n");
            textArea.append("音标：" + obj.get("phonetic") + "\n");
            textArea.append("英释义：" + "\n" + obj.get("definition") + "\n");
            textArea.append("汉释义：" + "\n" + obj.get("translation") + "\n");
            textArea.append("科斯林" + obj.get("collins") + "★" + "词汇" + "\n");
            if (Objects.equals(obj.get("oxford"), 1)) {
                textArea.append("牛津必背三千词词汇");
            }
        } catch (Exception ignored) {
            textArea.setText("无此单词！");
        }
    }

    private void Search() {
        textArea.setText("");
        String py = "python";
        String query = System.getProperty("user.dir") + "/script/query.py";
        String db = System.getProperty("user.dir") + "/db/stardict.db";
        if (!Objects.equals(textField.getText(), "")) {
            String data = (Script.ReadCmdLine(py + " " + query + " " + textField.getText() + " " + db));
            processResult(data);
            System.out.println("data = " + data);
        }
        else {
            textArea.setText("请输入单词！");
        }
    }
}
