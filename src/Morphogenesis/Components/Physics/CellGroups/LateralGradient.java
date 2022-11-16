package Morphogenesis.Components.Physics.CellGroups;

import Framework.Object.Component;
import Framework.Object.Entity;
import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Components.Meshing.RingCellMesh;
import Morphogenesis.Components.Meshing.RingMesh;
import Morphogenesis.Components.Physics.Spring.LateralShorteningSpringForce;
import Morphogenesis.Components.ReloadComponentOnChange;
import Morphogenesis.Components.Render.MeshRenderer;
import Renderer.Graphics.Painter;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static Morphogenesis.Components.Meshing.Mesh.onMeshRebuilt;

@ReloadComponentOnChange
public class LateralGradient extends Component {

    List<Entity> cellGroup = new ArrayList<>();

    public int numberOfConstrictingCells = 20;

    //@Tooltip(text = "number of cells away from center")
    public int constrictingCellsStartLocation = 10;
    public float constantCeiling = 10f;
    public float ratioCeiling = 0.9f;
    public Color groupColor = Color.BLUE;


    @Override
    public void awake() {
        onMeshRebuilt.subscribe(this::recalculate);
        calculateParameters();
    }

    private void recalculate(Mesh mesh){
        if(mesh == getComponent(Mesh.class)){
            calculateParameters();
        }
    }

    private void calculateParameters() {
        RingMesh mesh = getComponent(RingMesh.class);
        if(getComponent(ApicalGradient.class)!= null){
            constrictingCellsStartLocation = Math.max(constrictingCellsStartLocation,
                    getComponent(ApicalGradient.class).numberOfConstrictingCells/2);
        }
        for(Entity cell: mesh.cellList) {
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
                cell.getComponent(MeshRenderer.class).setColor(groupColor);
            }
            else if(cell.getComponent(LateralShorteningSpringForce.class)!= null){
                cell.removeComponent(LateralShorteningSpringForce.class);
                cell.getComponent(MeshRenderer.class).setColor(Painter.DEFAULT_COLOR);
            }
        }
    }

    @Override
    public void onDestroy() {
        onMeshRebuilt.unSubscribe(this::recalculate);
        for(Entity cell: cellGroup){
            cell.removeComponent(LateralShorteningSpringForce.class);
        }
    }
}
