package renderer.uiElements.panels;

import framework.object.EntityGroup;
import framework.object.Tag;
import input.SelectionEvents;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static framework.data.ImageHandler.loadImage;
import static framework.object.Tag.*;

public class ObjectPanel {

    private final JPanel panel;
    List<JButton> groupButtons = new ArrayList<>();
    JButton physicsButton;
    JButton cameraButton;
    JButton modelButton;

    public JPanel getPanel(){
        return panel;
    }

    public ObjectPanel(){
        panel = new JPanel(new GridLayout(0, 1, 5, 15));
        panel.setBorder(new MatteBorder(0,0,0,1, Color.LIGHT_GRAY));
        SelectionEvents.onCreateGroup.subscribe(this::addGroupButton);
        SelectionEvents.onClearGroups.subscribe(this::removeAllGroupButtons);
        SelectionEvents.onDeleteGroup.subscribe(this::removeGroupButton);
        SelectionEvents.onSelectGroup.subscribe(this::modifyGroupButton);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        removeAllGroupButtons(true);

    }

    private void modifyGroupButton(int index){
        panel.removeAll();
        panel.add(physicsButton);
        panel.add(cameraButton);
        panel.add(modelButton);
        List<JButton> newGroupButtons = new ArrayList<>();
        for(int i =0; i< groupButtons.size(); i++){
            newGroupButtons.add(getGroupButton(i));
        }
        groupButtons.clear();
        groupButtons = newGroupButtons;
        for(JButton button:groupButtons){
            panel.add(button);
        }
        panel.setVisible(false);
        panel.setVisible(true);
        panel.add(Box.createGlue());
    }

    private void removeAllGroupButtons(boolean changeScene){
        panel.removeAll();
        modelButton = null;
        groupButtons.clear();
        physicsButton = getTaggedObjectButton("physics", PHYSICS);
        physicsButton.setToolTipText("Select utilities.Physics Settings");
        cameraButton = getTaggedObjectButton("camera", CAMERA);
        cameraButton.setToolTipText("Select Main Camera");
        modelButton = getTaggedObjectButton("model", MODEL);
        modelButton.setToolTipText("Select Model Settings");
        panel.add(physicsButton);
        panel.add(cameraButton);
        panel.add(modelButton);
        panel.add(Box.createGlue());
    }

    private void removeGroupButton(int index){
        panel.removeAll();
        panel.add(physicsButton);
        panel.add(cameraButton);
        panel.add(modelButton);
        List<JButton> newGroupButtons = new ArrayList<>();
        int idx = 0;
        for(JButton button: groupButtons){
            if(button!= groupButtons.get(index)){
                newGroupButtons.add(getGroupButton(idx));
                idx++;
            }
        }
        groupButtons.clear();
        groupButtons = newGroupButtons;
        for(JButton button:groupButtons){
            panel.add(button);
        }
        panel.setVisible(false);
        panel.setVisible(true);
    }

    private void addGroupButton(int index){
        JButton button = getGroupButton(index);
        groupButtons.add(button);
        panel.add(button);
    }

    private JButton getGroupButton(int index) {
        if(index < 0) return null;
        EntityGroup group = SelectionEvents.groups.get(index);
        JButton button = new JButton();
        button.setHorizontalAlignment(JButton.CENTER);
        button.setBackground(group.color);
        button.setMargin(new Insets(0,0,0,0));
        ImageIcon icon = new ImageIcon(loadImage("group.png"));
        Image image = icon.getImage();
        image = image.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(image));
        button.setFont(new Font("Serif", Font.BOLD, 14));
        button.setText(group.name);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.BOTTOM);
        button.addActionListener(e-> SelectionEvents.selectGroup(index));
        button.setToolTipText("Select Group " + group.name);
        button.setMinimumSize(new Dimension(35,35));
        return button;
    }

    private JButton getTaggedObjectButton(String name, Tag tag) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(35,35));
        if(!name.equals("")) {
            ImageIcon icon = new ImageIcon(loadImage(name + ".png"));
            Image image = icon.getImage();
            image = image.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(image));
        }
        button.addActionListener(e-> SelectionEvents.onTagSelected.invoke(tag));
        return button;
    }
}
