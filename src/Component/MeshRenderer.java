package Component;

import Framework.Object.Annotations.DoNotDestroyInGUI;
import Framework.Object.Entity;
import Input.SelectionEvents;
import Renderer.Graphics.IColor;
import Framework.Rigidbodies.Edge;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Math.CustomMath;

import java.awt.*;
import java.util.HashSet;

/**
 * Cell Renderer class handles all drawing functions for the cells.
 */
@DoNotDestroyInGUI
public class MeshRenderer extends ObjectRenderer
{
    private transient Mesh cellMesh;
    private Color highlightColor = Color.yellow;

    public boolean showEdgeNormals = false;

    public MeshRenderer(boolean active){
        enabled = active;
    }

    @Override
    public void awake() {
        SelectionEvents.onEntitySelected.subscribe(this::highlightColor);
        onRendererAdded.invoke(this);
        cellMesh = parent.getComponent(Mesh.class);
    }


    private void setDefaultColor(Color color){
        defaultColor = color;
    }

    public void resetToDefaultColor(){
        color = defaultColor;
        alterColors(color);
    }

    public void highlightColor(HashSet<Entity> entities){
        if(!entities.contains(parent)){
            resetToDefaultColor();
        }
        else {
            this.color = highlightColor;
            alterColors(color);
        }
    }

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
        onRendererRemoved.invoke(this);
        SelectionEvents.onEntitySelected.unSubscribe(this::highlightColor);
    }
}
