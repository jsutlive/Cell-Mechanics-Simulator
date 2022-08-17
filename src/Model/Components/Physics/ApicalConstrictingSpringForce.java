package Model.Components.Physics;

import Model.Cells.Cell;
import Model.Components.Meshing.CellMesh;
import Model.Model;
import Physics.Forces.Gradient;
import Physics.Rigidbodies.ApicalEdge;
import Physics.Rigidbodies.Edge;

public class ApicalConstrictingSpringForce extends SpringForce{
    @Override
    public void setup() {
        CellMesh mesh = (CellMesh) parent.getComponent(CellMesh.class);
        for(Edge edge : mesh.edges) if (edge instanceof ApicalEdge) edges.add(edge);

        Gradient gr = Model.apicalGradient;
        Cell cell = (Cell) parent;
        if(gr == null || cell == null) return;
        setTargetLengthRatio(1 - gr.getRatios()[cell.getRingLocation() - 1]);

    }
}
