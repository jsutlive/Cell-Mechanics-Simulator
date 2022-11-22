package Renderer.UIElements.Panels;

import Framework.Object.Component;
import Framework.Object.Annotations.DoNotExposeInGUI;
import Framework.Object.Entity;
import Input.SelectionEvents;
import Morphogenesis.Components.MouseSelector;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

        SelectionEvents.onEntitySelected.subscribe(this::setPanelName);
    }

    private void createBaseLabels() {
        nameLabel = new JLabel("Inspector", SwingConstants.CENTER);
        panel.add(nameLabel);
    }

    public void setPanelName(HashSet<Entity> entities){
        panel.removeAll();
        createBaseLabels();
        if(entities.size() == 1) {
            for(Entity e: entities) createSingleEntityPanel(e);
        }else{
            nameLabel.setText(entities.size() + " entities");
            panel.add(new JLabel(""));
            setComponentsMultipleEntity(entities);
        }
    }

    private void createSingleEntityPanel(Entity e) {
        nameLabel.setText(e.name);
        panel.add(new JLabel(""));
        setComponentsSingleEntity(e);
    }

    private void setComponentsSingleEntity(Entity e) {
        for(Component c: e.getComponents()){
            if(c.getClass().getAnnotation(DoNotExposeInGUI.class) != null) continue;
            ComponentPanel componentPanel = new ComponentPanel(c);
            panel.add(componentPanel.getPanel());
        }
    }

    private void setComponentsMultipleEntity(HashSet<Entity> entities){
        List<Entity> e = new ArrayList<>(entities);

        for(Component c: e.get(0).getComponents()){
            boolean makePanel = true;
            if(c.getClass().getAnnotation(DoNotExposeInGUI.class) != null) continue;
            for(int i = 1; i < e.size(); i++) {
                if(e.get(i).getComponent(c.getClass()) == null) {
                    makePanel = false;
                    break;
                }
            }
            if(makePanel) {
                ComponentPanel multiPanel = new MultiComponentPanel(e,c.getClass());
                panel.add(multiPanel.getPanel());
            }


        }

    }


}
