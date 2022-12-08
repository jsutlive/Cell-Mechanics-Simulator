package Morphogenesis.Physics.CellGroups;

import Framework.Object.Component;
import Framework.Object.Entity;
import Input.SelectionEvents;
import Morphogenesis.Meshing.Mesh;
import Morphogenesis.Meshing.RingCellMesh;
import Morphogenesis.Meshing.RingMesh;
import Morphogenesis.Physics.Forces.GaussianGradient;
import Morphogenesis.Physics.Forces.Gradient;
import Morphogenesis.Physics.Spring.ApicalConstrictingSpringForce;
import Morphogenesis.ReloadComponentOnChange;
import Morphogenesis.Render.MeshRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static Renderer.Renderer.DEFAULT_COLOR;
import static Morphogenesis.Meshing.Mesh.onMeshRebuilt;
import static Input.SelectionEvents.onSelectionButtonPressed;

@ReloadComponentOnChange
@GroupSelector
public class ApicalGradient extends Component {

    transient List<Entity> cellGroup = new ArrayList<>();
    public int numberOfConstrictingCells = 12;
    public float mu = 0f;
    public float sigma = 0.8f;
    public float constantCeiling = 125.4f;
    public float constantFloor = 55f;
    public float ratioCeiling = 0.01f;
    public float ratioFloor = .05f;
    public Color groupColor = Color.MAGENTA;

    transient Gradient gradient = new GaussianGradient(mu, sigma);

    @Override
    public void awake() {
        onMeshRebuilt.subscribe(this::recalculate);
        onSelectionButtonPressed.subscribe(this::selectAllInGroup);
        if(numberOfConstrictingCells%2!=0)numberOfConstrictingCells++;
        calculateGradient();
        RingMesh mesh = getComponent(RingMesh.class);
        addCellsToGroup(mesh);
    }

    private void recalculate(Mesh mesh){
        if(mesh == getComponent(Mesh.class)){
            calculateGradient();
            addCellsToGroup(getComponent(RingMesh.class));
        }
    }

    private void selectAllInGroup(Component c){
        if(c == this) {
            SelectionEvents.selectEntities(cellGroup);
        }
    }

    public void calculateGradient(){
        cellGroup.clear();
        gradient.calculate(numberOfConstrictingCells,
                constantCeiling, ratioCeiling, constantFloor, ratioFloor);
    }

    private void addCellsToGroup(RingMesh mesh) {
        for(Entity cell: mesh.cellList){
            int ringLocation = cell.getComponent(RingCellMesh.class).ringLocation;
            if( ringLocation <= numberOfConstrictingCells / 2){
                ApicalConstrictingSpringForce apicalConstriction =
                        cell.getComponent(ApicalConstrictingSpringForce.class);
                if(apicalConstriction == null){
                    apicalConstriction = cell.addComponent(new ApicalConstrictingSpringForce());
                }
                apicalConstriction.setConstant(gradient.getConstants()[ringLocation- 1]);
                apicalConstriction.setTargetLengthRatio(gradient.getRatios()[ringLocation - 1]);
                cell.getComponent(MeshRenderer.class).setColor(groupColor);
                cellGroup.add(cell);
            }
            else{
                if(cell.getComponent(ApicalConstrictingSpringForce.class)!=null){
                    cell.removeComponent(ApicalConstrictingSpringForce.class);
                    cell.getComponent(MeshRenderer.class).setColor(DEFAULT_COLOR);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        onMeshRebuilt.unSubscribe(this::recalculate);
        onSelectionButtonPressed.unSubscribe(this::selectAllInGroup);
        for(Entity cell: cellGroup){
            cell.getComponent(MeshRenderer.class).setColor(getComponent(MeshRenderer.class).defaultColor);
            cell.removeComponent(ApicalConstrictingSpringForce.class);
        }
    }
}
