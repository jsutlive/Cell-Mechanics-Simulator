package renderer.uiElements.panels;

import framework.utilities.Debug;
import framework.utilities.Message;
import renderer.uiElements.windows.ConsolePopUp;

import javax.swing.*;
import java.awt.*;

/**
 * DebugPanel is a simple panel to interface with the debugger/logger system. Clicking its button will launch a small
 * console log window for viewing past messages.
 *
 * Copyright (c) 2023 Joseph Sutlive and Tony Zhang
 * All rights reserved
 */
public class DebugPanel {
    private final JPanel panel;
    private final JButton msgLabel;

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

        // Launch new console debug with reference to logger system upon button press.
        msgLabel.addActionListener(e-> new ConsolePopUp(debug));

        panel.add(msgLabel);
    }

    public JPanel getPanel() {
        return panel;
    }

    /**
     * Write most recent message to the logger panel to be displayed
     * @param msg a message to be displayed
     */
    public void logMessage(Message msg){
        msgLabel.setText(msg.getText());
        msgLabel.setBackground(Color.darkGray);
        msgLabel.setBorderPainted(false);
        msgLabel.setFocusPainted(false);
        msgLabel.setContentAreaFilled(false);
        msgLabel.setForeground(msg.getTypeColor());
    }
}
