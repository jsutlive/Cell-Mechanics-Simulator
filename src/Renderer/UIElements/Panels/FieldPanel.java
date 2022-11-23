package Renderer.UIElements.Panels;

import Framework.Object.Component;
import Framework.Object.Entity;
import Input.SelectionEvents;
import Renderer.UIElements.ColorDropDownMenu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static Renderer.UIElements.Panels.MultiComponentPanel.PLACEHOLDER_FIELD;

public class FieldPanel {

    JPanel panel;
    public boolean isSerializable = true;
    public String name;

    public FieldPanel(List<Component> components, Class<?> type, Object value, String name ){
        initialize(components, type, value, name);
    }

    public FieldPanel(Component c, Class<?> type, Object value, String name){
        List<Component> components = new ArrayList<>();
        components.add(c);
        initialize(components, type, value, name);
    }

    private void initialize(List<Component> components, Class<?> type, Object value, String name) {
        panel = new JPanel(new GridLayout(0, 2));
        panel.setBorder(new EmptyBorder(2,5,2,5));
        panel.setBackground(Color.lightGray);
        this.name = name;
        if(type == int.class){
            panel.add(new JLabel(name));
            int val;
            JTextField field;
            if(value!=PLACEHOLDER_FIELD) {
                val = (int) value;
                field = new JTextField(String.valueOf(val));
            }else{
                field = new JTextField((String)value);
            }
            for(Component c: components) field.addActionListener(e -> changeGUI(c, field.getText(), type));
            panel.add(field);
        }else if(type == float.class){
            panel.add(new JLabel(name));
            float val;
            JTextField field;
            if(value!=PLACEHOLDER_FIELD) {
                val = (float) value;
                field = new JTextField(String.valueOf(val));
            }else{
                field = new JTextField((String)value);
            }
            for(Component c: components) field.addActionListener(e -> changeGUI(c, field.getText(), type));
            panel.add(field);
        }else if(type == String.class){
            panel.add(new JLabel(name));
            String val;
            JTextField field;
            if(value!=PLACEHOLDER_FIELD) {
                val = (String) value;
                field = new JTextField(val);
            }else{
                field = new JTextField((String)value);
            }
            for(Component c: components) field.addActionListener(e -> changeGUI(c, field.getText(), type));
            panel.add(field);
        }
        else if(type == Color.class){
            Color val;
            ColorDropDownMenu colorDropDownMenu;
            if(value!=PLACEHOLDER_FIELD) {
                val = (Color) value;
                colorDropDownMenu = new ColorDropDownMenu(val);
            }else{
                colorDropDownMenu = new ColorDropDownMenu();
            }
            panel.add(new JLabel(name));
            for(Component c: components)
                colorDropDownMenu.getMenu().addActionListener(e->
                        changeGUI(c, (String) colorDropDownMenu.getMenu().getSelectedItem(), type));
            panel.add(colorDropDownMenu.getMenu());
        }
        else{
            isSerializable = false;
        }
    }

    public void changeGUI(Component c, String field, Class<?> type){
        if(field == PLACEHOLDER_FIELD) return;
        if(type == int.class){
            int value = Integer.parseInt(field);
            c.changeFieldOnGUI(name, value);
        } else if(type == float.class){
            float value = Float.parseFloat(field);
            c.changeFieldOnGUI(name, value);
        }else if(type == String.class){
            c.changeFieldOnGUI(name, field);
        }else if(type == Color.class){
            Color value = ColorDropDownMenu.colorDictionary.get(field);
            c.changeFieldOnGUI(name, value);
        }
        EntityPanel.onRefresh.invoke(true);
        SelectionEvents.refresh();
    }

    public JPanel getPanel(){
        return panel;
    }
}
