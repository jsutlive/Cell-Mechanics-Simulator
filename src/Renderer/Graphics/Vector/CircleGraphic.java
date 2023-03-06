package Renderer.Graphics.Vector;

import Utilities.Geometry.Vector.Vector2f;

import java.awt.*;

public class CircleGraphic extends VectorGraphic{
    float radius;
    Vector2f center;
    Color color;
    public CircleGraphic(Vector2f center, float radius, Color color){
        this.center = center;
        this.radius = radius;
        this.color = color;
    }

    @Override
    public void render() {
        drawCircle(center, radius-1, color);
        drawCircle(center, radius, color);
        drawCircle(center, radius+1, color);
    }
}
