package Renderer;

import Framework.States.StateMachine;
import Renderer.Graphics.DisplayWindow;
import Renderer.Graphics.IRender;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Renderer2D extends Renderer {
    DisplayWindow displayWindow;
    BufferStrategy bufferStrategy;

    public Renderer2D(String windowTitle) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        bounds = new Dimension(screenSize.height/10*7, screenSize.height/10*7);
        displayWindow = new DisplayWindow(windowTitle, bounds.width, bounds.height);
        IRender.onRendererAdded.subscribe(this::addGraphicToBatch);
        IRender.onRendererRemoved.subscribe(this::removeGraphicFromBatch);
        applicationIsRunning = true;
        StateMachine.onSaveStateInfo.subscribe(this::exportImage);
    }

    /**
     * Renders graphics to the screen.
     */
    @Override
    protected void render()
    {
        bufferStrategy = displayWindow.GetCanvas().getBufferStrategy();
        if(bufferStrategy == null) {
            displayWindow.GetCanvas().createBufferStrategy(3);
            return;
        }
        graphics = bufferStrategy.getDrawGraphics();
        graphics.clearRect(0,0,
                displayWindow.GetCanvas().getWidth(),
                displayWindow.GetCanvas().getHeight());
        for(int i = batch.size() -1; i >= 0; i--){
            if(i >= batch.size())return;
            batch.get(i).render(graphics);
        }
        bufferStrategy.show();
        graphics.dispose();
    }

    @Override
    public BufferedImage renderImage(){
        BufferedImage image = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_RGB);
        Graphics imageGraphics = image.createGraphics();
        for(int i = batch.size() -1; i >= 0; i--){
            if(i >= batch.size())continue;
            batch.get(i).render(imageGraphics);
        }
        imageGraphics.dispose();
        return image;
    }
}

