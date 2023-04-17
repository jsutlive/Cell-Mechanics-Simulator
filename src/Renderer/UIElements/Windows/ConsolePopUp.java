package Renderer.UIElements.Windows;

import Framework.Utilities.Debug;
import Framework.Utilities.Message;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Flow;

import static Framework.Data.ImageHandler.loadImage;

public class ConsolePopUp {
    JDialog jd;
    JFrame jFrame;

    JPanel panel;
    public ConsolePopUp(Debug debug){
        jFrame = new JFrame();
        panel = new JPanel();
        panel.setLayout(new GridLayout(0,1,0,10));
        panel.setBackground(Color.darkGray);
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);

        jd = new JDialog(jFrame);
        jd.add(scrollPane);
        jd.getContentPane().setBackground(Color.darkGray);
        jd.setTitle("History");
        jFrame.setIconImage(loadImage("record.png"));

        jd.setBounds(500, 300, 400, 300);

        for(Message m : debug.messageLog){
            addLog(m);
        }

        debug.onLog.subscribe(this::addLog);
        jd.setVisible(true);
    }

    private void addLog(Message m){
        JLabel j = new JLabel(m.getText());
        j.setForeground(m.getTypeColor());
        JLabel jt = new JLabel(m.getDateTime().toString());
        jt.setForeground(m.getTypeColor());
        panel.add(j);
        panel.add(jt);
        jd.revalidate();
    }
}
