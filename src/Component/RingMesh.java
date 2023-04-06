package Component;

import Framework.Object.Annotations.DoNotDestroyInGUI;
import Framework.Object.Annotations.DoNotEditWhilePlaying;
import Framework.Object.Entity;
import Input.SelectionEvents;
import Annotations.GroupSelector;
import Annotations.ReloadComponentOnChange;
import Annotations.DoNotEditInGUI;
import Framework.Rigidbodies.Edge;
import Framework.Rigidbodies.Node2D;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Math.Gauss;

import java.util.ArrayList;
import java.util.Collections;

import static Utilities.Math.CustomMath.TransformToWorldSpace;
import static Utilities.Math.CustomMath.GetUnitVectorOnCircle;
import static Input.SelectionEvents.onSelectionButtonPressed;

@ReloadComponentOnChange
@DoNotDestroyInGUI
@DoNotEditWhilePlaying
@GroupSelector
public class RingMesh extends Mesh {

    @DoNotEditInGUI
    public int lateralResolution = 4;
    public int segments = 80;
    public float outerRadius = 300;
    public float innerRadius = 200;

    public transient ArrayList<Node2D> outerNodes = new ArrayList<>();
    public transient ArrayList<Node2D> innerNodes = new ArrayList<>();
    public transient ArrayList<Edge> basalEdges = new ArrayList<>();
    public transient ArrayList<Edge> apicalEdges = new ArrayList<>();

    @Override
    public void awake() {
        onSelectionButtonPressed.subscribe(this::selectAll);
    }

    @Override
    public void onValidate() {
        resetCells();
        nodes.clear();
        generateTissueRing();
        setApicalAndBasalEdges();
        onMeshRebuilt.invoke(this);
    }

    private void selectAll(Component component){
        if(component ==this){
            SelectionEvents.selectEntities(parent.children);
        }
    }

    private void resetCells() {
        for(Entity cell: parent.children){
            cell.destroy();
        }
        parent.children.clear();
        outerNodes.clear();
        innerNodes.clear();
        basalEdges.clear();
        apicalEdges.clear();
    }

    private void setApicalAndBasalEdges() {
        for(Entity cell: parent.children)
        {
            RingCellMesh mesh = cell.getComponent(RingCellMesh.class);
            for(Node2D node: mesh.nodes){
                if(!node.getPosition().isNull()) {
                    if (!contains(node)) nodes.add(node);
                }
            }
            for(int i =0; i < mesh.edges.size(); i++){
                if(i == lateralResolution) {
                    Edge e = mesh.edges.get(i);
                    apicalEdges.add(e);
                    outerNodes.add((Node2D) e.getNodes()[0]);
                }
                if(i == mesh.edges.size()-1){
                    Edge e = mesh.edges.get(i);
                    basalEdges.add(e);
                    innerNodes.add((Node2D) e.getNodes()[0]);
                }
            }
        }
    }

    @Override
    protected void calculateArea() {
        area = Gauss.nShoelace(outerNodes) - Gauss.nShoelace(innerNodes);
    }

    @Override
    public Entity returnCellContainingPoint(Vector2f vector2f) {
        return null;
    }

    public float getRadiusToNode(int j) {
        float radiusStep = (innerRadius - outerRadius) / lateralResolution;
        return outerRadius + radiusStep * j;
    }

    public void generateTissueRing() {
        Vector2f position, unitVector;
        ArrayList<Entity> mirroredCells = new ArrayList<>();
        ArrayList<Node2D> oldNodes = new ArrayList<>();
        ArrayList<Node2D> oldMirroredNodes = new ArrayList<>();

        //make lateral edges
        for (int i = 0; i < (segments / 2) + 1; i++) {

            ArrayList<Node2D> nodes = new ArrayList<>();
            ArrayList<Node2D> mirroredNodes = new ArrayList<>();

            unitVector = GetUnitVectorOnCircle(i, segments);

            for (int j = 0; j <= lateralResolution; j++) {
                float radiusToNode = getRadiusToNode(j);
                // Transform polar to world coordinates
                position = TransformToWorldSpace(unitVector, radiusToNode);
                Node2D currentNode = new Node2D(position);
                Node2D mirroredNode = currentNode.clone();
                mirroredNode.mirrorAcrossYAxis();

                nodes.add(currentNode);
                mirroredNodes.add(mirroredNode);
            }
            Collections.reverse(mirroredNodes);
            Collections.reverse(oldNodes);

            // Build the first set of cells in the cell ring
            Entity newCell;
            if (i >= 1) {

                ArrayList<Node2D> cellNodes = new ArrayList<>(mirroredNodes);
                cellNodes.addAll(oldMirroredNodes);
                newCell = getNewCell(cellNodes);
                newCell.getComponent(RingCellMesh.class).ringLocation = i;
                mirroredCells.add(newCell);
                if(i != 1) {
                    cellNodes = new ArrayList<>(oldNodes);
                }else {
                    Collections.reverse(oldMirroredNodes);
                    cellNodes = new ArrayList<>(oldMirroredNodes);
                }
                if(i == segments/2){
                    Collections.reverse(mirroredNodes);
                    cellNodes.addAll(mirroredNodes);
                }else{
                    cellNodes.addAll(nodes);
                }
                newCell = getNewCell(cellNodes);
                newCell.getComponent(RingCellMesh.class).ringLocation = i;
                newCell.setParent(parent);
            }
            Collections.reverse(mirroredNodes);
            Collections.reverse(oldNodes);
            oldMirroredNodes = new ArrayList<>(mirroredNodes);
            oldNodes = new ArrayList<>(nodes);
            nodes.clear();
            mirroredNodes.clear();
        }
        Collections.reverse(mirroredCells);
        for(Entity e: mirroredCells) e.setParent(parent);
    }

    private Entity getNewCell(ArrayList<Node2D> cellNodes) {
        return new Entity("Cell " + (parent.children.size())).
                with(new RingCellMesh().build(cellNodes)).
                with(new BasalRigidityLossSpringForce()).
                //with(new CornerStiffness2D()).
                //with(new EdgeStiffness2D()).
                with(new MeshStiffness2D()).
                with(new OsmosisForce());
    }

    @Override
    public void onDestroy() {
        onSelectionButtonPressed.unSubscribe(this::selectAll);
    }
}
