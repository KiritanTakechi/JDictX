import javax.swing.*;

public class PyCheck {
    private final JFrame window;
    private JPanel panel;
    private JTextArea textArea;
    private JButton button;

    public PyCheck() {
        window = new JFrame("Python环境检测");
        window.setContentPane(this.panel);
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window.setSize(200, 150);
        window.setLocationRelativeTo(null);
        this.textArea.setEditable(false);
        this.textArea.setLineWrap(true);
        this.textArea.setWrapStyleWord(true);

        textArea.setText(Script.ReadCmdLine("python -V"));

        button.addActionListener(e -> window.dispose());
    }

}
