package presentation;

import javax.swing.*;

public class FailureWindow extends JFrame {
    JPanel contentPanel;
    JLabel sucessLabel;

    public FailureWindow(String message){
        super("Failure");
        initGUI(message);
    }

    private void initGUI(String message){
        setSize(300, 300);
        contentPanel = new JPanel();
        setContentPane(contentPanel);
        sucessLabel = new JLabel(message);
        contentPanel.add(sucessLabel);
        setVisible(true);
        setResizable(false);
    }
}
