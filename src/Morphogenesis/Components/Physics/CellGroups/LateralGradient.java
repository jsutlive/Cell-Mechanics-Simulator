package Morphogenesis.Components.Physics.CellGroups;

import Framework.Object.Component;
import Morphogenesis.Components.Meshing.RingMesh;
import Morphogenesis.Components.Physics.Spring.LateralShorteningSpringForce;
import Morphogenesis.Components.ReloadComponentOnChange;
import Morphogenesis.Components.ReloadEntityOnChange;
import Morphogenesis.Components.Render.CellRenderer;
import Morphogenesis.Entities.Cell;
import Renderer.Graphics.Painter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LateralGradient extends Component {

    List<Cell> cellGroup = new ArrayList<>();

    @ReloadComponentOnChange
    public int numberOfConstrictingCells = 20;

    @ReloadComponentOnChange
    //@Tooltip(text = "number of cells away from center")
    public int constrictingCellsStartLocation = 10;

    @ReloadComponentOnChange
    public float constantCeiling = 10f;
    @ReloadComponentOnChange
    public float ratioCeiling = 0.9f;


    @Override
    public void awake() {
        calculateParameters();
    }

    private void calculateParameters() {
        RingMesh mesh = getComponent(RingMesh.class);
        if(getComponent(ApicalGradient.class)!= null){
            constrictingCellsStartLocation = Math.max(constrictingCellsStartLocation,
                    getComponent(ApicalGradient.class).numberOfConstrictingCells/2);
        }
        for(Cell cell: mesh.cellList) {
            if (cell.getRingLocation() >= constrictingCellsStartLocation
                    && cell.getRingLocation() < constrictingCellsStartLocation + numberOfConstrictingCells) {
                if (cell.getComponent(LateralShorteningSpringForce.class) == null) {
                    cell.addComponent(new LateralShorteningSpringForce());
                }
                cellGroup.add(cell);
                LateralShorteningSpringForce shorteningSpringForce = cell.getComponent(LateralShorteningSpringForce.class);
                shorteningSpringForce.constant = constantCeiling;
                shorteningSpringForce.targetLengthRatio = ratioCeiling;
                cell.getComponent(CellRenderer.class).setColor(Color.BLUE);
            }
            else if(cell.getComponent(LateralShorteningSpringForce.class)!= null){
                cell.removeComponent(LateralShorteningSpringForce.class);
                cell.getComponent(CellRenderer.class).setColor(Painter.DEFAULT_COLOR);
            }
        }
    }

    @Override
    public void onDestroy() {
        for(Cell cell: cellGroup){
            cell.removeComponent(LateralShorteningSpringForce.class);
        }
    }
}
