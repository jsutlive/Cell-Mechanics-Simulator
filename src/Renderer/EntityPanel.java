package Renderer;

import Framework.Object.Component;
import Framework.Object.DoNotExposeInGUI;
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
        nameLabel = new JLabel("TEST LABEL", SwingConstants.CENTER);
        panel.add(nameLabel);
    }

    public void setPanelName(Entity e){
        SelectionEvents.selectedEntity = e;
        panel.removeAll();
        createBaseLabels();
        nameLabel.setText(e.name);

        panel.add(new JLabel(""));
        setComponents(e);
    }

    private void setComponents(Entity e) {
        for(Component c: e.getComponents()){
            if(c.getClass().getAnnotation(DoNotExposeInGUI.class) != null) continue;
            ComponentPanel componentPanel = new ComponentPanel(c);
            panel.add(componentPanel.getPanel());
        }
    }


}
