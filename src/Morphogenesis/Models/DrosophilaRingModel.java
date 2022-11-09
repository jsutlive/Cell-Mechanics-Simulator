package Morphogenesis.Models;

import Morphogenesis.Components.MouseSelector;
import Morphogenesis.Components.Physics.CellGroups.ApicalGradient;
import Morphogenesis.Components.Physics.CellGroups.LateralGradient;
import Morphogenesis.Components.Physics.Collision.CellRingCollider;
import Morphogenesis.Components.Physics.Collision.RigidBoundary;
import Morphogenesis.Components.Meshing.RingMesh;


public class DrosophilaRingModel extends Model {

    @Override
    public void awake() {
        this.name = "Physics System";
        if(getComponent(RingMesh.class)==null) {
            addComponent(new RingMesh());
        }
    }

    @Override
    public void start() {
        addComponent(new RigidBoundary());
        addComponent(new MouseSelector());
        addComponent(new ApicalGradient());
        addComponent(new LateralGradient());
        addComponent(new CellRingCollider());
    }
}
