package Renderer.UIElements.Panels;

import Framework.Object.Component;
import Renderer.Renderer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StaticFieldPanel {
    JPanel panel;
    public boolean isSerializable = true;
    public String name;

    public StaticFieldPanel(Component c, Class<?> type, Object value, String name ) {
        panel = new JPanel(new GridLayout(0, 2));
        panel.setBorder(new EmptyBorder(2, 5, 2, 5));
        panel.setBackground(Color.lightGray);
        this.name = name;
        if (type == int.class) {
            int val = (int) value;
            panel.add(new JLabel(name));
            JLabel field = new JLabel(String.valueOf(val));
            field.setBackground(Color.white);
            panel.add(field);
        } else if (type == float.class) {
            float val = (float) value;
            panel.add(new JLabel(name));
            JLabel field = new JLabel(String.valueOf(val));
            field.setBackground(Color.white);
            panel.add(field);
        } else if (type == String.class) {
            String val = (String) value;
            panel.add(new JLabel(name));
            JLabel field = new JLabel(val);
            field.setBackground(Color.white);
            panel.add(field);
        } else {
            isSerializable = false;
        }
    }

    public JPanel getPanel(){
        return panel;
    }
}
