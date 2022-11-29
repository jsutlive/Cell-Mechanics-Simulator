package Morphogenesis.Components.Meshing;

import Morphogenesis.Rigidbodies.Edges.BasicEdge;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Morphogenesis.Rigidbodies.Nodes.Node;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector2f;

import java.util.ArrayList;
import java.util.List;


public class SubdividedPolygonMesh extends Mesh{

    public int numSubdivisions = 5;

    @Override
    public void earlyUpdate() {
        for(Node2D n: nodes) n.resetResultantForce();
    }

    @Override
    public void lateUpdate() {
        if(isStatic) return;
        for (Node2D n : nodes) {
            n.move();
        }
        calculateArea();
    }

    public SubdividedPolygonMesh build(List<Node2D> simpleNodes, int numSubdivisions){
        this.numSubdivisions = numSubdivisions;
        for(int i = 1; i < simpleNodes.size(); i++){
            getSubdividedEdge(simpleNodes.get(i-1), simpleNodes.get(i));
            if(i == simpleNodes.size()-1){
                getSubdividedEdge(simpleNodes.get(i), simpleNodes.get(0));
            }
        }
        for(int i = edges.size()-1; i> -1; i--){
            for(Node node:edges.get(i).getNodes())
            {
                if(!nodes.contains(node)){
                    edges.remove(edges.get(i));
                }
            }
        }
        /*for(int i = 1; i < simpleNodes.size(); i++){
            nodes.add(simpleNodes.get(i-1));
            edges.add(new BasicEdge(simpleNodes.get(i-1), simpleNodes.get(i)));
            if(i == simpleNodes.size()-1){
                nodes.add(simpleNodes.get(i));
                edges.add(new BasicEdge(simpleNodes.get(i), simpleNodes.get(0)));
            }
        }*/
        return this;
    }

    private void getSubdividedEdge(Node2D nodeA, Node2D nodeB) {
        Vector2f a = nodeA.getPosition();
        Vector2f b = nodeB.getPosition();
        float edgeLength = Vector2f.dist(a, b)/numSubdivisions;
        Node2D oldNode = nodeA;
        nodes.add(oldNode);
        Vector2f segment = Vector2f.unit(a,b).mul(edgeLength);
        for(int j = 0; j < numSubdivisions; j++){
            Edge newEdge;
            Vector2f oldNodePosition = oldNode.getPosition();
            Vector2f newPosition = oldNodePosition.add(segment);
            Node2D newNode = new Node2D(newPosition);
            nodes.add(newNode);
            newEdge = new BasicEdge(oldNode, newNode);
            edges.add(newEdge);
            oldNode = newNode;

        }
    }
}
