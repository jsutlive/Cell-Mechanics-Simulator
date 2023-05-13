package component;

import framework.object.annotations.DoNotDestroyInGUI;
import framework.object.Entity;
import input.SelectionEvents;
import renderer.graphics.IColor;
import framework.rigidbodies.Edge;
import utilities.geometry.Vector.Vector2f;
import utilities.math.CustomMath;

import java.awt.*;
import java.util.HashSet;

/**
 * MeshRenderer is responsible for rendering objects with complex polygon shapes
 * and manages click-select
 *
 * Copyright (c) 2023 Joseph Sutlive and Tony Zhang
 * All rights reserved
 */
@DoNotDestroyInGUI
public class MeshRenderer extends ObjectRenderer
{
    private transient Mesh cellMesh;
    private final Color highlightColor = Color.yellow;

    public boolean showEdgeNormals = false;

    @Override
    public void awake() {
        SelectionEvents.onEntitySelected.subscribe(this::highlightColor);
        add(this);
        cellMesh = parent.getComponent(Mesh.class);
    }


    private void setDefaultColor(Color color){
        defaultColor = color;
    }

    public void resetToDefaultColor(){
        color = defaultColor;
        alterColors(color);
    }

    /**
     * Changes selected entities to a specific highlighted color
     * @param entities list of entities to be colored
     */
    public void highlightColor(HashSet<Entity> entities){
        if(!entities.contains(parent)){
            resetToDefaultColor();
        }
        else {
            this.color = highlightColor;
            alterColors(color);
        }
    }

    /**
     * Override edge colors to a new color
     * @param color
     */
    private void alterColors(Color color) {
        for (Edge edge : cellMesh.edges) {
            if (edge != null) {
                ((IColor) edge).setColor(color);
            }
        }
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        setDefaultColor(color);
        // set color for all edges in the cell
        this.color = color;
        alterColors(color);
    }

    private void drawEdgeNormal(Edge edge, Graphics g){
        Vector2f center = edge.getCenter();
        Vector2f normal = (Vector2f) CustomMath.normal(edge);
        normal.mul(7);
        normal = normal.add(center);

        drawLine(center, normal, g);
    }

    /**
     * Tells rendering system to draw components of cell.
     */
    @Override
    public void render(Graphics g)
    {
        if(!enabled) return;
        if(cellMesh == null)return;
        int edgeCount = cellMesh.edges.size();
        for(int i = 0; i < edgeCount; i++){
            Edge mainEdge = cellMesh.edges.get(i);
            //floor mod is necessary here because the % operator is actually the remainder, (-1 % 5) = -1, floorMod(-1,5) = 4
            Edge nextEdge = cellMesh.edges.get(Math.floorMod(i + 1, edgeCount));
            Edge previousEdge = cellMesh.edges.get(Math.floorMod(i - 1, edgeCount));

            Vector2f[] positions = mainEdge.getPositions();

            //instead of just offsetting an edge by its own normals, it's nodes must be offset by the adjacent edge normals as well
            //so that the end point of each edge matches the others.

            float offsetAmount = 0.5f/Camera.main.getScale();
            drawLine(
                    positions[0]
                            .add(CustomMath.normal(mainEdge).mul(offsetAmount))
                            .add(CustomMath.normal(previousEdge).mul(offsetAmount)),
                    positions[1]
                            .add(CustomMath.normal(mainEdge).mul(offsetAmount))
                            .add(CustomMath.normal(nextEdge).mul(offsetAmount)),
                    color, g);
            if(showEdgeNormals) {
                drawEdgeNormal(cellMesh.edges.get(i), g);
            }
        }
    }

    @Override
    public void onDestroy() {
        remove(this);
        SelectionEvents.onEntitySelected.unSubscribe(this::highlightColor);
    }
}
