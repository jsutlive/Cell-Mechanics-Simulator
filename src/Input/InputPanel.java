package Input;

import Framework.Events.EventHandler;
import Framework.Events.IEvent;
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

        IEvent<Boolean> playEventSink = this::toggleButton;
        IEvent<Boolean> stopEventSink = this::toggleButton;
        IEvent<MouseEvent> mouseMoveEventSink = this::findHoverCoordinates;

        InputEvents.onToggleSimulation.subscribe(playEventSink);
        InputEvents.onToggleSimulation.subscribe(stopEventSink);
        InputEvents.onMove.subscribe(mouseMoveEventSink);
        tempCanvasReference = canvas;
    }

    public void initialize() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(300,100));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        createPlayButton();
        createTimestepSlider();
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


    private void createTimestepSlider() {
        JPanel timestepPanel = new JPanel();
        JLabel timestepLabel = new JLabel("timestep");
        timestepLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JSlider timestepSlider = new JSlider(JSlider.HORIZONTAL, 0, 60, 10);
        timestepSlider.setFocusable(false);
        timestepSlider.setMajorTickSpacing(5);
        timestepSlider.setPaintTicks(true);
        timestepSlider.addChangeListener(e -> changeTimestepSlider(timestepSlider.getValue() / 1e5f));

        timestepPanel.add(timestepLabel);
        timestepPanel.add(timestepSlider);
        panel.add(timestepPanel);
    }

    void createPlayButton() {
        toggleSimulationButton = new JButton("Play");
        toggleSimulationButton.setSize(new Dimension(200,80));
        ImageIcon playIcon = new ImageIcon(loadImage("play.png"));
        Image play = playIcon.getImage();
        play = play.getScaledInstance(30,30, Image.SCALE_SMOOTH);
        toggleSimulationButton.setIcon(new ImageIcon(play));
        toggleSimulationButton.addActionListener(e -> InputEvents.toggleSimulation(true));
        toggleSimulationButton.setBackground(Color.GREEN);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(toggleSimulationButton);
        buttonPanel.setLayout(new FlowLayout());
        panel.add(buttonPanel);
    }

    public JPanel getPanel(){
        return panel;
    }

    void toggleButton(boolean isPlayingSimulation){
        ImageIcon icon;
        if(isPlayingSimulation) {
            toggleSimulationButton.setBackground(Color.RED);
            toggleSimulationButton.setText("Stop");
            icon = new ImageIcon(loadImage("stop.png"));
        }else{
            toggleSimulationButton.setBackground(Color.GREEN);
            toggleSimulationButton.setText("Play");
            icon = new ImageIcon(loadImage("play.png"));
        }

        Image image = icon.getImage();
        image = image.getScaledInstance(30,30, Image.SCALE_SMOOTH);
        toggleSimulationButton.setIcon(new ImageIcon(image));
        toggleSimulationButton.removeActionListener(toggleSimulationButton.getActionListeners()[0]);
        toggleSimulationButton.addActionListener(e -> InputEvents.toggleSimulation(!isPlayingSimulation));
    }
    public static EventHandler<Float> onTimestepSliderChanged = new EventHandler<>();

    public void changeTimestepSlider(float f){
        onTimestepSliderChanged.invoke(f);
    }
}
