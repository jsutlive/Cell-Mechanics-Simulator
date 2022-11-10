package Renderer.UIElements.Panels;

import Framework.Object.Component;
import Renderer.UIElements.ColorDropDownMenu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

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
            field.addActionListener(e -> changeGUI(c, field.getText(), type));
            panel.add(field);
        }else if(type == float.class){
            float val = (float) value;
            panel.add(new JLabel(name));
            JTextField field = new JTextField(String.valueOf(val));
            field.addActionListener(e -> changeGUI(c, field.getText(), type));
            panel.add(field);
        }else if(type == String.class){
            String val = (String) value;
            panel.add(new JLabel(name));
            JTextField field = new JTextField(val);
            field.addActionListener(e -> changeGUI(c, field.getText(), type));
            panel.add(field);
        }
        else if(type == Color.class){
            Color val = (Color) value;
            panel.add(new JLabel(name));
            panel.setLayout(new GridLayout(0, 4));
            ColorDropDownMenu colorDropDownMenu = new ColorDropDownMenu(val);
            colorDropDownMenu.getMenu().addActionListener(e-> changeGUI(c, (String) colorDropDownMenu.getMenu().getSelectedItem(), type));
            panel.add(colorDropDownMenu.getMenu());
        }
        else{
            isSerializable = false;
        }
    }

    public void changeGUI(Component c, String field, Class<?> type){
        if(type == int.class){
            int value = Integer.parseInt(field);
            c.changeFieldOnGUI(name, value);
        } else if(type == float.class){
            float value = Float.parseFloat(field);
            c.changeFieldOnGUI(name, value);
        }else if(type == String.class){
            String value = field;
            c.changeFieldOnGUI(name, value);
        }else if(type == Color.class){
            Color value = ColorDropDownMenu.colorDictionary.get(field);
            c.changeFieldOnGUI(name, value);
        }
    }

    public JPanel getPanel(){
        return panel;
    }
}
