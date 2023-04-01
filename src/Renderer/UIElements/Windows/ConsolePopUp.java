package Renderer.UIElements.Windows;

import Framework.Utilities.Debug;
import Framework.Utilities.Message;

import javax.swing.*;
import java.awt.*;

import static Framework.Data.ImageHandler.loadImage;

public class ConsolePopUp {
    JDialog jd;
    JFrame jFrame;
    public ConsolePopUp(Debug debug){
        jFrame = new JFrame();

        jd = new JDialog(jFrame);
        jd.getContentPane().setBackground(Color.darkGray);
        jd.setTitle("History");
        jFrame.setIconImage(loadImage("record.png"));

        jd.setBounds(500, 300, 400, 300);

        jd.setLayout(new GridLayout(0,2));

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
        jd.add(j);
        jd.add(jt);
        jd.revalidate();
    }
}
