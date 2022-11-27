package Renderer.Graphics;

import Framework.Events.EventHandler;

public interface IRender {
    EventHandler<IRender> onRendererAdded = new EventHandler<>();
    EventHandler<IRender> onRendererRemoved = new EventHandler<>();
    void render();
}
