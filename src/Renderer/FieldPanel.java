package Renderer;

import Framework.Object.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.lang.reflect.Type;

public class FieldPanel {

    JPanel panel;
    public boolean isSerializable = true;
    public String name;

    public FieldPanel(Component c, Class<?> type, Object value, String name ){
        panel = new JPanel(new GridLayout(0, 2));
        panel.setBorder(new EmptyBorder(2,5,2,5));
        panel.setBackground(Color.lightGray);
        this.name = name;
        if(type == int.class){
            int val = (int) value;
            panel.add(new JLabel(name));
            JTextField field = new JTextField(String.valueOf(val));
            field.addActionListener(e -> changeGUI(c, field, type));
            panel.add(field);
        }else if(type == float.class){
            float val = (float) value;
            panel.add(new JLabel(name));
            JTextField field = new JTextField(String.valueOf(val));
            field.addActionListener(e -> changeGUI(c, field, type));
            panel.add(field);
        }else if(type == String.class){
            String val = (String) value;
            panel.add(new JLabel(name));
            JTextField field = new JTextField(val);
            field.addActionListener(e -> changeGUI(c, field, type));
            panel.add(field);
        }else{
            isSerializable = false;
        }
    }



    public void changeGUI(Component c, JTextField field, Class<?> type){
        if(type == int.class){
            int value = Integer.parseInt(field.getText());
            c.changeFieldOnGUI(name, value);
        } else if(type == float.class){
            float value = Float.parseFloat(field.getText());
            c.changeFieldOnGUI(name, value);
        }else if(type == String.class){
            String value = field.getText();
            c.changeFieldOnGUI(name, value);
        }
    }

    public JPanel getPanel(){
        return panel;
    }
}
