package Physics.Bodies.Cell;

import Engine.Object.MonoBehavior;
import Physics.Bodies.Edge;
import Physics.Bodies.Vertex;
import Physics.Forces.Force;
import Utilities.Geometry.Vector2f;

public class CellEdge <T extends CellEdge<T>> extends Edge{

    private Cell cell;
    public final float initialLength;

    public CellEdge(Vertex a, Vertex b)
    {
        vertices[0] = a;
        vertices[1] = b;
        initialLength = getLength();
    }

    @Override
    protected void MovePosition(Vector2f deltaPosition) {
        vertices[0].getPosition().add(deltaPosition);
        vertices[1].getPosition().add(deltaPosition);
    }

    public Cell getCell()
    {
        return cell;
    }

    protected void setCell(Cell cell)
    {
        this.cell = cell;
    }

    @Override
    public void start() {

    }

    @Override
    public void destroy() {

    }


}
