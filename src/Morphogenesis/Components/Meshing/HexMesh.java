package Morphogenesis.Components.Meshing;

import Framework.Object.Component;
import Framework.Object.Entity;
import Input.SelectionEvents;
import Morphogenesis.Components.Physics.CellGroups.GroupSelector;
import Morphogenesis.Components.Physics.OsmosisForce;
import Morphogenesis.Components.Physics.Spring.ElasticForce;
import Morphogenesis.Components.Render.MeshRenderer;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static Input.SelectionEvents.onSelectionButtonPressed;
import static Utilities.Math.CustomMath.GetUnitVectorOnCircle;
import static Utilities.Math.CustomMath.TransformToWorldSpace;

@GroupSelector
public class HexMesh extends Mesh{

    int distanceFromCenter = 70;
    public List<Entity> cellList = new ArrayList<>();



    @Override
    public void awake() {
        onSelectionButtonPressed.subscribe(this::selectAll);
        buildPolygon(new Vector2f(0), 6, false);
        for(int i = 0; i < 6; i++){
            Vector2f unitVector = GetUnitVectorOnCircle((i*2)+1, 12);
            Vector2f position = TransformToWorldSpace(unitVector, distanceFromCenter * 1.8f);
            buildPolygon(position, 6, false);
        }
        for(int i = 0; i < 6; i++){
            Vector2f unitVector = GetUnitVectorOnCircle((i+1), 6);
            Vector2f position = TransformToWorldSpace(unitVector, distanceFromCenter * 3.1f);
            buildPolygon(position, 6, true);
        }
        for(int i = 0; i < 6; i++){
            Vector2f unitVector = GetUnitVectorOnCircle(((i*2)+1), 12);
            Vector2f position = TransformToWorldSpace(unitVector, distanceFromCenter * 3.6f);
            buildPolygon(position, 6, true);
        }
        for(Entity cell: cellList){
            for(Node2D node: cell.getComponent(Mesh.class).nodes){
                if(!nodes.contains(node)) nodes.add(node);
            }
        }
    }

    private void buildPolygon(Vector2f center, int numberOfSides, boolean isStatic) {
        List<Vector2f> positions = new ArrayList<>();
        List<Node2D> nodes = new ArrayList<>();
        Vector2f unitVector;
        for (int i = 0; i < numberOfSides; i++) {
            Vector2f newPosition = center;
            unitVector = GetUnitVectorOnCircle(i+1, numberOfSides);
            positions.add(newPosition.add(TransformToWorldSpace(unitVector, distanceFromCenter-0.05f)));
            nodes.add(new Node2D(positions.get(i)));
        }
        if(isStatic){
            Entity e = new Entity("Cell " + cellList.size()).
                    with(new SubdividedPolygonMesh().build(nodes, 6).setStatic()
                    );
            e.getComponent(MeshRenderer.class).setColor(Color.RED);
            cellList.add(e);
        }
        else {
            cellList.add(new Entity("Cell " + cellList.size()).
                    with(new SubdividedPolygonMesh().build(nodes, 6)).
                    with(new OsmosisForce()).
                    with(new ElasticForce())
            );
        }
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
            SelectionEvents.selectEntity(cellList.get(0));
            SelectionEvents.beginSelectingMultiple();
            for(Entity e: cellList) SelectionEvents.selectEntity(e);
            SelectionEvents.cancelSelectingMultiple();
        }
    }

    @Override
    public void onDestroy() {
        onSelectionButtonPressed.unSubscribe(this::selectAll);
    }
}
