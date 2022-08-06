package Input;

import Engine.States.State;

import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import Engine.ZoomRenderer;
import Engine.Renderer;
import Engine.Simulation;


public class Input extends JFrame implements KeyListener {

    private static Input _instance;
    private static boolean READY_TO_CHANGE_STATE = false;

    public static boolean changeStateKeyPressed(){
        return READY_TO_CHANGE_STATE;
    }

    public static Input getInstance() {
        if(_instance!=null) return _instance;
        else return new Input();
    }

    private Input(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));
        setContentPane(panel);

        JLabel label = new JLabel("Press SPACE to run/stop simulation");
        panel.add(label);

        JLabel timestepLabel = new JLabel("timestep");
        JSlider timestepSlider = new JSlider(JSlider.HORIZONTAL,1,100,10);
        timestepSlider.setFocusable(false);
        timestepSlider.setMajorTickSpacing(10);
        timestepSlider.setPaintTicks(true);
        timestepSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e){
                Simulation.TIMESTEP = timestepSlider.getValue() / 100f;
            }
        });
        panel.add(timestepLabel);
        panel.add(timestepSlider);
        _instance = this;
        addKeyListener(_instance);
        setSize(500,200);
        setVisible(true);
    }
    void Gui(){

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_SPACE){
            try {
                State.ChangeState();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }

        }
        if(keyCode == KeyEvent.VK_EQUALS){
            ((ZoomRenderer) Renderer.getInstance()).scale *= 1.2;
        }
        if(keyCode == KeyEvent.VK_MINUS){
            ((ZoomRenderer) Renderer.getInstance()).scale /= 1.2;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {


    }
}
