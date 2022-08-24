package Model.Components.Physics.ForceVector;

import Model.Cells.Cell;
import Model.Components.Meshing.CellMesh;
import Model.Components.Physics.Collider;
import Physics.Rigidbodies.Node;

import java.util.List;

public class CellRingCollider extends Collider {
    List<Cell> cells;

    @Override
    public void update() {
        for(Node node: nodes){
            for(Cell cell: cells){
                CellMesh mesh = (CellMesh) cell.getComponent(CellMesh.class);
                if(mesh.contains(node)) continue;
                if(mesh.collidesWithNode(node)){

                }
            }
        }
    }
}
