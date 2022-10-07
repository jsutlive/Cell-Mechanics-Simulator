package Renderer;

import Framework.Object.Component;
import Framework.Object.Entity;
import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Components.MouseSelector;
import Morphogenesis.Components.Physics.Force;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class ComponentPanel {

    JPanel panel;
    JLabel nameLabel;
    JLabel restingAreaLabel;
    JLabel areaLabel;

    public JPanel getPanel() {
        return panel;
    }

    public ComponentPanel(){
        panel = new JPanel(new GridLayout(7,0));
        panel.setBorder(new BevelBorder(BevelBorder.RAISED));
        panel.setBackground(Color.gray);
        createBaseLabels();

        MouseSelector.onEntitySelected.subscribe(this::setPanelName);
    }

    private void createBaseLabels() {
        nameLabel = new JLabel("TEST LABEL");
        restingAreaLabel = new JLabel("RESTING AREA");
        areaLabel = new JLabel("CURRENT AREA");
        panel.add(nameLabel);
        panel.add(restingAreaLabel);
        panel.add(areaLabel);
    }

    public void setPanelName(Entity e){
        panel.removeAll();
        createBaseLabels();
        nameLabel.setText("ENTITY " + e.getStateID());
        Mesh mesh = e.getComponent(Mesh.class);
        if(mesh!=null){
            restingAreaLabel.setText("Resting area: " + mesh.restingArea);
            areaLabel.setText("Current area: " + mesh.getArea());
        }
        panel.add(new JLabel(""));
        for(Component c: e.getComponents()){
            if(Force.class.isAssignableFrom(c.getClass()))
                panel.add(new JLabel(c.getClass().getSimpleName()));
        }
    }


}
