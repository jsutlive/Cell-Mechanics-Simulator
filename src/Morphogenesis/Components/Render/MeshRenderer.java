package Morphogenesis.Components.Render;

import Framework.Object.Annotations.DoNotDestroyInGUI;
import Framework.Object.Entity;
import Input.SelectionEvents;
import Morphogenesis.Components.MouseSelector;
import Morphogenesis.Rigidbodies.Nodes.Node;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Renderer.Graphics.IColor;
import Renderer.Graphics.IRender;
import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Utilities.Geometry.Vector.Vector;
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
    public boolean enabled = true;

    @Override
    public void awake() {
        onRendererAdded.invoke(this);
        cellMesh = parent.getComponent(Mesh.class);
        defaultColor = color;
        SelectionEvents.onEntitySelected.subscribe(this::highlightColor);
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

    /**
     * Tells rendering system to draw components of cell.
     */
    @Override
    public void render()
    {
        if(!enabled) return;
        for(Edge edge: cellMesh.edges)
        {
            Vector2f[] positions = edge.getPositions();
            System.out.print("meshStart ");
            for(Node node: cellMesh.nodes)
            {
                Vector position = node.getPosition();
                System.out.print(position.get(0) + "&" + position.get(1) + " ");
            }
            System.out.print("meshEnd ");
        }
    }

    @Override
    public void onDestroy() {
        onRendererRemoved.invoke(this);
        SelectionEvents.onEntitySelected.unSubscribe(this::highlightColor);
    }
}
