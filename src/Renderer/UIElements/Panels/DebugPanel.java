package Renderer.UIElements.Panels;

import Framework.Debug.Debug;

import javax.swing.*;
import java.awt.*;

public class DebugPanel {
    private JPanel panel;
    private JLabel msgLabel;

    public DebugPanel(){
        panel = new JPanel();
        panel.setBackground(Color.darkGray);
        msgLabel = new JLabel();
        panel.add(msgLabel);
        Debug debug = new Debug();
        debug.onLog.subscribe(this::logMessage);
        debug.onLogWarning.subscribe(this::logWarning);
        debug.onLogError.subscribe(this::logError);
    }

    public JPanel getPanel() {
        return panel;
    }

    public void logError(String msg){
        msgLabel.setText(msg);
        msgLabel.setForeground(Color.RED);
    }

    public void logWarning(String msg){
        msgLabel.setText(msg);
        msgLabel.setForeground(Color.YELLOW);
    }

    public void logMessage(String msg){
        msgLabel.setText(msg);
        msgLabel.setForeground(Color.GREEN);
    }
}
