package Renderer.UIElements.Panels;

import Framework.Events.EventHandler;
import Framework.States.StateMachine;
import Input.InputEvents;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static Framework.Data.ImageHandler.loadImage;

public class PlayPanel {

    private JPanel panel;
    private JButton toggleSimulationButton;

    public PlayPanel(){
        StateMachine.onStateMachineStateChange.subscribe(this::toggleButton);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new BevelBorder(BevelBorder.RAISED));
        panel.setPreferredSize(new Dimension(300, 160));
        createPlayButton();
        createTimestepSlider();
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
        toggleSimulationButton.setToolTipText("Start Simulation");
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
            toggleSimulationButton.setText("Restart");
            toggleSimulationButton.setToolTipText("End Simulation");
            icon = new ImageIcon(loadImage("stop.png"));
        }else{
            toggleSimulationButton.setBackground(Color.GREEN);
            toggleSimulationButton.setText("Play");
            toggleSimulationButton.setToolTipText("Start Simulation");
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

    private void createTimestepSlider() {
        JPanel timestepPanel = new JPanel();
        JLabel timestepLabel = new JLabel("timestep");
        timestepLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JSlider timestepSlider = new JSlider(JSlider.HORIZONTAL, -18, -10, -14);
        timestepSlider.setFocusable(false);
        timestepSlider.setMajorTickSpacing(4);
        timestepSlider.setMinorTickSpacing(1);
        timestepSlider.setPaintTicks(true);
        timestepSlider.addChangeListener(e -> changeTimestepSlider((float) Math.pow(2f,timestepSlider.getValue())));

        timestepPanel.add(timestepLabel);
        timestepPanel.add(timestepSlider);
        panel.add(timestepPanel);
    }

}
