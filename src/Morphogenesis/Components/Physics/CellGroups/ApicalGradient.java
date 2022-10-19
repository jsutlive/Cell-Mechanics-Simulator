package Morphogenesis.Components.Physics.CellGroups;

import Framework.Object.Component;
import Morphogenesis.Components.Meshing.RingMesh;
import Morphogenesis.Components.Physics.Forces.GaussianGradient;
import Morphogenesis.Components.Physics.Forces.Gradient;
import Morphogenesis.Components.Physics.Spring.ApicalConstrictingSpringForce;
import Morphogenesis.Entities.Cell;

import java.util.ArrayList;
import java.util.List;

public class ApicalGradient extends Component {

    List<Cell> cellGroup = new ArrayList<>();
    int numberOfConstrictingCells = 18;
    public float mu = 0f;
    public float sigma = 0.8f;
    public float constantCeiling = 75.4f;
    public float constantFloor = 5f;
    public float ratioCeiling = 0.01f;
    public float ratioFloor = .05f;
    Gradient gradient = new GaussianGradient(mu, sigma);

    @Override
    public void awake() {
        gradient.calculate(numberOfConstrictingCells,
                constantCeiling, ratioCeiling, constantFloor, ratioFloor);
        RingMesh mesh = getComponent(RingMesh.class);
        for(Cell cell: mesh.cellList){
            if(cell.getRingLocation() < numberOfConstrictingCells/2){
                cellGroup.add(cell);
                //ApicalConstrictingSpringForce apicalConstriction = new ApicalConstrictingSpringForce();
                cell.getComponent(ApicalConstrictingSpringForce.class).setConstant
                        (gradient.getConstants()[cell.getRingLocation() - 1]);
                cell.getComponent(ApicalConstrictingSpringForce.class).
                        setTargetLengthRatio(gradient.getRatios()[cell.getRingLocation() - 1]);

            }
        }
    }
}
