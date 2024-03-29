package renderer.uiElements.panels;

import framework.object.annotations.DoNotDestroyInGUI;
import framework.object.annotations.DoNotEditWhilePlaying;
import component.Component;
import framework.object.Entity;
import input.InputEvents;
import annotations.DoNotEditInGUI;
import utilities.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiComponentPanel extends ComponentPanel{

    public static String PLACEHOLDER_FIELD = "--";

    public <T extends Component> MultiComponentPanel(List<Entity> entities, Class<T> componentClass) {
        InputEvents.onToggleSimulation.subscribe(this::handleSimulationToggle);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.white);
        Class type = null;
        Object value = null;
        String name = null;


        JPanel namePanel = new JPanel();
        setEnableButton(entities, componentClass, namePanel);
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
        namePanel.setBorder(new EmptyBorder(4,25,2,25));
        namePanel.setBackground(Color.decode("#5774b7"));
        JLabel nameLabel = new JLabel(StringUtils.splitPascalCase(componentClass.getSimpleName()));
        JLabel nameIcon = new JLabel("");
        nameIcon.setIcon(getComponentIcon(componentClass));
        namePanel.add(nameIcon);
        namePanel.add(Box.createVerticalStrut(30));
        namePanel.add(nameLabel);
        nameLabel.setForeground(Color.white);
        nameLabel.setFont(nameLabel.getFont().deriveFont(Collections.singletonMap(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD)).deriveFont(18f));
        namePanel.add(Box.createHorizontalGlue());
        setDeleteButton(entities, componentClass, namePanel);
        namePanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 35));


        panel.add(namePanel);
        setFields(entities, type, value, name, componentClass);
        panel.setPreferredSize(new Dimension(300, panel.getPreferredSize().height));
    }

    private <T extends Component> void setEnableButton(List<Entity> entities, Class<T> componentClass, JPanel namePanel){
        JCheckBox checkBox = new JCheckBox();
        for(Entity entity: entities){
            checkBox.addActionListener(e -> entity.getComponent(componentClass).setEnabled(checkBox.isSelected()));
            if(entity.getComponent(componentClass).isEnabled()) {
                checkBox.setSelected(true);
                continue;
            }
            checkBox.setSelected(false);
            break;
        }
        checkBox.setToolTipText("Enable/Disable");
        namePanel.add(checkBox);
    }


    private <T extends Component> void setDeleteButton(List<Entity> entities, Class<T> componentClass, JPanel namePanel) {
        if(componentClass.getAnnotation(DoNotDestroyInGUI.class)==null) {
            JButton deleteButton = new JButton("X");
            deleteButton.setMargin(new Insets(0,0,0,0));
            deleteButton.setFont(new Font("Serif", Font.BOLD, 10));
            deleteButton.setPreferredSize(new Dimension(15, 15));
            deleteButton.setToolTipText("Delete " + componentClass.getSimpleName());
            deleteButton.setBackground(Color.red);
            if(entities == null) return;
            for(Entity entity: entities) {
                deleteButton.addActionListener(e -> entity.getComponent(componentClass).removeSelf());
            }
            namePanel.add(deleteButton);
        }
    }

    protected void setFields(List<Entity> entities, Class type, Object value, String name, Class componentClass) {
        List<Component> components = new ArrayList<>();
        for(Entity e: entities){
            components.add(e.getComponent(componentClass));
        }
        for(Field f : componentClass.getFields()){
            if(Modifier.isTransient(f.getModifiers())){
                f.setAccessible(true);
            }else if (Modifier.isProtected(f.getModifiers())){
                f.setAccessible(true);
            }
            try {
                type = f.getType();
                value = checkValues(components, type, f);
                name = f.getName();
            }
            catch (IllegalAccessException e){
                e.printStackTrace();
            }
            if(f.getDeclaredAnnotation(DoNotEditInGUI.class)!= null ||
                    (isPlaying && f.getDeclaredAnnotation(DoNotEditWhilePlaying.class) != null)){
                StaticFieldPanel staticFieldPanel = new StaticFieldPanel(type, value, name);
                if(staticFieldPanel.isSerializable) {
                    panel.add(staticFieldPanel.getPanel());
                }
            }
            else {
                FieldPanel fieldPanel = new FieldPanel(components, type, value, name);
                if (fieldPanel.isSerializable) {
                    panel.add(fieldPanel.getPanel());
                }
            }
        }
    }

    private Object checkValues(List<Component> components, Class type, Field f) throws IllegalAccessException {
        if(type == int.class){
            int testVal = (int)f.get(components.get(0));
            for(Component c: components) {
                if((int)f.get(c) != testVal){
                    return PLACEHOLDER_FIELD;
                }
            }
            return testVal;
        }
        if(type == String.class){
            String testVal = (String)f.get(components.get(0));
            for(Component c: components) {
                if(f.get(c) != testVal){
                    return PLACEHOLDER_FIELD;
                }
            }
            return testVal;
        }
        if(type == float.class){
            float testVal = (float)f.get(components.get(0));
            for(Component c: components) {
                if((float)f.get(c) != testVal){
                    return PLACEHOLDER_FIELD;
                }
            }
            return testVal;
        }
        if(type == Color.class){
            Color testVal = (Color)f.get(components.get(0));
            for(Component c: components) {
                if(f.get(c) != testVal){
                    return PLACEHOLDER_FIELD;
                }
            }
            return testVal;
        }
        return PLACEHOLDER_FIELD;
    }
}
