package Component;

import Framework.Object.Entity;
import Framework.Rigidbodies.Edge;
import Utilities.Geometry.Geometry;
import Utilities.Geometry.Vector.Vector2f;

import java.util.Collections;
import java.util.List;


public class RigidbodyCollider extends Collider{
    transient List<Entity> cells;
    transient List<Edge> edges;
    public transient float boundary;

    @Override
    public void awake() {
        cells = getChildren();
        edges = getComponent(Mesh.class).edges;
    }

    @Override
    public void update() {
        Collections.shuffle(cells);
        checkCollision();
    }

    private void checkCollision() {
        for(int i = 0; i < edges.size(); i++){
            Edge a = edges.get(i);
            for(int j = 0; j< edges.size(); j++){
                Edge b = edges.get(j);
                if(a == b) continue;
                if (b.contains(a.getNodes()[0])) continue;
                if (b.contains(a.getNodes()[1])) continue;
                detectBoundaryCollision(a,b);
            }
        }
    }

    private void detectBoundaryCollision(Edge edgeA, Edge edgeB){
        List<Vector2f> a = edgeA.getCollisionBox(boundary);
        List<Vector2f> b = edgeB.getCollisionBox(boundary);

        Vector2f collisionOut = new Vector2f();
        for(int i = 0; i < a.size()-1; i++){
            Vector2f p1 = a.get(i);
            Vector2f p2;
            if(i == a.size()-1) p2 = a.get(0);
            else p2 = a.get(i);
            for(int j = 0; j < b.size(); j++){
                Vector2f q1 = b.get(i);
                Vector2f q2;
                if(i == b.size()-1) q2 = b.get(0);
                else q2 = b.get(i);
                if(Geometry.doesIntersect(
                        p1.x, p1.y, p2.x, p2.y,
                        q1.x, q1.y, q2.x, q2.y,
                        collisionOut)){
                    if(collisionOut.isNull()) continue;
                    edgeA.addForceVector(getClass().getSimpleName(), collisionOut.sub(edgeA.getCenter()));
                    edgeB.addForceVector(getClass().getSimpleName(), collisionOut.sub(edgeB.getCenter()));
                }
            }
        }
    }

}
