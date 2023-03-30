package Component;

import Framework.Object.Annotations.DoNotDestroyInGUI;
import Framework.Rigidbodies.Edge;
import Framework.Rigidbodies.Node2D;
import Utilities.Geometry.Vector.Vector2f;

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

    @Override
    public float getMaximumDistance() {
        System.out.println("TEST");
        float maximumDistance = 0f;
        int sz = nodes.size();
        for(int i = 0; i < sz-1; i++){
            for(int j = i+1; j < sz; j++){
                Vector2f a = nodes.get(i).getPosition();
                Vector2f b = nodes.get(j).getPosition();
                maximumDistance = Math.max(maximumDistance, Vector2f.dist(a, b));
            }
        }
        return maximumDistance;
    }

    @Override
    public float getMinimumDistance() {
        float minimumDistance = Float.POSITIVE_INFINITY;
        int sz = nodes.size();
        for(int i = 0; i < sz-1; i++){
            for(int j = i+1; j < sz; j++){
                Vector2f a = nodes.get(i).getPosition();
                Vector2f b = nodes.get(j).getPosition();
                minimumDistance = Math.min(minimumDistance, Vector2f.dist(a, b));
            }
        }
        return minimumDistance;
    }



}
