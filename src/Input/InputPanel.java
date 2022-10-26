package Input;

import Framework.Events.EventHandler;
import Framework.Events.IEvent;
import Framework.States.State;
import Renderer.EntityPanel;
import Utilities.Geometry.Vector.Vector2i;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

public class InputPanel {

    private JPanel panel;
    private JButton playButton;
    private JButton stopButton;
    private JLabel mouseLabel;
    private Canvas tempCanvasReference; //FIX
    private JTextField elasticConstantsModifier;

    public InputPanel(Canvas canvas){
        initialize();

        IEvent<Boolean> playEventSink = this::disablePlayButton;
        IEvent<Boolean> stopEventSink = this::enablePlayButton;
        IEvent<MouseEvent> mouseMoveEventSink = this::findHoverCoordinates;

        InputEvents.onPlay.subscribe(playEventSink);
        InputEvents.onStop.subscribe(stopEventSink);
        InputEvents.onMove.subscribe(mouseMoveEventSink);
        tempCanvasReference = canvas;
    }

    public void initialize() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(300,100));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JPanel buttonGroup = new JPanel(new FlowLayout());
        buttonGroup.add(createPlayButton());
        buttonGroup.add(createStopButton());
        panel.add(buttonGroup);
        createTimestepSlider();
        EntityPanel componentPanel = new EntityPanel();

        elasticConstantsModifier = new JTextField();
        elasticConstantsModifier.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                String s = elasticConstantsModifier.getText();
                if(!s.isEmpty()){
                    float f = Float.parseFloat(elasticConstantsModifier.getText());
                    State.setNewElasticConstants(f);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        panel.add(elasticConstantsModifier);
        panel.add(new JLabel("Global Elastic Constant"), BorderLayout.NORTH);

        JScrollPane componentScroll = new JScrollPane(componentPanel.getPanel());
        componentScroll.setHorizontalScrollBar(null);
        panel.add(componentScroll);
        mouseLabel = new JLabel("TEST");
        panel.add(mouseLabel);


    }

    public void findHoverCoordinates(MouseEvent e){
        int mouse_x=MouseInfo.getPointerInfo().getLocation().x-tempCanvasReference.getLocationOnScreen().x;
        int mouse_y=MouseInfo.getPointerInfo().getLocation().y-tempCanvasReference.getLocationOnScreen().y;
        mouseLabel.setText("Position: " + new Vector2i(mouse_x, mouse_y).print());
    }


    private void createTimestepSlider() {
        JPanel timestepPanel = new JPanel();
        JLabel timestepLabel = new JLabel("timestep");
        timestepLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JSlider timestepSlider = new JSlider(JSlider.HORIZONTAL, 0, 60, 10);
        timestepSlider.setFocusable(false);
        timestepSlider.setMajorTickSpacing(5);
        timestepSlider.setPaintTicks(true);
        timestepSlider.addChangeListener(e -> changeTimestepSlider(timestepSlider.getValue() / 10000f));

        timestepPanel.add(timestepLabel);
        timestepPanel.add(timestepSlider);
        panel.add(timestepPanel);
    }

    private JButton createPlayButton() {
        playButton = new JButton("Play");
        playButton.setPreferredSize(new Dimension(150,30));
        ImageIcon playIcon = new ImageIcon("assets/play.png");
        Image play = playIcon.getImage();
        play = play.getScaledInstance(15,15, Image.SCALE_SMOOTH);
        playButton.setIcon(new ImageIcon(play));
        playButton.addActionListener(e -> InputEvents.play());
        playButton.setBackground(Color.GREEN);
        return playButton;
    }

    private JButton createStopButton() {
        stopButton = new JButton("Stop");
        stopButton.setPreferredSize(new Dimension(150,30));
        ImageIcon stopIcon = new ImageIcon("assets/stop.png");
        Image stop = stopIcon.getImage();
        stop = stop.getScaledInstance(15,15, Image.SCALE_SMOOTH);
        stopButton.setIcon(new ImageIcon(stop));
        stopButton.addActionListener(e -> InputEvents.stop());
        stopButton.setBackground(Color.RED);
        stopButton.setEnabled(false);
        return stopButton;
    }

    public JPanel getPanel(){
        return panel;
    }

    public JButton getPlayButton(){
        return playButton;
    }

    void disablePlayButton(boolean bool){
        playButton.setEnabled(false);
        elasticConstantsModifier.setEnabled(false);
        stopButton.setEnabled(true);
        try {
            State.ChangeState();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    void enablePlayButton(boolean bool){
        playButton.setEnabled(true);
        elasticConstantsModifier.setEnabled(true);
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
