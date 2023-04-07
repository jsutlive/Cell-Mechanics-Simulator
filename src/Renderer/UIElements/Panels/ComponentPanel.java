package Renderer.UIElements.Panels;

import Framework.Object.Annotations.DoNotEditWhilePlaying;
import Component.Component;
import Component.*;
import Framework.Object.Annotations.DoNotDestroyInGUI;
import Framework.Object.Transform;
import Input.InputEvents;
import Input.SelectionEvents;
import Annotations.GroupSelector;
import Annotations.DoNotEditInGUI;
import Utilities.StringUtils;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;

import static Framework.Data.ImageHandler.loadImage;

public class ComponentPanel {

    JPanel panel;
    private int numFields;
    public JPanel getPanel() {
        return panel;
    }
    protected boolean isPlaying;

    public ComponentPanel(){}
    public ComponentPanel(Component c) {
        InputEvents.onToggleSimulation.subscribe(this::handleSimulationToggle);
        c.onComponentChanged.subscribe(this::refreshPanel);
        refreshPanel(c);
    }

    private void refreshPanel(Component c) {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.white);
        Class<?> type = null;
        Object value = null;
        String name = null;
        Class<?> componentClass = c.getClass();

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
        namePanel.setBorder(new EmptyBorder(4,25,3,25));
        namePanel.setBackground(Color.decode("#5774b7"));
        JLabel nameLabel = new JLabel(StringUtils.splitPascalCase(c.getClass().getSimpleName()));
        JLabel nameIcon = new JLabel("");
        nameIcon.setIcon(getComponentIcon(c));
        namePanel.add(nameIcon);
        namePanel.add(Box.createVerticalStrut(30));
        namePanel.add(nameLabel);
        nameLabel.setForeground(Color.white);
        nameLabel.setFont(nameLabel.getFont().deriveFont(Collections.singletonMap(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD)).deriveFont(18f));

        namePanel.add(Box.createHorizontalGlue());

        setSelectButton(c, namePanel);
        setDeleteButton(c, namePanel);
        namePanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));

        panel.add(namePanel);
        setFields(c, type, value, name, componentClass);
        panel.setPreferredSize(new Dimension(300, panel.getPreferredSize().height));
    }

    private ImageIcon getComponentIcon(Component c) {
        ImageIcon icon;
        if(Force.class.isAssignableFrom(c.getClass())) {
            icon = new ImageIcon(loadImage("force.png"));
        }else if(Mesh.class.isAssignableFrom(c.getClass())){
            icon = new ImageIcon(loadImage("mesh.png"));
        }else if(Camera.class.isAssignableFrom(c.getClass())){
            icon = new ImageIcon(loadImage("camera.png"));
        }else if(ObjectRenderer.class.isAssignableFrom(c.getClass())){
            icon = new ImageIcon(loadImage("paint.png"));
        }else if(SaveSystem.class.isAssignableFrom(c.getClass())){
            icon = new ImageIcon(loadImage("save.png"));
        }else if(Transform.class.isAssignableFrom(c.getClass())){
            icon = new ImageIcon(loadImage("axis.png"));
        }else{
            icon = new ImageIcon(loadImage("code.png"));
        }
        Image image = icon.getImage();
        image = image.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    protected void handleSimulationToggle(Boolean b){
        isPlaying = b;
    }

    private void setSelectButton(Component c, JPanel namePanel){
        if(c.getClass().getAnnotation(GroupSelector.class)!=null) {
            JButton deleteButton = new JButton("S");
            deleteButton.setMargin(new Insets(0,0,0,0));
            deleteButton.setFont(new Font("Serif", Font.BOLD, 10));
            deleteButton.setPreferredSize(new Dimension(15, 15));
            deleteButton.setToolTipText("Select all in group");
            deleteButton.setBackground(Color.cyan);
            deleteButton.addActionListener(e -> SelectionEvents.onSelectionButtonPressed.invoke(c));
            namePanel.add(deleteButton);
        }
    }

    private void setDeleteButton(Component c, JPanel namePanel) {
        if(c.getClass().getAnnotation(DoNotDestroyInGUI.class)==null) {
            JButton deleteButton = new JButton("");
            ImageIcon icon = new ImageIcon(loadImage("close.png"));
            Image image = icon.getImage();
            image = image.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            deleteButton.setIcon(new ImageIcon(image));
            deleteButton.setMargin(new Insets(0,0,0,0));
            deleteButton.setFont(new Font("Serif", Font.BOLD, 10));
            deleteButton.setPreferredSize(new Dimension(30, 30));
            deleteButton.setToolTipText("Delete " + c.getClass().getSimpleName());
            deleteButton.setBackground(Color.black);
            deleteButton.addActionListener(e -> {
                c.removeSelf();
                SelectionEvents.refresh();
            });
            namePanel.add(deleteButton);
        }
    }

    private void setFields(Component c, Class<?> type, Object value, String name, Class<?> componentClass) {
        for(Field f : componentClass.getFields()){
            if(Modifier.isTransient(f.getModifiers())){
                f.setAccessible(true);
            }else if (Modifier.isProtected(f.getModifiers())){
                f.setAccessible(true);
            }
            try {
                type = f.getType();
                value = f.get(c);
                name = f.getName();
            }
            catch (IllegalAccessException e){
                e.printStackTrace();
            }

            if(f.getDeclaredAnnotation(DoNotEditInGUI.class)!= null ||
                    (isPlaying && f.getDeclaredAnnotation(DoNotEditWhilePlaying.class) != null)){
                StaticFieldPanel staticFieldPanel = new StaticFieldPanel(type, value, name);
                if(staticFieldPanel.isSerializable){

                    panel.add(staticFieldPanel.getPanel());
                    numFields++;
                }
            }
            else {
                FieldPanel fieldPanel = new FieldPanel(c, type, value, name);
                if (fieldPanel.isSerializable) {
                    panel.add(fieldPanel.getPanel());
                    numFields++;
                }
            }
        }
    }

}
