package Morphogenesis.Components.Meshing;

import Morphogenesis.Rigidbodies.Edges.BasicEdge;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Math.CustomMath;


public class SimpleCellMesh extends Mesh{


    public SimpleCellMesh build(int radius, int nodeCount, Vector2f center) {
        for(int i =0; i < nodeCount; i++){
            Vector2f unit = CustomMath.GetUnitVectorOnCircle(i, nodeCount);
            Vector2f positionFromCenter = unit.mul(radius);
            Vector2f nodePosition = positionFromCenter.add(center);
            nodes.add(new Node2D(nodePosition));
        }
        for(int i = 0; i < nodes.size(); i++){
            if(i != (nodes.size()-1)){
                edges.add(new BasicEdge(nodes.get(i), nodes.get(i+1)));
            }
            else{
                edges.add(new BasicEdge(nodes.get(i), nodes.get(0)));
            }
        }
        return this;
    }
}
