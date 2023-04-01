package Renderer;

import Renderer.Graphics.DisplayWindow;
import Renderer.Graphics.IRender;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Renderer2D extends Renderer {
    DisplayWindow displayWindow;
    BufferStrategy bufferStrategy;

    public Renderer2D(String windowTitle) {
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        bounds = new Dimension(screensize.height/10*7, screensize.height/10*7);
        displayWindow = new DisplayWindow(windowTitle, bounds.width, bounds.height);
        IRender.onRendererAdded.subscribe(this::addGraphicToBatch);
        IRender.onRendererRemoved.subscribe(this::removeGraphicFromBatch);
        applicationIsRunning = true;
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
        graphics.clearRect(0,0, bounds.width, bounds.height);

        for(int i = batch.size() -1; i >= 0; i--){
            if(i >= batch.size())return;
            batch.get(i).render();
        }

        bufferStrategy.show();
        displayWindow.GetCanvas().prepareImageForExport();
        graphics.dispose();
    }
}
