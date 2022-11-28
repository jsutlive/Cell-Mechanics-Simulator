package Renderer.Graphics;

import Framework.Events.EventHandler;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Renderer.Renderer;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Geometry.Vector.Vector2i;
import Utilities.Math.CustomMath;

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

    default void drawEdgeNormal(Edge edge){
        Vector2f center = edge.getCenter();
        Vector2f normal = (Vector2f) CustomMath.normal(edge);
        normal.mul(7);
        normal = normal.add(center);

        drawLine(center.asInt(), normal.asInt());
    }

    default void drawLine(Vector2i pointA, Vector2i pointB) {
        Renderer.instance.drawLine(pointA, pointB);
    }

    default void drawLine(Vector2i pointA, Vector2i pointB, Color color) {
        Renderer.instance.setColor(color);
        drawLine(pointA, pointB);
    }

    default void drawCircle(Vector2i center, int diameter){
        Renderer.instance.drawCircle(center, diameter);
    }

    default void drawCircle(Vector2i center, int diameter, Color color){
        Renderer.instance.setColor(color);
        drawCircle(center, diameter);
    }
}
