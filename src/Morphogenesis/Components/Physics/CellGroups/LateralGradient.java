package Morphogenesis.Components.Physics.CellGroups;

import Framework.Object.Component;
import Morphogenesis.Components.Meshing.RingMesh;
import Morphogenesis.Components.Physics.Spring.LateralShorteningSpringForce;
import Morphogenesis.Components.ReloadComponentOnChange;
import Morphogenesis.Entities.Cell;

public class LateralGradient extends Component {
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
        for(Cell cell: mesh.cellList) {
            if(cell.getComponent(LateralShorteningSpringForce.class) != null){
                cell.getComponent(LateralShorteningSpringForce.class).constant = constantCeiling;
                cell.getComponent(LateralShorteningSpringForce.class).targetLengthRatio = ratioCeiling;
            }
        }
    }
}
