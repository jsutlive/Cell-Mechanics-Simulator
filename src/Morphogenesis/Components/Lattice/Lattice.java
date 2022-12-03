package Morphogenesis.Components.Lattice;

import Framework.Object.Component;
import Framework.Object.Annotations.DoNotExposeInGUI;
import Morphogenesis.Components.Meshing.Builder;
import Morphogenesis.Components.Meshing.RingCellMesh;
import Morphogenesis.Rigidbodies.Edge;
import Morphogenesis.Rigidbodies.Node2D;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@DoNotExposeInGUI
public class Lattice extends Component {
    public List<Edge> edgeList = new ArrayList<>();

    /**
     * Assumes apical resolution of 1. Constructs lattice based on the cell mesh     *
     */
    @Builder
    public void buildLattice(){
        List<Node2D> nodes = getComponent(RingCellMesh.class).nodes;
        if(nodes.size() != 0){
            int nodeSize = nodes.size();
            int count = (nodeSize/2) - 1;
            for(int i = 0; i <count; i++){
                edgeList.add(new Edge(nodes.get(i), nodes.get(nodeSize - i - 2)));
                edgeList.add(new Edge(nodes.get(i+1), nodes.get(nodeSize - i - 1) ));
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
