package Model;

import java.util.List;

public interface IOrganism
{
    /**
     * Generate the organism and any cell data objects associated with it. Does not bind physics.
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    void generateOrganism() throws InstantiationException, IllegalAccessException;

    /**
     * @return all cells in the organism
     */
    List<Cell> getAllCells();
}
