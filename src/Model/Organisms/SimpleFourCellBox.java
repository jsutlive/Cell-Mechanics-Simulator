package Model.Organisms;

import Engine.Renderer;
import Engine.States.State;
import Model.Cell;
import Physics.Rigidbodies.Node;
import Utilities.Model.Builder;

import java.util.ArrayList;
import java.util.List;

public class SimpleFourCellBox implements IOrganism{
    List<Cell> allCells = new ArrayList<>();
    List<Node> allNodes = new ArrayList<>();
    @Override
    public void generateOrganism() throws InstantiationException, IllegalAccessException {
        allCells = Builder.getSimpleFourCellBox();
        System.out.println(allCells.size());
        for(Cell cell: allCells){
            State.setFlagToRender(cell);
            cell.setColor(Renderer.defaultColor);
            for(Node node: cell.getNodes()){
                if(!allNodes.contains(node)) allNodes.add(node);
            }
        }
    }

    @Override
    public List<Cell> getAllCells() {
        return allCells;
    }

    @Override
    public List<Node> getAllNodes() {
        return allNodes;
    }

    @Override
    public void addCellToList(List<Cell> cellList, Cell cell) {

    }
}
