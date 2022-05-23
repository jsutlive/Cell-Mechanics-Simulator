package Input;

import Engine.States.State;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
        JLabel label = new JLabel("Press SPACE to run/stop simulation");
        _instance = this;
        addKeyListener(_instance);
        setSize(200,200);
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
    }

    @Override
    public void keyReleased(KeyEvent e) {


    }
}
