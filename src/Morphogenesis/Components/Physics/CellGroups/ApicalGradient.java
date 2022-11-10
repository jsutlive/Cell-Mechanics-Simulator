package Morphogenesis.Components.Physics.CellGroups;

import Framework.Object.Component;
import Morphogenesis.Components.Meshing.RingMesh;
import Morphogenesis.Components.Physics.Forces.GaussianGradient;
import Morphogenesis.Components.Physics.Forces.Gradient;
import Morphogenesis.Components.Physics.Spring.ApicalConstrictingSpringForce;
import Morphogenesis.Components.ReloadComponentOnChange;
import Morphogenesis.Components.Render.CellRenderer;
import Morphogenesis.Entities.Cell;
import Renderer.Graphics.Painter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ApicalGradient extends Component {

    List<Cell> cellGroup = new ArrayList<>();
    @ReloadComponentOnChange
    public int numberOfConstrictingCells = 12;
    @ReloadComponentOnChange
    public float mu = 0f;
    @ReloadComponentOnChange
    public float sigma = 0.8f;
    @ReloadComponentOnChange
    public float constantCeiling = 125.4f;
    @ReloadComponentOnChange
    public float constantFloor = 55f;
    @ReloadComponentOnChange
    public float ratioCeiling = 0.01f;
    @ReloadComponentOnChange
    public float ratioFloor = .05f;
    @ReloadComponentOnChange
    public Color groupColor = Color.MAGENTA;

    Gradient gradient = new GaussianGradient(mu, sigma);

    @Override
    public void awake() {
        calculateGradient();
        RingMesh mesh = getComponent(RingMesh.class);
        addCellsToGroup(mesh);
    }
    public void calculateGradient(){
        cellGroup.clear();
        gradient.calculate(numberOfConstrictingCells,
                constantCeiling, ratioCeiling, constantFloor, ratioFloor);
    }

    private void addCellsToGroup(RingMesh mesh) {
        for(Cell cell: mesh.cellList){
            if(cell.getRingLocation() <= numberOfConstrictingCells / 2){
                if(cell.getComponent(ApicalConstrictingSpringForce.class)== null){
                    cell.addComponent(new ApicalConstrictingSpringForce());
                }
                ApicalConstrictingSpringForce apicalConstriction = cell.getComponent(ApicalConstrictingSpringForce.class);
                apicalConstriction.setConstant(gradient.getConstants()[cell.getRingLocation() - 1]);
                apicalConstriction.setTargetLengthRatio(gradient.getRatios()[cell.getRingLocation() - 1]);
                cell.getComponent(CellRenderer.class).setColor(groupColor);
                cellGroup.add(cell);
            }
            else{
                if(cell.getComponent(ApicalConstrictingSpringForce.class)!=null){
                    cell.removeComponent(ApicalConstrictingSpringForce.class);
                    cell.getComponent(CellRenderer.class).setColor(Painter.DEFAULT_COLOR);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        for(Cell cell: cellGroup){
            cell.removeComponent(ApicalConstrictingSpringForce.class);
        }
    }
}
