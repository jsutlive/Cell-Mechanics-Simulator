package renderer.uiElements.panels;

import framework.object.EntityGroup;
import input.SelectionEvents;
import renderer.uiElements.ColorDropDownMenu;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import java.awt.*;

/**
 * Group Panel: UI element for editing group characteristics, primarily for UI purposes
 */
public class GroupPanel {
    JPanel panel;
    EntityGroup group;
    public JPanel getPanel(){
        return panel;
    }
    public GroupPanel(){
        SelectionEvents.onSelectGroup.subscribe(this::createPanel);
        SelectionEvents.onCreateGroup.subscribe(this::createPanel);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new BevelBorder(BevelBorder.RAISED));
        createPanel(-1);
    }

    private void createPanel(int i){
        if(i > -1){
            group = SelectionEvents.groups.get(i);
            panel.removeAll();
            JPanel nameLabelPanel = new JPanel();
            JLabel nameLabel = new JLabel("Group Settings");
            nameLabel.setFont(nameLabel.getFont().deriveFont(18.0f));
            nameLabelPanel.add(nameLabel);

            panel.add(nameLabelPanel);
            addNameField(i);
            addColorField(i);
            panel.setVisible(true);
        }else{
            panel.removeAll();
            panel.setVisible(false);
        }
    }

    private void addNameField(int i){
        JPanel nameFieldPanel = new JPanel(new GridLayout(1,2));
        nameFieldPanel.setBackground(Color.gray);
        nameFieldPanel.add(new JLabel("Group Name"));
        JTextField field;
        field = new JTextField(group.name);
        field.addActionListener(e -> {
            group.setName(field.getText());
            SelectionEvents.selectGroup(i);
        });
        nameFieldPanel.add(field);
        nameFieldPanel.setBorder(new EmptyBorder(2,5,2,5));
        panel.add(nameFieldPanel);
    }

    private void addColorField(int i){
        JPanel colorFieldPanel = new JPanel(new GridLayout(1,2));
        colorFieldPanel.setBackground(Color.gray);
        ColorDropDownMenu colorDropDownMenu = new ColorDropDownMenu(group.color);
        colorFieldPanel.add(new JLabel("Group color"));
        colorDropDownMenu.getMenu().addActionListener(e->{
            Color value = ColorDropDownMenu.colorDictionary.get(
                    colorDropDownMenu.getMenu().getSelectedItem()
            );
            group.changeGroupColor(value);
            SelectionEvents.selectGroup(i);
        });
        colorFieldPanel.add(colorDropDownMenu.getMenu());
        colorFieldPanel.setBorder(new EmptyBorder(2,5,2,5));
        panel.add(colorFieldPanel);
    }
}
