package Renderer.Graphics.Vector;

import Utilities.Geometry.Vector.Vector2i;

import java.awt.*;

public class CircleGraphic extends VectorGraphic{
    int radius;
    Vector2i center;
    Color color;
    public CircleGraphic(Vector2i center, int radius, Color color){
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
