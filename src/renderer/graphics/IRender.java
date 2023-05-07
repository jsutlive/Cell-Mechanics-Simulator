package renderer.graphics;

import framework.events.EventHandler;
import component.Camera;
import utilities.geometry.Vector.Vector2i;
import utilities.geometry.Vector.Vector2f;

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
    void render(Graphics g);

     default void drawLine(Vector2f pointA, Vector2f pointB, Graphics g){
         Vector2i transformedPointA = Camera.main.transformToView(pointA);
         Vector2i transformedPointB = Camera.main.transformToView(pointB);
         g.drawLine(transformedPointA.x, transformedPointA.y, transformedPointB.x,transformedPointB.y);
     }

    default void drawLine(Vector2f pointA, Vector2f pointB, Color color, Graphics g) {
        g.setColor(color);
        drawLine(pointA, pointB, g);
    }

    default void drawPolygon(Vector2f[] points, Graphics g){
         int[] xPoints = new int[points.length];
         int[] yPoints = new int[points.length];
         for(int i = 0; i<points.length; i++){
             Vector2i transformedPoint = Camera.main.transformToView(points[i]);
             xPoints[i] = transformedPoint.x;
             yPoints[i] = transformedPoint.y;
         }
         g.drawPolygon(xPoints, yPoints, points.length);
    }

    default void drawPolygon(Vector2f[] points, Color color, Graphics g){
         g.setColor(color);
         drawPolygon(points, g);
    }

     default void drawCircle(Vector2f center, float diameter, Graphics g){
         Vector2i transformedCenter = Camera.main.transformToView(center);
         int transformedDiameter = Math.round(diameter* Camera.main.getScale());
         g.drawOval(transformedCenter.x - transformedDiameter/2,
                 transformedCenter.y - transformedDiameter/2,
                 transformedDiameter,
                 transformedDiameter);
     }

    default void drawCircle(Vector2f center, float diameter, Color color, Graphics g){
        g.setColor(color);
        drawCircle(center, diameter, g);
    }

 }
