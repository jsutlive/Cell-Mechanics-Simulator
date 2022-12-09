package Renderer.UIElements.Panels;

import Framework.Events.EventHandler;
import Framework.Object.Component;
import Framework.Object.Annotations.DoNotExposeInGUI;
import Framework.Object.Entity;
import Input.SelectionEvents;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class EntityPanel {

    JPanel panel;
    JLabel nameLabel;
    JPanel nameLabelPanel;
    JButton groupButton;
    int currentGroupSelection = -1;

    public static EventHandler<Boolean> onRefresh = new EventHandler<>();
    public JPanel getPanel() {
        return panel;
    }

    public void refresh(Boolean b){
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(2,2,2,2));
        panel.setAutoscrolls(true);
        createBaseLabels();
    }

    public EntityPanel(){
        SelectionEvents.onEntitySelected.subscribe(this::setPanelName);
        SelectionEvents.onSelectGroup.subscribe(this::setIsGroupSelection);
        SelectionEvents.onCreateGroup.subscribe(this::setIsGroupSelection);
        refresh(true);
    }

    private void setIsGroupSelection(int index){
        currentGroupSelection = index;
    }

    private void createBaseLabels() {
        nameLabelPanel = new JPanel();
        nameLabel = new JLabel("Inspector");
        nameLabel.setFont(nameLabel.getFont().deriveFont(18.0f));
        nameLabelPanel.add(nameLabel);

        panel.add(nameLabelPanel);
    }

    public void setPanelName(HashSet<Entity> entities){
        panel.removeAll();
        createBaseLabels();
        if(entities.size()!= 0) {
            if(entities.size()>1){
                setGroupButton(nameLabelPanel, entities);
            }else{
                currentGroupSelection = -1;
            }
            setDeleteButton(nameLabelPanel, entities);
        }
        if(entities.size() == 0) return;
        if(entities.size() == 1) {
            for(Entity e: entities) createSingleEntityPanel(e);
        }else{
            nameLabel.setText(entities.size() + " entities");
            nameLabel.setFont(nameLabel.getFont().deriveFont(18.0f));
            panel.add(new JLabel(""));
            setComponentsMultipleEntity(entities);
        }
    }

    private void createSingleEntityPanel(Entity entity) {
        panel.remove(nameLabel);
        JTextField nameField = new JTextField(entity.name);
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setFont(nameLabel.getFont().deriveFont(18.0f));
        nameField.setMaximumSize(new Dimension(300, 50));
        panel.add(nameField);
        nameField.addActionListener(e->entity.name = nameField.getText());
        setComponentsSingleEntity(entity);
    }

    private void setDeleteButton(JPanel namePanel, HashSet<Entity> entities) {
        JButton deleteButton = new JButton("X");
        deleteButton.setMargin(new Insets(0,0,0,0));
        deleteButton.setFont(new Font("Serif", Font.BOLD, 10));
        deleteButton.setPreferredSize(new Dimension(15, 15));
        if(entities.size()==1) {
            deleteButton.setToolTipText("Delete Entity");
        }else {
            deleteButton.setToolTipText("Delete Entities");
        }
        deleteButton.setBackground(Color.red);
        deleteButton.addActionListener(e -> {
            SelectionEvents.deleteSelection();
        });
        namePanel.add(deleteButton);

    }

    private void setGroupButton(JPanel namePanel, HashSet<Entity> entities){
        groupButton = new JButton("G");
        groupButton.setMargin(new Insets(0,0,0,0));
        groupButton.setFont(new Font("Serif", Font.BOLD, 10));
        groupButton.setPreferredSize(new Dimension(15, 15));

        if(currentGroupSelection < 0) {
            groupButton.setToolTipText("Group Entities");
            groupButton.setBackground(Color.green);
            List<Entity> ent = new ArrayList<>();
            ent.addAll(entities);
            groupButton.addActionListener(e -> {
                SelectionEvents.createGroup(ent);
                setPanelName(entities);
            });
        }else{
            groupButton.setText("U");
            groupButton.setToolTipText("Delete Group");
            groupButton.setBackground(Color.yellow);
            groupButton.addActionListener(e -> {
                SelectionEvents.deleteGroup(currentGroupSelection);
                setPanelName(entities);
            });
        }
        namePanel.add(groupButton);
    }

    private void setComponentsSingleEntity(Entity e) {
        for(Component c: e.getComponents()){
            if(c.getClass().getAnnotation(DoNotExposeInGUI.class) != null) continue;
            ComponentPanel componentPanel = new ComponentPanel(c);
            panel.add(componentPanel.getPanel());
        }
    }

    private void setComponentsMultipleEntity(HashSet<Entity> entities){
        boolean hasDifferentEntityTypes = false;
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
            }else{
                hasDifferentEntityTypes = true;
            }
        }
        if(hasDifferentEntityTypes){
            JPanel warning = new JPanel();
            warning.add(new JLabel("Only common components are editable"));
            panel.add(warning);
        }
    }
}
