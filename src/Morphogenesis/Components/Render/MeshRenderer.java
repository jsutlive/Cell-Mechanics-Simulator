package Morphogenesis.Components.Render;

import Framework.Object.DoNotExposeInGUI;
import Framework.Object.Entity;
import Framework.States.State;
import Morphogenesis.Components.Lattice.Lattice;
import Morphogenesis.Components.MouseSelector;
import Renderer.Graphics.IColor;
import Renderer.Graphics.Painter;
import Morphogenesis.Entities.Cell;
import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Rigidbodies.Edges.Edge;

import java.awt.*;
import java.util.Collections;

/**
 * Cell Renderer class handles all drawing functions for the cells.
 */
@DoNotExposeInGUI
public class MeshRenderer extends ObjectRenderer
{
    private transient Mesh cellMesh;
    private Color highlightColor = Color.yellow;

    @Override
    public void awake() {
        State.setFlagToRender(parent);
        cellMesh = parent.getComponent(Mesh.class);
        defaultColor = color;
        MouseSelector.onEntitySelected.subscribe(this::highlightColor);
    }

    private void setDefaultColor(Color color){
        defaultColor = color;
    }

    public void resetToDefaultColor(){
        color = defaultColor;
        alterColors(color);
    }

    public void highlightColor(Entity e){
        if(parent!=e){
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
        Painter.drawMesh(cellMesh, color);
    }
}
