package Renderer;

import Framework.Object.Component;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class ComponentPanel {

    JPanel panel;
    ArrayList<String> componentNames = new ArrayList<>();

    public JPanel getPanel() {
        return panel;
    }

    public ComponentPanel(Component c) {
        panel = new JPanel(new GridLayout(0, 1, 5, 5 ));
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
            FieldPanel fieldPanel = new FieldPanel(c, type, value, name);
            if(fieldPanel.isSerializable)
                panel.add(fieldPanel.getPanel());
        }

    }

}
