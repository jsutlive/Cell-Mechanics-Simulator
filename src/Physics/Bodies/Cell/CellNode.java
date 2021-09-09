package Physics.Bodies.Cell;

import Engine.Object.MonoBehavior;
import Physics.Bodies.Vertex;
import Physics.Forces.Force;
import Utilities.Geometry.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class CellNode extends Vertex
{
    private List<Cell> cell =  new ArrayList<>();

    public CellNode(Vector2f position) {
        this.position = position;
    }

    public CellNode() {

    }

    @Override
    protected void MovePosition(Vector2f deltaPosition) {
        position.add(deltaPosition);
    }

    public List<Cell> getCell()
    {
        return cell;
    }

    protected void setCell(Cell cell)
    {
        this.cell.add(cell);
    }

    @Override
    public void start() {

    }

    @Override
    public void update()
    {
        super.update();
    }

    @Override
    public void destroy() {

    }


}
