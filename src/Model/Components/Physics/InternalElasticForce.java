package Model.Components.Physics;

import Model.Components.Lattice.Lattice;
import Model.Components.Meshing.CellMesh;
import Physics.Rigidbodies.Edge;

public class InternalElasticForce extends ElasticForce{
    @Override
    public void awake() {
        edges = getComponent(Lattice.class).edgeList;
    }
}
