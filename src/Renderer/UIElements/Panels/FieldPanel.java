package Renderer.UIElements.Panels;

import Component.BatchManager;
import Component.Component;
import Input.SelectionEvents;
import Renderer.UIElements.ColorDropDownMenu;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Geometry.Vector.Vector2i;
import Utilities.StringUtils;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(400, 40));
        panel.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.decode("#e4e4e4")),new EmptyBorder(4,25,2,25)));
        this.name = name;
        if(type == int.class){
            panel.add(new JLabel(StringUtils.splitCamelCase(name)));
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
            panel.add(new JLabel(StringUtils.splitCamelCase(name)));
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

        }
        else if(type == boolean.class) {
            panel.add(new JLabel(StringUtils.splitCamelCase(name)));
            boolean val;
            JCheckBox checkBox = new JCheckBox("");
            if(value != PLACEHOLDER_FIELD) {
                val = (boolean) value;
                checkBox.setSelected(val);
            }
            for(Component c: components) checkBox.addActionListener(e -> changeGUI(c, Boolean.toString(checkBox.isSelected()), type));
            panel.add(checkBox);
        }
        else if(type == String.class){
            panel.add(new JLabel(StringUtils.splitCamelCase(name)));
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
        } else if(type == Color.class){
            Color val;
            ColorDropDownMenu colorDropDownMenu;
            if(value!=PLACEHOLDER_FIELD) {
                val = (Color) value;
                colorDropDownMenu = new ColorDropDownMenu(val);
            }else{
                colorDropDownMenu = new ColorDropDownMenu();
            }
            panel.add(new JLabel(StringUtils.splitCamelCase(name)));
            for(Component c: components)
                colorDropDownMenu.getMenu().addActionListener(e->
                        changeGUI(c, (String) Objects.requireNonNull(colorDropDownMenu.getMenu().getSelectedItem()), type));
            panel.add(colorDropDownMenu.getMenu());
        }
        else if(type == Vector2i.class){
            panel.setLayout(new GridLayout(0, 5));
            panel.add(new JLabel(StringUtils.splitCamelCase(name)));
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
            panel.add(new JLabel(StringUtils.splitCamelCase(name)));
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
        else if(type == File.class){
            if(value == null) {
                panel.add(new JLabel("No File"));
                JButton fileSelect = new JButton("Select Batch File");
                for(Component c: components) {
                    fileSelect.addActionListener(e -> {
                        getFileFromParams(c);
                        SelectionEvents.refresh();
                    });
                }
                panel.add(fileSelect);
            }else{
                File val = (File)value;
                panel.add(new JLabel(val.getPath()));
                JButton fileSelect = new JButton("Change Batch File");
                for(Component c: components) {
                    fileSelect.addActionListener(e -> {
                        getFileFromParams(c);
                        SelectionEvents.refresh();
                    });
                }
                panel.add(fileSelect);
            }
        } else{
            isSerializable = false;

        }
    }

    private void getFileFromParams(Component c){
        if(! (c instanceof BatchManager)) return;
        JFileChooser chooser = new JFileChooser();

        // Create and set file extension filters, we'll mainly be importing csv and text files
        FileNameExtensionFilter textFilter = new FileNameExtensionFilter("*.txt", "txt");
        FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("*.csv", "csv");
        chooser.addChoosableFileFilter(textFilter);
        chooser.addChoosableFileFilter(csvFilter);
        chooser.setFileFilter(csvFilter);

        chooser.setDialogTitle("Select File");
        int choice = chooser.showOpenDialog(null);
        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            c.changeFieldOnGUI(name, file);
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
        if(field.equals(PLACEHOLDER_FIELD)) return;
        if(type == int.class){
            int value = Integer.parseInt(field);
            c.changeFieldOnGUI(name, value);
        } else if(type == float.class){
            float value = Float.parseFloat(field);
            c.changeFieldOnGUI(name, value);
        }else if(type == boolean.class){
            boolean value = Boolean.parseBoolean(field);
            c.changeFieldOnGUI(name, value);
        }else if(type == String.class){
            c.changeFieldOnGUI(name, field);
        }else if(type == Color.class){
            Color value = ColorDropDownMenu.colorDictionary.get(field);
            c.changeFieldOnGUI(name, value);
        }
    }

    public JPanel getPanel(){
        return panel;
    }
}
