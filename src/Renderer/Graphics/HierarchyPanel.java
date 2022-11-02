package Renderer.Graphics;

import Framework.Object.Entity;
import Framework.States.State;
import Input.InputEvents;
import Input.SelectionEvents;

import javax.swing.*;
import java.awt.*;

public class HierarchyPanel {

    private JPanel panel;

    public JPanel getPanel(){
        return panel;
    }

    public HierarchyPanel(){
        panel = new JPanel(new GridLayout(0, 1, 5, 5));
        State.onAddEntity.subscribe(this::addEntityLabel);

    }

    public void addEntityLabel(Entity entity) {
        JButton button = new JButton(entity.name);
        button.addActionListener(e -> SelectionEvents.selectedEntity = entity);
        panel.add(button);
    }
}
