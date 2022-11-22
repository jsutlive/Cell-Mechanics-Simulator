package Input;

import Framework.Events.EventHandler;
import Utilities.Geometry.Vector.Vector2i;
import javafx.scene.input.KeyCode;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.*;

public class InputEvents implements KeyListener, MouseListener, MouseMotionListener {

    private boolean spaceToggle = false;

      @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_SPACE){
            if(spaceToggle) stop();
            else play();
        }
        if(keyCode == KeyEvent.VK_EQUALS) {
            scale(1.2f);
        }
        if(keyCode == KeyEvent.VK_MINUS) {
            scale(1/1.2f);
        }

        if(keyCode == KeyEvent.VK_LEFT) {
            shift(new Vector2i(-25, 0));
        }
        if(keyCode == KeyEvent.VK_RIGHT) {
            shift(new Vector2i(25, 0));
        }
        if(keyCode == KeyEvent.VK_UP) {
            shift(new Vector2i(0, -25));
        }
        if(keyCode == KeyEvent.VK_DOWN) {
            shift(new Vector2i(0, 25));
        }

        if(keyCode == KeyEvent.VK_ALT){
            onAlt.invoke(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_ALT){
            onAlt.invoke(false);
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        click(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        onPress.invoke(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        onRelease.invoke(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public static EventHandler<Vector2i> onShift = new EventHandler<>();
    public static EventHandler<Float> onScale = new EventHandler<>();

    public static EventHandler<Boolean> onAlt = new EventHandler<>();
    public static EventHandler<Boolean> onPlay = new EventHandler<>();
    public static EventHandler<Boolean> onStop = new EventHandler<>();
    public static EventHandler<MouseEvent> onClick = new EventHandler<>();
    public static EventHandler<MouseEvent> onMove = new EventHandler<>();
    public static EventHandler<MouseEvent> onDrag = new EventHandler<>();
    public static EventHandler<MouseEvent> onPress = new EventHandler<>();
    public static EventHandler<MouseEvent> onRelease = new EventHandler<>();

    public void click(MouseEvent event){
        onClick.invoke(event);
    }

    public void moveMouse(MouseEvent event){
        onMove.invoke(event);
    }

    public static void play(){
        onPlay.invoke(true);
    }

    public static void stop(){
        onStop.invoke(true);
    }

    public void scale(Float f){
        onScale.invoke(f);
    }

    public void shift(Vector2i i){
        onShift.invoke( i);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        onDrag.invoke(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        moveMouse(e);
    }
}
