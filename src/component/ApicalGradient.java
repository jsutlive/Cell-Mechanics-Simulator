package component;

import framework.object.Entity;
import framework.object.EntityGroup;
import framework.utilities.Debug;
import input.SelectionEvents;
import annotations.GroupSelector;

import java.awt.*;
import java.util.ArrayList;

import static renderer.Renderer.DEFAULT_COLOR;
import static component.Mesh.onMeshRebuilt;
import static input.SelectionEvents.onSelectionButtonPressed;

@GroupSelector
public class ApicalGradient extends CellGradient {

    public float mu = 0f;
    public float sigma = 0.8f;
    public float constantCeiling = 450f;
    public float constantFloor = 200f;
    public float ratioCeiling = 0.01f;
    public float ratioFloor = .05f;


    @Override
    public void awake() {
        numberOfConstrictingCells = 12;
        gradient = new GaussianGradient(mu, sigma);
        groupColor = Color.MAGENTA;
        onMeshRebuilt.subscribe(this::recalculate);
        onSelectionButtonPressed.subscribe(this::selectAllInGroup);
    }

    @Override
    public void onValidate() {
        if(cellGroup==null) {
            cellGroup = new EntityGroup(new ArrayList<>(), "apicl", groupColor);
        }else{
            cellGroup.changeGroupColor(groupColor);
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

    public void calculateGradient(){
        gradient.calculate(numberOfConstrictingCells,
                constantCeiling, ratioCeiling, constantFloor, ratioFloor);
    }

    @Override
    protected void addCellsToGroup() {
        for(Entity cell: getChildren()){
            RingCellMesh ringCellMesh = cell.getComponent(RingCellMesh.class);
            if(ringCellMesh == null){
                Debug.LogError("Mesh type mismatch: cannot use this component without a RingCellMesh");
                return;
            }
            int ringLocation = ringCellMesh.ringLocation;
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
            MeshRenderer renderer = getComponent(MeshRenderer.class);
            if(renderer!= null) {
                renderer.setColor(renderer.defaultColor);
            }
            cell.removeComponent(ApicalConstrictingSpringForce.class);
        }
    }
}
