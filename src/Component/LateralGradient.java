package Component;

import Framework.Object.Entity;
import Framework.Object.EntityGroup;
import Framework.Utilities.Debug;
import Input.SelectionEvents;
import Annotations.GroupSelector;


import java.awt.*;
import java.util.ArrayList;

import static Component.Mesh.onMeshRebuilt;
import static Input.SelectionEvents.onSelectionButtonPressed;
import static Renderer.Renderer.DEFAULT_COLOR;

@GroupSelector
public class LateralGradient extends CellGradient {

    public int constrictingCellsStartLocation = 7;
    public float constantCeiling = 15f;
    public float ratioCeiling = 0.7f;
    public float onsetTime = 300f;


    @Override
    public void awake() {
        numberOfConstrictingCells = 35;
        groupColor = Color.BLUE;
        onMeshRebuilt.subscribe(this::recalculate);
        onSelectionButtonPressed.subscribe(this::selectAllInGroup);
    }

    @Override
    public void onValidate(){
        if(cellGroup==null) {
            cellGroup = new EntityGroup(new ArrayList<>(), "latrl", groupColor);
        }else{
            cellGroup.changeGroupColor(groupColor);
        }
        addCellsToGroup();
        cellGroup.recolor();
    }

    private void recalculate(Mesh mesh){
        if(mesh == getComponent(Mesh.class)){
            addCellsToGroup();
            cellGroup.recolor();
        }
    }

    @Override
    protected void addCellsToGroup() {
        if(getComponent(ApicalGradient.class)!= null){
            constrictingCellsStartLocation = Math.max(constrictingCellsStartLocation,
                    getComponent(ApicalGradient.class).numberOfConstrictingCells/2);
        }
        for(Entity cell: getChildren()) {
            RingCellMesh ringCellMesh = cell.getComponent(RingCellMesh.class);
            if(ringCellMesh == null){
                Debug.LogError("Mesh type mismatch: cannot use this component without a RingCellMesh");
                return;
            }
            int ringLocation = ringCellMesh.ringLocation;
            if (ringLocation >= constrictingCellsStartLocation
                    && ringLocation < constrictingCellsStartLocation + numberOfConstrictingCells) {
                if (cell.getComponent(LateralShorteningSpringForce.class) == null) {
                    cell.addComponent(new LateralShorteningSpringForce());
                }
                cellGroup.add(cell);
                LateralShorteningSpringForce shorteningSpringForce = cell.getComponent(LateralShorteningSpringForce.class);
                if(shorteningSpringForce == null){
                    Debug.LogError("Missing force: LateralShorteningSpringForce must be added");
                    return;
                }
                shorteningSpringForce.constant = constantCeiling;
                shorteningSpringForce.targetLengthRatio = ratioCeiling;
                shorteningSpringForce.onsetTime = onsetTime;
            }
            else if(cell.getComponent(LateralShorteningSpringForce.class)!= null){
                cell.removeComponent(LateralShorteningSpringForce.class);
                cell.getComponent(MeshRenderer.class).setColor(DEFAULT_COLOR);
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
            cell.removeComponent(LateralShorteningSpringForce.class);
        }
    }
}
