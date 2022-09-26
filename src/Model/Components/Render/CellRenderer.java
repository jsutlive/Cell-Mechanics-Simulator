package Model.Components.Render;

import Engine.States.State;
import GUI.IColor;
import GUI.Painter;
import Model.Cells.Cell;
import Model.Components.Meshing.CellMesh;
import Physics.Rigidbodies.Edges.Edge;

import java.awt.*;

/**
 * Cell Renderer class handles all drawing functions for the cells.
 */
public class CellRenderer extends ObjectRenderer
{
    private transient CellMesh cellMesh;
    private Color color = Painter.DEFAULT_COLOR;

    @Override
    public void awake() {
        State.setFlagToRender(parent);
        cellMesh = parent.getComponent(CellMesh.class);
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        // set color for all edges in the cell
        this.color = color;
        for(Edge edge: cellMesh.edges)
        {
            if(edge != null)
            {
                ((IColor) edge).setColor(color);
            }
        }

    }

    /**
     * Tells rendering system to draw components of cell.
     */
    @Override
    public void render()
    {
        Painter.drawCell(getParentAs(Cell.class), color);
    }
}
