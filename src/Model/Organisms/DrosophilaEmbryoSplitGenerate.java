package Model.Organisms;

import Engine.Renderer;
import Engine.States.State;
import Model.Cell;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector2i;
import Utilities.Model.Builder;

import java.util.ArrayList;
import java.util.List;

public class DrosophilaEmbryoSplitGenerate implements  IOrganism{
    List<Cell> allCells = new ArrayList<>();
    List<Node> allNodes  = new ArrayList<>();

    @Override
    public void generateOrganism() throws InstantiationException, IllegalAccessException {
        allCells = Builder.getCellRingSplitBuild(80, 4, 200, 300, new Vector2i(800));
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
    public List<Node> getAllNodes(){
        return allNodes;
    }
}
