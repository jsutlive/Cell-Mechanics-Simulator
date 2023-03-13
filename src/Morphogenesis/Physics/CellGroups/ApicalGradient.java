package Morphogenesis.Physics.CellGroups;

import Framework.Object.Component;
import Framework.Object.Entity;
import Framework.Object.EntityGroup;
import Input.SelectionEvents;
import Morphogenesis.Meshing.Mesh;
import Morphogenesis.Meshing.RingCellMesh;
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

    transient EntityGroup cellGroup;
    public int numberOfConstrictingCells = 12;
    public float mu = 0f;
    public float sigma = 0.8f;
    public float constantCeiling = 450f;
    public float constantFloor = 200f;
    public float ratioCeiling = 0.01f;
    public float ratioFloor = .05f;
    public Color groupColor = Color.MAGENTA;

    transient Gradient gradient = new GaussianGradient(mu, sigma);

    @Override
    public void awake() {
        onMeshRebuilt.subscribe(this::recalculate);
        onSelectionButtonPressed.subscribe(this::selectAllInGroup);
        if(cellGroup==null) {
            cellGroup = new EntityGroup(new ArrayList<>(), "apicl", groupColor);
        }
        if(numberOfConstrictingCells%2!=0)numberOfConstrictingCells++;
        calculateGradient();
        addCellsToGroup();
        cellGroup.recolor();
    }

    private void recalculate(Mesh mesh){
        if(mesh == getComponent(Mesh.class)){
            calculateGradient();
            addCellsToGroup();
            cellGroup.recolor();
        }
    }

    private void selectAllInGroup(Component c){
        if(c == this) {
            SelectionEvents.selectGroup(cellGroup);
        }
    }

    public void calculateGradient(){
        gradient.calculate(numberOfConstrictingCells,
                constantCeiling, ratioCeiling, constantFloor, ratioFloor);
    }

    private void addCellsToGroup() {
        System.out.println("HERE");
        for(Entity cell: getChildren()){
            int ringLocation = cell.getComponent(RingCellMesh.class).ringLocation;
            if( ringLocation <= numberOfConstrictingCells / 2){
                ApicalConstrictingSpringForce apicalConstriction =
                        cell.getComponent(ApicalConstrictingSpringForce.class);
                if(apicalConstriction == null){
                    apicalConstriction = cell.addComponent(new ApicalConstrictingSpringForce());
                }
                apicalConstriction.setConstant(gradient.getConstants()[ringLocation- 1]);
                apicalConstriction.setTargetLengthRatio(gradient.getRatios()[ringLocation - 1]);
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
        SelectionEvents.deleteGroup(cellGroup.groupID);
        for(Entity cell: cellGroup.entities){
            cell.getComponent(MeshRenderer.class).setColor(getComponent(MeshRenderer.class).defaultColor);
            cell.removeComponent(ApicalConstrictingSpringForce.class);
        }
    }
}
