import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PyCheck {

    private JPanel panel;
    private JLabel label;

    public PyCheck() {
        JFrame window = new JFrame("Python环境检测");
        window.setContentPane(this.panel);
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window.setSize(200, 150);
        window.setLocationRelativeTo(null);
        String data = Script.ReadCmdLine("python -V");
        if(data.startsWith("Python")) {
            String regEx = "[^0-9.]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(data);
            String result = m.replaceAll("").trim();
            label.setText("Python版本为：" + result);
        }
        else {
            label.setText("未检测到Python");
        }
    }

}
