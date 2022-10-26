package Renderer;

import Framework.Object.Component;
import Morphogenesis.Components.Render.DoNotEditInGUI;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ComponentPanel {

    JPanel panel;
    public JPanel getPanel() {
        return panel;
    }

    public ComponentPanel(Component c) {
        panel = new JPanel(new GridLayout(0, 1, 0, 5 ));
        Class type = null;
        Object value = null;
        String name = null;
        Class componentClass = c.getClass();
        panel.add(new JLabel(componentClass.getSimpleName()));
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
            if(f.getDeclaredAnnotation(DoNotEditInGUI.class)!= null){
                StaticFieldPanel staticFieldPanel = new StaticFieldPanel(c, type, value, name);
                if(staticFieldPanel.isSerializable)
                    panel.add(staticFieldPanel.getPanel());
            }
            else {
                FieldPanel fieldPanel = new FieldPanel(c, type, value, name);
                if (fieldPanel.isSerializable)
                    panel.add(fieldPanel.getPanel());
            }

        }
        addDeleteButton(c);

    }
    public void addDeleteButton(Component component){
        JButton deleteButton = new JButton("X");
        deleteButton.setPreferredSize(new Dimension(20, 20));
        deleteButton.setBackground(Color.red);
        deleteButton.addActionListener(e -> component.removeSelf());
        panel.add(deleteButton);
    }

}
