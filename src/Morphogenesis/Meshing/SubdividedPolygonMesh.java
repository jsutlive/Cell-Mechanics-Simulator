package Morphogenesis.Meshing;

import Framework.Object.Annotations.DoNotDestroyInGUI;
import Framework.Rigidbodies.Edge;
import Framework.Rigidbodies.Node2D;

import java.util.List;

@DoNotDestroyInGUI
public class SubdividedPolygonMesh extends Mesh{

    @Override
    public void earlyUpdate() {
        for(Node2D n: nodes) n.resetResultantForce();
    }

    @Override
    public void lateUpdate() {
        for (Node2D n : nodes) {
            n.move();
        }
        calculateArea();
    }

    public SubdividedPolygonMesh build(List<Node2D> simpleNodes){
        nodes.addAll(simpleNodes);
        for(int i = 1; i < simpleNodes.size(); i++){
            edges.add(new Edge(simpleNodes.get(i-1), simpleNodes.get(i)));
        }
        edges.add(new Edge(simpleNodes.get(simpleNodes.size()-1), simpleNodes.get(0)));

        return this;
    }


}