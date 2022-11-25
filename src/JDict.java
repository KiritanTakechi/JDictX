import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDict {
    private JLabel label;
    private JTextField textField;
    private JButton button0;
    private JTextArea textArea;
    private JButton button1;
    private JPanel panel;

    public JDict() {
        button0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String py = System.getProperty("user.dir") + "/venv/bin/python3";
                String query = System.getProperty("user.dir") + "/py/query.py";
                String db = System.getProperty("user.dir") + "/db/stardict.db";
                textArea.setText(ReadCmdLine(py + " " + query + " " + textField.getText() + " " + db));
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
            }
        });
    }

    public static void main(String[] args) {
        JFrame window = new JFrame("英汉大辞典");
        JDict jDict = new JDict();
        window.setContentPane(jDict.panel);
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(500, 300);
        window.setLocationRelativeTo(null);
        jDict.textArea.setEditable(false);
        jDict.textArea.setLineWrap(true);
        jDict.textArea.setWrapStyleWord(true);
    }

    private static Logger log = Logger.getLogger(String.valueOf(JDict.class));

    public static String ReadCmdLine(String command){
        StringBuilder result = new StringBuilder();

        Process process = null;
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
            //获取执行结果
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));
            String line = null;
            while ((line = bufrIn.readLine()) != null) {
                result.append(line).append('\n');
            }
            while ((line = bufrError.readLine()) != null) {
                result.append(line).append('\n');
            }


        } catch (Exception e) {
            log.log(Level.parse("error"),"执行命令失败，e:{}",e);
        }finally {
            closeStream(bufrIn);
            closeStream(bufrError);

            // 销毁子进程
            if (process != null) {
                process.destroy();
            }
        }
        //replaceAll("\\\\x", "%"),解决返回结果编码问题
        return result.toString().replaceAll("\\\\x", "%");
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                log.log(Level.parse("error"),"关闭连接失败，e:{}");
            }
        }

    }
}
