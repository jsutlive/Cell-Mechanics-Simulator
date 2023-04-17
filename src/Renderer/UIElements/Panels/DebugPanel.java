package Renderer.UIElements.Panels;

import Framework.Utilities.Debug;
import Framework.Utilities.Message;
import Renderer.UIElements.Windows.ConsolePopUp;

import javax.swing.*;
import java.awt.*;

public class DebugPanel {
    private JPanel panel;
    private JButton msgLabel;

    public DebugPanel(){
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        Debug debug = new Debug();
        debug.onLog.subscribe(this::logMessage);

        panel.setBackground(Color.darkGray);
        msgLabel = new JButton();
        msgLabel.setMaximumSize(new Dimension(Short.MAX_VALUE,25));
        msgLabel.setBackground(Color.darkGray);
        msgLabel.setOpaque(true);
        msgLabel.addActionListener(e->
        {
            new ConsolePopUp(debug);
        });
        panel.add(msgLabel);
    }

    public JPanel getPanel() {
        return panel;
    }

    public void logMessage(Message msg){
        msgLabel.setText(msg.getText());
        msgLabel.setBackground(Color.darkGray);
        msgLabel.setBorderPainted(false);
        msgLabel.setFocusPainted(false);
        msgLabel.setContentAreaFilled(false);
        msgLabel.setForeground(msg.getTypeColor());
    }
}
