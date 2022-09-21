package Model.Components.Lattice;

import Model.Components.Component;
import Model.Components.Meshing.CellMesh;
import Physics.Rigidbodies.BasicEdge;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;

import java.util.ArrayList;
import java.util.List;

public class Lattice extends Component {
    public List<Edge> edgeList = new ArrayList<>();

    /**
     * Assumes apical resolution of 1. Constructs lattice based on the cell mesh     *
     */
    public void buildLattice(){
        List<Node> nodes = getComponent(CellMesh.class).nodes;
        if(nodes.size() != 0){
            int nodeSize = nodes.size();
            int count = (nodeSize/2);
            for(int i = 0; i <count; i++){
                edgeList.add(new BasicEdge(nodes.get(i), nodes.get(nodeSize - i - 2)));
                edgeList.add(new BasicEdge(nodes.get(i+1), nodes.get(nodeSize - i - 1) ));
            }
        }

    }
}
