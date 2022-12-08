package Renderer.UIElements.Panels;

import Framework.Object.Annotations.DoNotDestroyInGUI;
import Framework.Object.Annotations.DoNotEditWhilePlaying;
import Framework.Object.Component;
import Framework.Object.Entity;
import Input.InputEvents;
import Morphogenesis.Render.DoNotEditInGUI;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class MultiComponentPanel extends ComponentPanel{

    public static String PLACEHOLDER_FIELD = "--";

    public <T extends Component> MultiComponentPanel(List<Entity> entities, Class<T> componentClass) {
        InputEvents.onToggleSimulation.subscribe(this::handleSimulationToggle);
        panel = new JPanel(new GridLayout(0, 1, 0, 5 ));
        panel.setBorder(new BevelBorder(BevelBorder.RAISED));
        Class type = null;
        Object value = null;
        String name = null;


        JPanel namePanel = new JPanel();
        JLabel nameLabel = new JLabel(componentClass.getSimpleName());
        namePanel.add(nameLabel);
        nameLabel.setFont(nameLabel.getFont().deriveFont(14.0f));

        setDeleteButton(entities, componentClass, namePanel);

        panel.add(namePanel);
        setFields(entities, type, value, name, componentClass);
    }

    private <T extends Component> void setDeleteButton(List<Entity> entities, Class<T> componentClass, JPanel namePanel) {
        if(componentClass.getAnnotation(DoNotDestroyInGUI.class)==null) {
            JButton deleteButton = new JButton("X");
            deleteButton.setMargin(new Insets(0,0,0,0));
            deleteButton.setFont(new Font("Serif", Font.BOLD, 10));
            deleteButton.setPreferredSize(new Dimension(15, 15));
            deleteButton.setToolTipText("Delete " + componentClass.getSimpleName());
            deleteButton.setBackground(Color.red);
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
                if(staticFieldPanel.isSerializable)
                    panel.add(staticFieldPanel.getPanel());
            }
            else {
                FieldPanel fieldPanel = new FieldPanel(components, type, value, name);
                if (fieldPanel.isSerializable)
                    panel.add(fieldPanel.getPanel());
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
