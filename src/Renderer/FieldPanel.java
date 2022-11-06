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
            addRGBpanels(val, c);
        }
        else{
            isSerializable = false;
        }
    }

    private void addRGBpanels(Color originalColor, Component c) {
        JPanel red = new JPanel(new GridLayout(2,0,5, 5));
        JPanel green = new JPanel(new GridLayout(2,0,5, 5));
        JPanel blue = new JPanel(new GridLayout(2,0,5, 5));

        JLabel redLabel = new JLabel("Red");
        redLabel.setPreferredSize(new Dimension(20,20));
        red.add(redLabel);

        JLabel greenLabel = new JLabel("Green");
        greenLabel.setPreferredSize(new Dimension(20,20));
        green.add(greenLabel);

        JLabel blueLabel = new JLabel("Blue");
        blueLabel.setPreferredSize(new Dimension(20,20));
        blueLabel.setPreferredSize(new Dimension(20,20));

        JTextField redField = new JTextField(originalColor.getRed());
        redField.setPreferredSize(new Dimension(20,20));
        JTextField greenField = new JTextField(originalColor.getGreen());
        greenField.setPreferredSize(new Dimension(20,20));
        JTextField blueField = new JTextField(originalColor.getBlue());
        blueField.setPreferredSize(new Dimension(20,20));

        redField.addActionListener(e -> changeGUI(c,
                getColorFromCurrentRGB(
                        Float.parseFloat(redField.getText()),
                        Float.parseFloat(greenField.getText()),
                        Float.parseFloat(blueField.getText()))));
        greenField.addActionListener(e -> changeGUI(c,
                getColorFromCurrentRGB(
                        Float.parseFloat(redField.getText()),
                        Float.parseFloat(greenField.getText()),
                        Float.parseFloat(blueField.getText()))));
        blueField.addActionListener(e -> changeGUI(c,
                getColorFromCurrentRGB(
                        Float.parseFloat(redField.getText()),
                        Float.parseFloat(greenField.getText()),
                        Float.parseFloat(blueField.getText()))));

        red.add(redField);
        blue.add(blueField);
        green.add(greenField);

        panel.add(red);
        panel.add(green);
        panel.add(blue);
    }


    private Color getColorFromCurrentRGB(float r, float g, float b){
        return new Color(r, g, b, 1);
    }

    public void changeGUI(Component c, Color color){
        c.changeFieldOnGUI(name, color);
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
        }
    }

    public JPanel getPanel(){
        return panel;
    }
}
