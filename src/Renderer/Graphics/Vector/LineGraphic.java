package Renderer.Graphics.Vector;

import Utilities.Geometry.Vector.Vector2i;

import java.awt.*;

public class LineGraphic extends VectorGraphic {
    public Vector2i posA;
    public Vector2i posB;
    public LineGraphic(Vector2i posA, Vector2i posB){
        this.posA = posA;
        this.posB = posB;
    }

    @Override
    public void render() {
        drawLine(posA, posB, Color.GREEN);
    }
}
