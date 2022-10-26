package Renderer;

import Framework.Object.Component;
import Framework.Object.Entity;
import Input.SelectionEvents;
import Morphogenesis.Components.MouseSelector;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EntityPanel {

    JPanel panel;
    JLabel nameLabel;

    public JPanel getPanel() {
        return panel;
    }

    public EntityPanel(){
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(2,2,2,2));
        panel.setAutoscrolls(true);
        createBaseLabels();

        MouseSelector.onEntitySelected.subscribe(this::setPanelName);
    }

    private void createBaseLabels() {
        nameLabel = new JLabel("TEST LABEL");
        panel.add(nameLabel);
    }

    public void setPanelName(Entity e){
        SelectionEvents.selectedEntity = e;
        panel.removeAll();
        createBaseLabels();
        nameLabel.setText("ENTITY " + e.getStateID());
        /*Mesh mesh = e.getComponent(Mesh.class);
        if(mesh!=null){
            restingAreaLabel.setText("Resting area: " + mesh.restingArea);
            areaLabel.setText("Current area: " + mesh.getArea());
        }*/
        panel.add(new JLabel(""));
        setComponents(e);
    }

    private void setComponents(Entity e) {
        for(Component c: e.getComponents()){
            ComponentPanel componentPanel = new ComponentPanel(c);
            panel.add(componentPanel.getPanel());
            /*if(Force.class.isAssignableFrom(c.getClass()))
                panel.add(new JLabel(c.getClass().getSimpleName()));*/
        }
    }


}
