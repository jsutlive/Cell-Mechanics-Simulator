package input;

import framework.data.FileBuilder;
import framework.events.EventHandler;
import framework.utilities.Debug;
import renderer.uiElements.windows.ConsolePopUp;
import utilities.geometry.Vector.Vector2i;

import java.awt.event.*;
import java.io.IOException;
/**
 * InputEvents: Collection of events used to bridge GUI/ user input layer and simulation engine.
 *
 * Copyright (c) 2023 Joseph Sutlive and Tony Zhang
 * All rights reserved
 */
public class InputEvents implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    public static final EventHandler<String> onLoadModel = new EventHandler<>();
    private boolean spaceToggle = false;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_SPACE){
            spaceToggle = !spaceToggle;
            toggleSimulation(spaceToggle);
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
        if(keyCode == KeyEvent.VK_SHIFT){
            onShiftKey.invoke(true);
        }
        if(keyCode == KeyEvent.VK_DELETE || keyCode == KeyEvent.VK_BACK_SPACE){
            SelectionEvents.deleteSelection();
        }
        if(keyCode == KeyEvent.VK_C){
            new ConsolePopUp(Debug.INSTANCE);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_ALT){
            onAlt.invoke(false);
        }
        if(keyCode == KeyEvent.VK_SHIFT){
            onShiftKey.invoke(false);
        }
    }

    public static void clear(){
        onClear.invoke(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        click(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        onPress.invoke(e);
        SelectionEvents.refresh();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        onRelease.invoke(e);
        SelectionEvents.refresh();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public static final EventHandler<Boolean> onClear = new EventHandler<>();
    public static final EventHandler<Vector2i> onShift = new EventHandler<>();
    public static final EventHandler<Float> onScale = new EventHandler<>();

    public static final EventHandler<Boolean> onAlt = new EventHandler<>();
    public static final EventHandler<Boolean> onShiftKey = new EventHandler<>();
    public static final EventHandler<Boolean> onToggleSimulation = new EventHandler<>();
    public static final EventHandler<MouseEvent> onClick = new EventHandler<>();
    public static final EventHandler<MouseEvent> onMove = new EventHandler<>();
    public static final EventHandler<MouseEvent> onDrag = new EventHandler<>();
    public static final EventHandler<MouseEvent> onPress = new EventHandler<>();
    public static final EventHandler<MouseEvent> onRelease = new EventHandler<>();

    public static void toggleSimulation(boolean b) {
        if(!b) {
            try {
                FileBuilder.saveCSV();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            FileBuilder.saveDictionary.clear();
        }
        onToggleSimulation.invoke(b);
    }

    public void click(MouseEvent event){
        onClick.invoke(event);
    }

    public void moveMouse(MouseEvent event){
        onMove.invoke(event);
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
        SelectionEvents.refresh();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        moveMouse(e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

            if (e.getWheelRotation() < 0) {
                onScale.invoke(1.05f);
            } else {
                onScale.invoke(1/(1.05f));
            }

    }
}
