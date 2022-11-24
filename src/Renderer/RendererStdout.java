package Renderer;

import Framework.Engine;
import Framework.States.State;
import Renderer.Graphics.DisplayWindow;
import Utilities.Geometry.Vector.Vector2i;

import java.awt.*;
import java.awt.image.BufferStrategy;


public class RendererStdout extends Renderer
{
    public RendererStdout()
    {

    }
    /**
     * Renders graphics to the screen. Should only be accessed from the Engine object.
     */

    @Override
    public void run()
    {
        try {
            if(State.GetState() != null)
            {
                State.GetState().Render();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
