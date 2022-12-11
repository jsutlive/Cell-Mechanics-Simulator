package Renderer.UIElements.Panels;

import Framework.Object.Component;
import Framework.Object.Entity;
import Input.SelectionEvents;
import Renderer.UIElements.ColorDropDownMenu;
import Renderer.UIElements.SetSlider;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Geometry.Vector.Vector2i;

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
        panel.setMaximumSize(new Dimension(325, 50));
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
            if (value != PLACEHOLDER_FIELD) {
                val = (float) value;
                field = new JTextField(String.valueOf(val));
            } else {
                field = new JTextField((String) value);
            }
            for (Component c : components) field.addActionListener(e -> changeGUI(c, field.getText(), type));
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
        else if(type == Vector2i.class){
            panel.setLayout(new GridLayout(0, 5));
            panel.add(new JLabel(name));
            Vector2i val;
            JTextField fieldX,fieldY;
            if(value!=PLACEHOLDER_FIELD) {
                val = (Vector2i) value;
                fieldX = new JTextField(String.valueOf(val.x));
                fieldY = new JTextField(String.valueOf(val.y));

            }else{
                fieldX = new JTextField((String)value);
                fieldY = new JTextField((String)value);
            }
            for(Component c: components) {
                fieldX.addActionListener(e -> changeGUIVec(c, new String[]{fieldX.getText(), fieldY.getText()}, type));
            }
            fieldX.setHorizontalAlignment(JTextField.CENTER);
            fieldY.setHorizontalAlignment(JTextField.CENTER);
            panel.add(fieldX);
            panel.add(new JLabel("X"));
            panel.add(fieldY);
            panel.add(new JLabel("Y"));
        }
        else if(type == Vector2f.class){
            panel.setLayout(new GridLayout(0, 5));
            panel.add(new JLabel(name));
            Vector2f val;
            JTextField fieldX,fieldY;
            if(value!=PLACEHOLDER_FIELD) {
                val = (Vector2f) value;
                fieldX = new JTextField(String.valueOf(val.x));
                fieldY = new JTextField(String.valueOf(val.y));

            }else{
                fieldX = new JTextField((String)value);
                fieldY = new JTextField((String)value);
            }
            for(Component c: components) {
                fieldX.addActionListener(e -> changeGUIVec(c, new String[]{fieldX.getText(), fieldY.getText()}, type));
            }
            fieldX.setHorizontalAlignment(JTextField.CENTER);
            fieldY.setHorizontalAlignment(JTextField.CENTER);
            panel.add(fieldX);
            panel.add(new JLabel("X"));
            panel.add(fieldY);
            panel.add(new JLabel("Y"));
        }
        else{
            isSerializable = false;
        }
    }

    public void changeGUIVec(Component c, String[] field, Class<?> type){
        if(type == Vector2i.class){
            int x = Integer.parseInt(field[0]);
            int y = Integer.parseInt(field[1]);
            c.changeFieldOnGUI(name, new Vector2i(x, y));
        }
        else if(type == Vector2f.class){
            float x = Float.parseFloat(field[0]);
            float y = Float.parseFloat(field[1]);
            c.changeFieldOnGUI(name, new Vector2f(x, y));
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
        SelectionEvents.refresh();
    }

    public JPanel getPanel(){
        return panel;
    }
}
