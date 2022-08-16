package Model.Components.Physics;

import Model.Components.Meshing.CellMesh;

public class ElasticForce extends SpringForce{
    @Override
    public void setup() {
        CellMesh mesh = (CellMesh) parent.getComponent(CellMesh.class);
        edges = mesh.edges;
        targetLengthRatio = 1;
    }


}
