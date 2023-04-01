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

        Debug debug = new Debug();
        debug.onLog.subscribe(this::logMessage);

        panel.setBackground(Color.darkGray);
        msgLabel = new JButton();
        msgLabel.setPreferredSize(new Dimension(800,25));
        msgLabel.setBackground(Color.darkGray);
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
        msgLabel.setForeground(msg.getTypeColor());
    }
}
