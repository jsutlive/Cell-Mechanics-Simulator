package Model.Components.Lattice;

import Model.Components.Component;
import Model.Components.Meshing.Builder;
import Model.Components.Meshing.CellMesh;
import Physics.Rigidbodies.Edges.BasicEdge;
import Physics.Rigidbodies.Edges.Edge;
import Physics.Rigidbodies.Nodes.Node2D;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Lattice extends Component {
    public List<Edge> edgeList = new ArrayList<>();

    /**
     * Assumes apical resolution of 1. Constructs lattice based on the cell mesh     *
     */
    public void buildLattice(){
        List<Node2D> nodes = getComponent(CellMesh.class).nodes;
        if(nodes.size() != 0){
            int nodeSize = nodes.size();
            int count = (nodeSize/2);
            for(int i = 0; i <count; i++){
                edgeList.add(new BasicEdge(nodes.get(i), nodes.get(nodeSize - i - 2)));
                edgeList.add(new BasicEdge(nodes.get(i+1), nodes.get(nodeSize - i - 1) ));
            }
        }

    }

    @Override
    public void onValidate()
    {
        for(Method method: getClass().getDeclaredMethods()){
            if(method.isAnnotationPresent(Builder.class))
            {
                try {
                    method.invoke(this);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
