package Renderer.Graphics.Vector;

import Utilities.Geometry.Vector.Vector2f;

import java.awt.*;

public class LineGraphic extends VectorGraphic {
    public Vector2f posA;
    public Vector2f posB;
    public LineGraphic(Vector2f posA, Vector2f posB){
        this.posA = posA;
        this.posB = posB;
    }

    @Override
    public void render(Graphics g) {
        drawLine(posA, posB, Color.GREEN, g);
    }
}
