package Renderer.UIElements.Panels;

import Framework.Object.Annotations.DoNotEditWhilePlaying;
import Framework.Object.Component;
import Framework.Object.Annotations.DoNotDestroyInGUI;
import Framework.States.EditorState;
import Framework.States.State;
import Input.InputEvents;
import Input.SelectionEvents;
import Morphogenesis.Components.Physics.CellGroups.GroupSelector;
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
    protected boolean isPlaying;

    public ComponentPanel(){}
    public ComponentPanel(Component c) {
        InputEvents.onToggleSimulation.subscribe(this::handleSimulationToggle);
        panel = new JPanel(new GridLayout(0, 1, 0, 5 ));
        panel.setBorder(new BevelBorder(BevelBorder.RAISED));
        Class type = null;
        Object value = null;
        String name = null;
        Class componentClass = c.getClass();


        JPanel namePanel = new JPanel();
        namePanel.add(new JLabel(componentClass.getSimpleName()));

        setSelectButton(c, namePanel);
        setDeleteButton(c, namePanel);

        panel.add(namePanel);
        setFields(c, type, value, name, componentClass);
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
            deleteButton.addActionListener(e -> {
                SelectionEvents.onSelectionButtonPressed.invoke(c);
            });
            namePanel.add(deleteButton);
        }
    }

    private void setDeleteButton(Component c, JPanel namePanel) {
        if(c.getClass().getAnnotation(DoNotDestroyInGUI.class)==null) {
            JButton deleteButton = new JButton("X");
            deleteButton.setMargin(new Insets(0,0,0,0));
            deleteButton.setFont(new Font("Serif", Font.BOLD, 10));
            deleteButton.setPreferredSize(new Dimension(15, 15));
            deleteButton.setToolTipText("Delete " + c.getClass().getSimpleName());
            deleteButton.setBackground(Color.red);
            deleteButton.addActionListener(e -> {
                c.removeSelf();
                SelectionEvents.refresh();
            });
            namePanel.add(deleteButton);
        }
    }

    private void setFields(Component c, Class type, Object value, String name, Class componentClass) {
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
                if(staticFieldPanel.isSerializable)
                    panel.add(staticFieldPanel.getPanel());
            }
            else {
                FieldPanel fieldPanel = new FieldPanel(c, type, value, name);
                if (fieldPanel.isSerializable)
                    panel.add(fieldPanel.getPanel());
            }


        }
    }

}
