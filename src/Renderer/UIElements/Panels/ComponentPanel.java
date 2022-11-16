package Renderer.UIElements.Panels;

import Framework.Object.Annotations.DoNotEditWhilePlaying;
import Framework.Object.Component;
import Framework.Object.Annotations.DoNotDestroyInGUI;
import Framework.States.EditorState;
import Framework.States.State;
import Morphogenesis.Components.Render.DoNotEditInGUI;

import javax.swing.*;
import javax.swing.border.BevelBorder;
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
        panel.setBorder(new BevelBorder(BevelBorder.RAISED));
        Class type = null;
        Object value = null;
        String name = null;
        Class componentClass = c.getClass();


        JPanel namePanel = new JPanel();
        namePanel.add(new JLabel(componentClass.getSimpleName()));

        if(c.getClass().getAnnotation(DoNotDestroyInGUI.class)==null) {
            JButton deleteButton = new JButton("X");
            deleteButton.setMargin(new Insets(0,0,0,0));
            deleteButton.setFont(new Font("Serif", Font.BOLD, 10));
            deleteButton.setPreferredSize(new Dimension(15, 15));
            deleteButton.setToolTipText("Delete " + c.getClass().getSimpleName());
            deleteButton.setBackground(Color.red);
            deleteButton.addActionListener(e -> c.removeSelf());
            namePanel.add(deleteButton);
        }

        panel.add(namePanel);



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
            try {
                if(f.getDeclaredAnnotation(DoNotEditInGUI.class)!= null ||
                        (EditorState.class.isAssignableFrom(State.GetState().getClass()) &&
                                f.getDeclaredAnnotation(DoNotEditWhilePlaying.class) != null)){
                    StaticFieldPanel staticFieldPanel = new StaticFieldPanel(c, type, value, name);
                    if(staticFieldPanel.isSerializable)
                        panel.add(staticFieldPanel.getPanel());
                }
                else {
                    FieldPanel fieldPanel = new FieldPanel(c, type, value, name);
                    if (fieldPanel.isSerializable)
                        panel.add(fieldPanel.getPanel());
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

}
