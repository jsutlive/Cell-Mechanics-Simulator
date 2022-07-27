package Model.Organisms;

import Model.Cells.Cell;
import Physics.Rigidbodies.Node;

import java.util.List;

public interface IOrganism {
    /**
     * Generate the organism and any cell data objects associated with it. Does not bind physics.
     *
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    void generateOrganism() throws InstantiationException, IllegalAccessException;

    /**
     * @return all cells in the organism
     */
    List<Cell> getAllCells();

    /**
     * @return all nodes in the organism
     */
    List<Node> getAllNodes();

    void addCellToList(List<Cell> cellList, Cell cell);
}
