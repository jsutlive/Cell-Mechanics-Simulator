package Renderer;

import Framework.Object.Entity;
import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Components.MouseSelector;

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
        panel = new JPanel(new GridLayout(5,1));
        panel.setBorder(new BevelBorder(BevelBorder.RAISED));
        panel.setBackground(Color.gray);
        nameLabel = new JLabel("TEST LABEL");
        restingAreaLabel = new JLabel("RESTING AREA");
        areaLabel = new JLabel("CURRENT AREA");
        panel.add(nameLabel);
        panel.add(restingAreaLabel);
        panel.add(areaLabel);

        MouseSelector.onEntitySelected.subscribe(this::setPanelName);
    }

    public void setPanelName(Entity e){
        nameLabel.setText("ENTITY " + e.getStateID());
        Mesh mesh = e.getComponent(Mesh.class);
        if(mesh!=null){
            restingAreaLabel.setText("Resting area: " + mesh.restingArea);
            areaLabel.setText("Current area: " + mesh.getArea());
        }
    }


}
