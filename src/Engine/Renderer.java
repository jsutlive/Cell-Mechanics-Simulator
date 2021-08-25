package Engine;

import Engine.States.State;
import GUI.DisplayWindow;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Renderer implements Runnable
{
    public static Renderer instance;
    public static Graphics g;
    DisplayWindow displayWindow;
    BufferStrategy bufferStrategy;
    String title;
    int width, height;

    public static Renderer getInstance()
    {
        if(instance == null)
        {
            instance = new Renderer();
        }
        return instance;
    }
    private Renderer()
    {
        width = Simulation.bounds.x;
        height = Simulation.bounds.y;
        title = Simulation.title;
        displayWindow = new DisplayWindow(title, width, height);
    }
    @Override
    public void run()
    {
        bufferStrategy = displayWindow.GetCanvas().getBufferStrategy();
        if(bufferStrategy == null)
        {
            displayWindow.GetCanvas().createBufferStrategy(3);
            return;
        }
        g = bufferStrategy.getDrawGraphics();

        g.clearRect(0,0,width,height);

        if(State.GetState() != null)
        {
            State.GetState().Render();

        }

        bufferStrategy.show();
        g.dispose();


    }

    public DisplayWindow GetDisplayWindow()
    {
        return displayWindow;
    }
}
