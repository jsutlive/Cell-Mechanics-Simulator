package Physics.Bodies.Cell;

import Physics.Bodies.Vertex;
import Utilities.Geometry.Vector2f;

public class CellNode extends Vertex
{
    private Cell cell;

    public CellNode(Vector2f position) {
        this.position = position;
    }

    public CellNode() {

    }

    @Override
    protected void MovePosition(Vector2f deltaPosition) {
        position.add(deltaPosition);
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
    public void update()
    {
        super.update();
        //if(forceMap.size()>= 1)
        //System.out.println(this.cell.getID() + ":::" + forceMap.size());
    }

}
