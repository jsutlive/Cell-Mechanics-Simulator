package Renderer.Graphics;

import Framework.Events.EventHandler;
import Component.Camera;
import Utilities.Geometry.Vector.Vector2i;
import Utilities.Geometry.Vector.Vector2f;

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

     default void drawLine(Vector2f pointA, Vector2f pointB){
         Vector2i transformedPointA = Camera.main.transformToView(pointA);
         Vector2i transformedPointB = Camera.main.transformToView(pointB);
         graphics.drawLine(transformedPointA.x, transformedPointA.y, transformedPointB.x,transformedPointB.y);
     }

    default void drawLine(Vector2f pointA, Vector2f pointB, Color color) {
        graphics.setColor(color);
        drawLine(pointA, pointB);
    }

    default void drawPolygon(Vector2f[] points){
         int[] xPoints = new int[points.length];
         int[] yPoints = new int[points.length];
         for(int i = 0; i<points.length; i++){
             Vector2i transformedPoint = Camera.main.transformToView(points[i]);
             xPoints[i] = transformedPoint.x;
             yPoints[i] = transformedPoint.y;
         }
         graphics.drawPolygon(xPoints, yPoints, points.length);
    }

    default void drawPolygon(Vector2f[] points, Color color){
         graphics.setColor(color);
         drawPolygon(points);
    }

     default void drawCircle(Vector2f center, float diameter){
         Vector2i transformedCenter = Camera.main.transformToView(center);
         int transformedDiameter = Math.round(diameter* Camera.main.getScale());
         graphics.drawOval(transformedCenter.x - transformedDiameter/2,
                 transformedCenter.y - transformedDiameter/2,
                 transformedDiameter,
                 transformedDiameter);
     }

    default void drawCircle(Vector2f center, float diameter, Color color){
        graphics.setColor(color);
        drawCircle(center, diameter);
    }

 }
