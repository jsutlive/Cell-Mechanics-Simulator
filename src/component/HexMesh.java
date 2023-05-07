package component;

import framework.object.annotations.DoNotDestroyInGUI;
import framework.object.Entity;
import input.SelectionEvents;
import annotations.GroupSelector;
import framework.rigidbodies.Node2D;
import utilities.geometry.Vector.Vector2f;
import utilities.math.CustomMath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static input.SelectionEvents.onSelectionButtonPressed;
import static utilities.math.CustomMath.*;

@GroupSelector
@DoNotDestroyInGUI
public class HexMesh extends Mesh{

    public int numSubdivisions = 5;
    public int sidesPerCell = 6;
    int distanceFromCenter = 70;
    HashMap<Vector2f, Node2D> positionToNodeMap = new HashMap<>();
    List<Vector2f> allCentroids = new ArrayList<>();


    private void removeEntityFromList(Entity e){
        getChildren().remove(e);
    }

    @Override
    public void awake() {
        onSelectionButtonPressed.subscribe(this::selectAll);
        Entity.onRemoveEntity.subscribe(this::removeEntityFromList);
    }

    @Override
    public void onValidate() {
        for(int i = getChildren().size()-1; i >= 0; i--){
            getChildren().get(i).destroy();
        }
        allCentroids.clear();

        positionToNodeMap.clear();
        generateSimpleMesh(new Vector2f(0), sidesPerCell);
        allCentroids.add(new Vector2f(0));
        //float hypot = 121.244f;
        float sinAngle = (float) Math.sin(Math.toRadians(((sidesPerCell-2)*180)/sidesPerCell));
        float hypot = (distanceFromCenter * sinAngle) * 2;
        List<Vector2f> centroids = new ArrayList<>();
        for(int i = 0; i < sidesPerCell; i++){
            Vector2f unitVector = GetUnitVectorOnCircle((i*2)+1, sidesPerCell * 2);
            Vector2f position = TransformToWorldSpace(unitVector, hypot);
            position.x = CustomMath.round(position.x, 3);
            position.y = CustomMath.round(position.y, 3);
            generateSimpleMesh(position, sidesPerCell);
            centroids.add(position);
            allCentroids.add(position);
        }
        List<Vector2f> centroids2 = new ArrayList<>();
        for(int i = 0; i < centroids.size(); i++){
            for(int j = 0; j < sidesPerCell; j++) {
                Vector2f unitVector = GetUnitVectorOnCircle((j * 2) + 1, sidesPerCell * 2);
                Vector2f position = TransformToWorldSpace(unitVector, hypot);
                position = position.add(centroids.get(i));
                position.x = CustomMath.round(position.x, 3);
                position.y = CustomMath.round(position.y, 3);
                if(isNewCentroidPosition(position)){
                    centroids2.add(position);
                    generateSimpleMesh(position, sidesPerCell);
                    allCentroids.add(position);
                }
            }
        }
        List<Vector2f> centroids3 = new ArrayList<>();
        for(int i = 0; i < centroids2.size(); i++){
            for(int j = 0; j < sidesPerCell; j++) {
                Vector2f unitVector = GetUnitVectorOnCircle((j * 2) + 1, sidesPerCell * 2);
                Vector2f position = TransformToWorldSpace(unitVector, hypot);
                position = position.add(centroids2.get(i));
                position.x = CustomMath.round(position.x, 3);
                position.y = CustomMath.round(position.y, 3);
                if(isNewCentroidPosition(position)) {
                    centroids3.add(position);
                    generateSimpleMesh(position, sidesPerCell);
                    allCentroids.add(position);
                }
            }
        }
        List<Vector2f> centroids4 = new ArrayList<>();
        for(int i = 0; i < centroids3.size(); i++){
            for(int j = 0; j < sidesPerCell; j++) {
                Vector2f unitVector = GetUnitVectorOnCircle((j * 2) + 1, sidesPerCell * 2);
                Vector2f position = TransformToWorldSpace(unitVector, hypot);
                position = position.add(centroids3.get(i));
                position.x = CustomMath.round(position.x, 3);
                position.y = CustomMath.round(position.y, 3);
                if(isNewCentroidPosition(position)) {
                    centroids4.add(position);
                    generateSimpleMesh(position, sidesPerCell);
                    allCentroids.add(position);
                }
            }
        }
        for(int i = 0; i < centroids4.size(); i++){
            for(int j = 0; j < sidesPerCell; j++) {
                Vector2f unitVector = GetUnitVectorOnCircle((j * 2) + 1, sidesPerCell * 2);
                Vector2f position = TransformToWorldSpace(unitVector, hypot);
                position = position.add(centroids4.get(i));
                position.x = CustomMath.round(position.x, 3);
                position.y = CustomMath.round(position.y, 3);
                if(isNewCentroidPosition(position)) {
                    generateSimpleMesh(position, sidesPerCell);
                    allCentroids.add(position);
                }
            }
        }
    }

    private boolean isNewCentroidPosition(Vector2f pos) {
        for(Vector2f v : allCentroids){
            if(v.approx(pos)) return false;
        }
        return true;
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
        Entity e = new Entity("Cell " + getChildren().size()).
                with(new SubdividedPolygonMesh().build(entityNodes)).
                with(new OsmosisForce()).
                with(new ElasticForce());
        e.setParent(parent);
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
        /*for (Entity cell : cellList) {
            if (cell.getComponent(Mesh.class).collidesWithPoint(vector2f)) {
                return cell;
            }
        }*/
        return null;
    }

    private void selectAll(Component component){
        if(component ==this){
            SelectionEvents.selectEntities(getChildren());
        }
    }

    @Override
    public void onDestroy() {
        onSelectionButtonPressed.unSubscribe(this::selectAll);
        Entity.onRemoveEntity.unSubscribe(this::removeEntityFromList);

    }
}
