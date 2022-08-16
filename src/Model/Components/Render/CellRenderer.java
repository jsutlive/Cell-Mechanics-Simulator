package Model.Components.Render;

import GUI.IColor;
import GUI.Painter;
import Model.Cells.Cell;
import Model.Components.Render.ObjectRenderer;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;

import java.awt.*;

/**
 * Cell Renderer class handles all drawing functions for the cells.
 */
public class CellRenderer extends ObjectRenderer
{
    Cell cell;
    private Color color = Painter.DEFAULT_COLOR;

    @Override
    public void init() {
        cell = (Cell) parent;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        // set color for all nodes in the cell
        for(Node node : cell.getNodes() )
        {
            if(node instanceof IColor)
            {
                ((IColor) node).setColor(color);
            }
        }
        // set color for all edges in the cell
        for(Edge edge: cell.getEdges())
        {
            if(edge instanceof  IColor)
            {
                ((IColor) edge).setColor(color);
            }
        }
        for(Edge edge: cell.getInternalEdges())
        {
            if(edge instanceof  IColor)
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
        Painter.drawCell(cell);
    }
}