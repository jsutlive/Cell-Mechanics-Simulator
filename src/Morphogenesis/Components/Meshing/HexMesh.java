package Morphogenesis.Components.Meshing;

import Framework.Object.Annotations.DoNotDestroyInGUI;
import Framework.Object.Component;
import Framework.Object.Entity;
import Input.SelectionEvents;
import Morphogenesis.Components.Physics.CellGroups.GroupSelector;
import Morphogenesis.Components.Physics.OsmosisForce;
import Morphogenesis.Components.Physics.Spring.ElasticForce;
import Morphogenesis.Components.ReloadComponentOnChange;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Math.CustomMath;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static Input.SelectionEvents.onSelectionButtonPressed;
import static Utilities.Math.CustomMath.*;

@GroupSelector
@DoNotDestroyInGUI
@ReloadComponentOnChange
public class HexMesh extends Mesh{

    public int numSubdivisions = 5;
    int distanceFromCenter = 70;
    public List<Entity> cellList = new ArrayList<>();
    HashMap<Vector2f, Node2D> positionToNodeMap = new HashMap<>();



    @Override
    public void awake() {
        onSelectionButtonPressed.subscribe(this::selectAll);
        positionToNodeMap.clear();
        generateSimpleMesh(new Vector2f(0), 6);
        //float hypot = CustomMath.sq(distanceFromCenter) + CustomMath.sq(distanceFromCenter) -
        //        (float)(2 * CustomMath.sq(distanceFromCenter) * Math.cos(Math.toRadians(120)));
        float hypot = 121.244f;
        List<Vector2f> centroids = new ArrayList<>();
        for(int i = 0; i < 6; i++){
            Vector2f unitVector = GetUnitVectorOnCircle((i*2)+1, 12);
            Vector2f position = TransformToWorldSpace(unitVector, hypot);
            position.x = CustomMath.round(position.x, 3);
            position.y = CustomMath.round(position.y, 3);
            generateSimpleMesh(position, 6);
            centroids.add(position);
        }
        for(int i = 0; i < centroids.size(); i++){
            Vector2f unitVector = GetUnitVectorOnCircle((i*2)+1, 12);
            Vector2f position = TransformToWorldSpace(unitVector, hypot);
            position = position.add(centroids.get(i));
            position.x = CustomMath.round(position.x, 3);
            position.y = CustomMath.round(position.y, 3);
            generateSimpleMesh(position, 6);
        }
        System.out.println(nodes.size());
    }

    private void generateSimpleMesh(Vector2f center, int numberOfSides) {
        Vector2f unitVector;
        List<Node2D> entityNodes = new ArrayList<>();
        for (int i = 0; i < numberOfSides; i++) {
            Vector2f newPosition = center;
            unitVector = GetUnitVectorOnCircle(i+1, numberOfSides);
            newPosition = newPosition.add(TransformToWorldSpace(unitVector, distanceFromCenter));
            Node2D newNode = getNode2DFromPositionHash(newPosition);
            entityNodes.add(newNode);
        }
        entityNodes = subdivide(entityNodes, numSubdivisions);
        Entity e = new Entity("Cell " + cellList.size()).
                with(new SubdividedPolygonMesh().build(entityNodes)).
                with(new OsmosisForce()).
                with(new ElasticForce());
        cellList.add(e);
    }

    private Node2D getNode2DFromPositionHash(Vector2f newPosition) {
        boolean containsPosition = false;
        for(Vector2f position: positionToNodeMap.keySet())
        {
            if(position.approx(newPosition)) {
                containsPosition = true;
                newPosition = position;
                break;
            }
        }
        Node2D newNode;
        if(!containsPosition) {
            newNode = new Node2D(newPosition);
            nodes.add(newNode);
            positionToNodeMap.put(newPosition, newNode);
        }
        else{
            newNode = positionToNodeMap.get(newPosition);
        }
        return newNode;
    }

    private List<Node2D> subdivide(List<Node2D> nodesToSubdivide, int numSubdivisions){
        int count = nodesToSubdivide.size();
        List<Node2D> entityNodes = new ArrayList<>();
        for(int i = 1; i < count; i++ ){
            entityNodes.add(nodesToSubdivide.get(i-1));
            List<Node2D> nodesToAdd = getSubdividedEdge(nodesToSubdivide.get(i-1), nodesToSubdivide.get(i), numSubdivisions);
            for(Node2D node: nodesToAdd){
                if(!entityNodes.contains(node))entityNodes.add(node);
            }
        }
        entityNodes.add(nodesToSubdivide.get(count-1));
        List<Node2D> nodesToAdd = getSubdividedEdge(nodesToSubdivide.get(count-1), nodesToSubdivide.get(0), numSubdivisions);
        for(Node2D node: nodesToAdd){
            if(!entityNodes.contains(node))entityNodes.add(node);
        }
        return entityNodes;
    }

    private List<Node2D> getSubdividedEdge(Node2D nodeA, Node2D nodeB, int numSubdivisions) {
        List<Vector2f> positions = new ArrayList<>();
        Vector2f a = nodeA.getPosition();
        Vector2f b = nodeB.getPosition();
        float edgeLength = Vector2f.dist(a, b)/numSubdivisions;
        Vector2f oldPosition = a.copy();
        Vector2f segment = Vector2f.unit(a,b).mul(edgeLength);
        for(int j = 0; j < numSubdivisions-1; j++){
            Vector2f newPosition = oldPosition.add(segment);
            positions.add(newPosition);
            oldPosition = newPosition;
        }
        List<Node2D> subdividedNodes = new ArrayList<>();
        for(Vector2f pos : positions){
            Node2D nodeToAdd = getNode2DFromPositionHash(pos);
            if(!subdividedNodes.contains(nodeToAdd)) {
                subdividedNodes.add(nodeToAdd);
            }
        }
        return subdividedNodes;
    }

    @Override
    public Entity returnCellContainingPoint(Vector2f vector2f) {
        for (Entity cell : cellList) {
            if (cell.getComponent(Mesh.class).collidesWithPoint(vector2f)) {
                return cell;
            }
        }
        return parent;
    }

    private void selectAll(Component component){
        if(component ==this){
            SelectionEvents.selectEntities(cellList);
        }
    }

    @Override
    public void onDestroy() {
        onSelectionButtonPressed.unSubscribe(this::selectAll);
    }
}
