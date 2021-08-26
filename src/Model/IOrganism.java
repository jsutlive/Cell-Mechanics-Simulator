package Model;

import Physics.Bodies.Cell.Cell;
import Physics.Bodies.Cell.CellGroup;

public interface IOrganism
{
    void generateOrganism();
    CellGroup getAllCells();
}
