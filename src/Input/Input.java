package Input;

import javafx.scene.input.KeyCode;

import java.awt.event.KeyEvent;
import java.util.Scanner;

public class InputHandler {
    public boolean changeStateKeyPressed(KeyEvent keyEvent){
        if(keyEvent.getKeyCode() == KeyCode.ENTER) return true;
        else return false;
    }
}
