package Renderer.UIElements.Panels;

import Framework.Object.Tag;
import Input.SelectionEvents;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static Framework.Data.ImageHandler.loadImage;
import static Framework.Object.Tag.CAMERA;
import static Framework.Object.Tag.MODEL;

public class ObjectPanel {

    private final JPanel panel;
    List<JButton> groupButtons = new ArrayList<>();

    public JPanel getPanel(){
        return panel;
    }

    public ObjectPanel(){
        panel = new JPanel(new GridLayout(0, 1, 5, 15));
        SelectionEvents.onCreateGroup.subscribe(this::addGroupButton);
        SelectionEvents.onClearGroups.subscribe(this::removeAllGroupButtons);
        SelectionEvents.onDeleteGroup.subscribe(this::removeGroupButton);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new BevelBorder(BevelBorder.RAISED));
        removeAllGroupButtons(true);
    }

    private void removeAllGroupButtons(boolean changeScene){
        panel.removeAll();
        groupButtons.clear();
        JButton physicsButton = getTaggedObjectButton("physics", MODEL);
        physicsButton.setToolTipText("Select Group Physics");
        JButton cameraButton = getTaggedObjectButton("camera", CAMERA);
        cameraButton.setToolTipText("Select Main Camera");
        panel.add(physicsButton);
        panel.add(cameraButton);
    }

    private void removeGroupButton(int index){
        for(JButton button: groupButtons){
            panel.remove(button);
        }
        List<JButton> newGroupButtons = new ArrayList<>();
        int idx = 0;
        for(JButton button: groupButtons){
            if(button!= groupButtons.get(index)){
                newGroupButtons.add(getGroupButton(idx));
                idx++;
                panel.add(button);
            }
        }
    }

    private void addGroupButton(int index){
        JButton button = getGroupButton(index);
        panel.add(button);
    }

    private JButton getGroupButton(int index) {
        JButton button = new JButton();
        button.setHorizontalAlignment(JButton.CENTER);
        button.setBackground(Color.PINK);
        button.setMargin(new Insets(0,0,0,0));
        ImageIcon icon = new ImageIcon(loadImage("group.png"));
        Image image = icon.getImage();
        image = image.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(image));
        button.setFont(new Font("Serif", Font.BOLD, 14));
        button.setText("  " + index + "  " );
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.BOTTOM);
        button.addActionListener(e-> SelectionEvents.selectGroup(index));
        button.setToolTipText("Select Group " + index);
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
