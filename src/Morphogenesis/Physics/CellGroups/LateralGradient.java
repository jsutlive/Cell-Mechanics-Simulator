package Morphogenesis.Physics.CellGroups;

import Framework.Object.Component;
import Framework.Object.Entity;
import Framework.Object.EntityGroup;
import Input.SelectionEvents;
import Morphogenesis.Meshing.Mesh;
import Morphogenesis.Meshing.RingCellMesh;
import Morphogenesis.Physics.Spring.LateralShorteningSpringForce;
import Morphogenesis.ReloadComponentOnChange;
import Morphogenesis.Render.MeshRenderer;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static Morphogenesis.Meshing.Mesh.onMeshRebuilt;
import static Input.SelectionEvents.onSelectionButtonPressed;
import static Renderer.Renderer.DEFAULT_COLOR;

@ReloadComponentOnChange
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
        if(cellGroup==null) {
            cellGroup = new EntityGroup(new ArrayList<>(), "latrl", groupColor);
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
            cell.getComponent(MeshRenderer.class).setColor(getComponent(MeshRenderer.class).defaultColor);
            cell.removeComponent(LateralShorteningSpringForce.class);
        }
    }
}
