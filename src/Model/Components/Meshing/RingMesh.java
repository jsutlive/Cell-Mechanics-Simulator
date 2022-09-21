package Model.Components.Meshing;

import Model.Cells.Cell;
import Physics.Rigidbodies.Node;
import Utilities.Math.Gauss;

import java.util.ArrayList;
import java.util.List;

public class RingMesh extends Mesh{

    public List<Node> outerNodes = new ArrayList<>();
    public List<Node> innerNodes = new ArrayList<>();
    public List<Cell> cellList = new ArrayList<>();

    @Override
    protected void calculateArea(){
        area = Gauss.nShoelace(outerNodes) - Gauss.nShoelace(innerNodes);
    }


}
