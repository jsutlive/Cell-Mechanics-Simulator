package Morphogenesis.Components.Render;

import Framework.Object.Annotations.DoNotDestroyInGUI;
import Framework.Object.Annotations.DoNotExposeInGUI;
import Framework.Object.Entity;
import Framework.States.State;
import Input.SelectionEvents;
import Morphogenesis.Components.Meshing.RingCellMesh;
import Morphogenesis.Components.MouseSelector;
import Renderer.Graphics.IColor;
import Renderer.Graphics.Painter;
import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Rigidbodies.Edges.Edge;

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
        State.setFlagToRender(parent);
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
        if(enabled)
            Painter.drawMesh(cellMesh, color);
    }

    @Override
    public void onDestroy() {
        SelectionEvents.onEntitySelected.unSubscribe(this::highlightColor);
    }
}
