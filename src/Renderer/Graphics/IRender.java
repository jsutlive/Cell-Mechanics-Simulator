package Renderer.Graphics;

import Framework.Events.EventHandler;
import Renderer.Camera;
import Utilities.Geometry.Vector.Vector2i;

import static Renderer.Renderer.graphics;

import java.awt.*;
 public interface IRender {

    EventHandler<IRender> onRendererAdded = new EventHandler<>();
    EventHandler<IRender> onRendererRemoved = new EventHandler<>();

    default void add(IRender rend){
        onRendererAdded.invoke(rend);
    }
    default void remove(IRender rend){
        onRendererRemoved.invoke(rend);
    }
    void render();

     default void drawLine(Vector2i pointA, Vector2i pointB){
         Vector2i transformedPointA = Camera.main.transformToView(pointA);
         Vector2i transformedPointB = Camera.main.transformToView(pointB);
         graphics.drawLine(transformedPointA.x, transformedPointA.y, transformedPointB.x,transformedPointB.y);
     }

    default void drawLine(Vector2i pointA, Vector2i pointB, Color color) {
        graphics.setColor(color);
        drawLine(pointA, pointB);
    }

     default void drawCircle(Vector2i center, int diameter){
         Vector2i transformedCenter = Camera.main.transformToView(center);
         int transformedDiameter = Math.round(diameter* Camera.main.getScale());
         graphics.drawOval(transformedCenter.x - transformedDiameter/2,
                 transformedCenter.y - transformedDiameter/2,
                 transformedDiameter,
                 transformedDiameter);
     }

    default void drawCircle(Vector2i center, int diameter, Color color){
        graphics.setColor(color);
        drawCircle(center, diameter);
    }

 }
