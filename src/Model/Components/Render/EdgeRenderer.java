package Model.Components.Render;

import Engine.States.State;
import GUI.Painter;
import Model.EdgeMono;
import Physics.Rigidbodies.Edges.BasalEdge;
import Physics.Rigidbodies.Nodes.Node;
import Physics.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector2f;

import java.awt.*;

public class EdgeRenderer extends ObjectRenderer {
    private Color color = Painter.DEFAULT_COLOR;
    private EdgeMono edge;
    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void render() {
        Node[] nodes = edge.getNodes();
        Vector2f pos1 = (Vector2f) nodes[0].getPosition();
        Vector2f pos2 = (Vector2f) nodes[1].getPosition();

        Painter.drawLine(pos1.asInt(), pos2.asInt(), color);
        if(edge.getEdge() instanceof BasalEdge){
        Painter.drawEdgeNormal(edge.getEdge());
        }
    }

    @Override
    public void awake() {
        State.setFlagToRender(parent);
        edge = (EdgeMono) parent;
    }
}