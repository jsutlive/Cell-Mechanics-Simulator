package Component;

import Framework.Object.Entity;
import Framework.Object.EntityGroup;
import Input.SelectionEvents;
import Annotations.GroupSelector;


import java.awt.*;
import java.util.ArrayList;

import static Component.Mesh.onMeshRebuilt;
import static Input.SelectionEvents.onSelectionButtonPressed;
import static Renderer.Renderer.DEFAULT_COLOR;

@GroupSelector
public class LateralGradient extends Component {

    transient EntityGroup cellGroup;

    public int numberOfConstrictingCells = 35;

    //@Tooltip(text = "number of cells away from center")
    public int constrictingCellsStartLocation = 7;
    public float constantCeiling = 15f;
    public float ratioCeiling = 0.7f;
    public Color groupColor = Color.BLUE;
    public float onsetTime = 300f;


    @Override
    public void awake() {
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
        calculateParameters();
        cellGroup.recolor();
    }

    private void recalculate(Mesh mesh){
        if(mesh == getComponent(Mesh.class)){
            calculateParameters();
        }
    }

    private void selectAllInGroup(Component c){
        if(c == this) {
            SelectionEvents.selectGroup(cellGroup);
        }
    }

    private void calculateParameters() {
        if(getComponent(ApicalGradient.class)!= null){
            constrictingCellsStartLocation = Math.max(constrictingCellsStartLocation,
                    getComponent(ApicalGradient.class).numberOfConstrictingCells/2);
        }
        for(Entity cell: getChildren()) {
            int ringLocation = cell.getComponent(RingCellMesh.class).ringLocation;
            if (ringLocation >= constrictingCellsStartLocation
                    && ringLocation < constrictingCellsStartLocation + numberOfConstrictingCells) {
                if (cell.getComponent(LateralShorteningSpringForce.class) == null) {
                    cell.addComponent(new LateralShorteningSpringForce());
                }
                cellGroup.add(cell);
                LateralShorteningSpringForce shorteningSpringForce = cell.getComponent(LateralShorteningSpringForce.class);
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
