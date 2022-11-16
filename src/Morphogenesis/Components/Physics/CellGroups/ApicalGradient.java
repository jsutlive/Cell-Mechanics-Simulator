package Morphogenesis.Components.Physics.CellGroups;

import Framework.Object.Component;
import Framework.Object.Entity;
import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Components.Meshing.RingCellMesh;
import Morphogenesis.Components.Meshing.RingMesh;
import Morphogenesis.Components.Physics.Forces.GaussianGradient;
import Morphogenesis.Components.Physics.Forces.Gradient;
import Morphogenesis.Components.Physics.Spring.ApicalConstrictingSpringForce;
import Morphogenesis.Components.ReloadComponentOnChange;
import Morphogenesis.Components.Render.MeshRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static Renderer.Graphics.Painter.DEFAULT_COLOR;
import static Morphogenesis.Components.Meshing.Mesh.onMeshRebuilt;

@ReloadComponentOnChange
public class ApicalGradient extends Component {

    List<Entity> cellGroup = new ArrayList<>();
    public int numberOfConstrictingCells = 12;
    public float mu = 0f;
    public float sigma = 0.8f;
    public float constantCeiling = 125.4f;
    public float constantFloor = 55f;
    public float ratioCeiling = 0.01f;
    public float ratioFloor = .05f;
    public Color groupColor = Color.MAGENTA;

    Gradient gradient = new GaussianGradient(mu, sigma);

    @Override
    public void awake() {
        onMeshRebuilt.subscribe(this::recalculate);
        if(numberOfConstrictingCells%2!=0)numberOfConstrictingCells++;
        calculateGradient();
        RingMesh mesh = getComponent(RingMesh.class);
        addCellsToGroup(mesh);
    }

    private void recalculate(Mesh mesh){
        if(mesh == getComponent(Mesh.class)){
            calculateGradient();
            addCellsToGroup(getComponent(RingMesh.class));
        }
    }

    public void calculateGradient(){
        cellGroup.clear();
        gradient.calculate(numberOfConstrictingCells,
                constantCeiling, ratioCeiling, constantFloor, ratioFloor);
    }

    private void addCellsToGroup(RingMesh mesh) {
        for(Entity cell: mesh.cellList){
            int ringLocation = cell.getComponent(RingCellMesh.class).ringLocation;
            if( ringLocation <= numberOfConstrictingCells / 2){
                if(cell.getComponent(ApicalConstrictingSpringForce.class)== null){
                    cell.addComponent(new ApicalConstrictingSpringForce());
                }
                ApicalConstrictingSpringForce apicalConstriction = cell.getComponent(ApicalConstrictingSpringForce.class);
                apicalConstriction.setConstant(gradient.getConstants()[ringLocation- 1]);
                apicalConstriction.setTargetLengthRatio(gradient.getRatios()[ringLocation - 1]);
                cell.getComponent(MeshRenderer.class).setColor(groupColor);
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
        for(Entity cell: cellGroup){
            cell.removeComponent(ApicalConstrictingSpringForce.class);
        }
    }
}
