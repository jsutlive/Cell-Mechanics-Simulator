package Renderer.UIElements.Panels;

import Framework.Events.EventHandler;
import Framework.Events.IEvent;
import Input.InputEvents;
import Renderer.UIElements.Panels.EntityPanel;
import Renderer.Camera;
import Utilities.Geometry.Vector.Vector2i;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;

import static Framework.Data.ImageHandler.loadImage;
import static java.awt.MouseInfo.getPointerInfo;

public class InputPanel {

    private JPanel panel;
    private JButton toggleSimulationButton;
    private JLabel mouseLabel;
    private final Canvas tempCanvasReference; //FIX

    public InputPanel(Canvas canvas){
        initialize();
        InputEvents.onMove.subscribe(this::findHoverCoordinates);

        tempCanvasReference = canvas;
    }

    public void initialize() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(350,100));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        PlayPanel playPanel = new PlayPanel();
        panel.add(playPanel.getPanel());
        GroupPanel groupPanel = new GroupPanel();
        panel.add(groupPanel.getPanel());
        EntityPanel componentPanel = new EntityPanel();

        JScrollPane componentScroll = new JScrollPane(componentPanel.getPanel());
        componentScroll.setHorizontalScrollBar(null);
        panel.add(componentScroll);
        mouseLabel = new JLabel("TEST");
        panel.add(mouseLabel);


    }

    public void findHoverCoordinates(MouseEvent e){
        if(Camera.main == null) return;
        int mouse_x= getPointerInfo().getLocation().x-tempCanvasReference.getLocationOnScreen().x;
        int mouse_y= getPointerInfo().getLocation().y-tempCanvasReference.getLocationOnScreen().y;
        Vector2i mousePos = Camera.main.getScreenPoint(new Vector2i(mouse_x, mouse_y));
        mouseLabel.setText("Position: " + mousePos.print());
    }

    public JPanel getPanel() {
        return panel;
    }
}
