package Physics.Bodies.Cell;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CellGroup
{
    private List<Cell> cells = new ArrayList<Cell>();
    private Color color;

    public void addCell(Cell cell)
    {
        if(color != null) cell.setColor(color);
        cells.add(cell);
    }

    public List<Cell> getCells()
    {
        return cells;
    }

    public Cell getCell(int index)
    {
        return cells.get(index);
    }

    public void setColor(Color color)
    {
        System.out.println(cells.size());
        for(Cell cell: cells)
        {
            cell.setColor(color);
        }
    }

    public void update()
    {
        for(Cell cell: cells)
        {
            cell.update();
        }
    }
}
