package Renderer.Graphics.Vector;

import Renderer.Graphics.Painter;
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
        Painter.drawCircle(center, radius-1, color);
        Painter.drawCircle(center, radius, color);
        Painter.drawCircle(center, radius+1, color);
    }
}
