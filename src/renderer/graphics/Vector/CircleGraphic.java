package renderer.graphics.Vector;

import utilities.geometry.Vector.Vector2f;

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
    public void render(Graphics g) {
        drawCircle(center, radius-1, color, g);
        drawCircle(center, radius, color, g);
        drawCircle(center, radius+1, color, g);
    }
}
