package Input;

import Framework.Engine;
import Framework.Events.EventHandler;
import Framework.Events.IEvent;
import Framework.States.State;

import javax.swing.*;
import java.awt.*;

public class InputPanel {

    private JPanel panel;
    private JButton playButton;
    private JButton stopButton;

    public InputPanel(){
        initialize();

        IEvent<Boolean> playEventSink = this::disablePlayButton;
        IEvent<Boolean> stopEventSink = this::enablePlayButton;

        InputEvents.onPlay.subscribe(playEventSink);
        InputEvents.onStop.subscribe(stopEventSink);

    }

    public void initialize() {
        panel = new JPanel(new GridLayout(15, 1));
        createPlayButton();
        createStopButton();
        createTimestepSlider();
    }

    private void createTimestepSlider() {
        JLabel timestepLabel = new JLabel("timestep");
        JSlider timestepSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 10);
        timestepSlider.setFocusable(false);
        timestepSlider.setMajorTickSpacing(10);
        timestepSlider.setPaintTicks(true);
        timestepSlider.addChangeListener(e -> changeTimestepSlider(timestepSlider.getValue() / 10000f));
        panel.add(timestepLabel);
        panel.add(timestepSlider);
    }

    private void createPlayButton() {
        playButton = new JButton("Play");
        playButton.addActionListener(e -> InputEvents.play());
        panel.add(playButton);
    }

    private void createStopButton() {
        stopButton = new JButton("Stop");
        stopButton.addActionListener(e -> InputEvents.stop());
        stopButton.setEnabled(false);
        panel.add(stopButton);
    }

    public JPanel getPanel(){
        return panel;
    }

    public JButton getPlayButton(){
        return playButton;
    }

    void disablePlayButton(boolean bool){
        playButton.setEnabled(false);
        stopButton.setEnabled(true);
        try {
            State.ChangeState();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    void enablePlayButton(boolean bool){
        playButton.setEnabled(true);
        stopButton.setEnabled(false);
        try {
            State.ChangeState();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static EventHandler<Float> onTimestepSliderChanged = new EventHandler<>();

    public void changeTimestepSlider(float f){
        onTimestepSliderChanged.invoke(f);
    }
}
