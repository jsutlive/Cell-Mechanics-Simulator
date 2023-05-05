package Renderer.UIElements.Panels;

import Utilities.StringUtils;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import static Renderer.UIElements.Panels.MultiComponentPanel.PLACEHOLDER_FIELD;


public class StaticFieldPanel {
    JPanel panel;
    public boolean isSerializable = true;
    public String name;

    public StaticFieldPanel(Class<?> type, Object value, String name ) {
        initialize(type, value, name);
    }

    private void initialize(Class<?> type, Object value, String name){
        panel = new JPanel(new GridLayout(0, 2));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(400, 30));
        panel.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.decode("#e4e4e4")),new EmptyBorder(4,25,2,25)));
        this.name = name;
        panel.add(new JLabel(StringUtils.splitCamelCase(name)));
        if (type == int.class) {
            int val;
            JLabel field;
            if(value!=PLACEHOLDER_FIELD) {
                val = (int) value;
                field = new JLabel(String.valueOf(val));
            }else{
                field = new JLabel((String)value);
            }
            field.setBackground(Color.white);
            panel.add(field);
        } else if (type == float.class) {
            float val;
            JLabel field;
            if(value!=PLACEHOLDER_FIELD) {
                val = (float) value;
                field = new JLabel(String.valueOf(val));
            }else{
                field = new JLabel((String)value);
            }
            field.setBackground(Color.white);
            panel.add(field);
        } else if (type == String.class) {
            String val = (String) value;
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
