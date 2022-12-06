package Framework.Object;

import Input.SelectionEvents;
import Morphogenesis.Components.Meshing.HexMesh;
import Morphogenesis.Components.Meshing.RingMesh;
import Morphogenesis.Components.MouseSelector;
import Morphogenesis.Components.Physics.CellGroups.ApicalGradient;
import Morphogenesis.Components.Physics.CellGroups.LateralGradient;
import Morphogenesis.Components.Physics.Collision.CellRingCollider;
import Morphogenesis.Components.Physics.Collision.MeshCollider;
import Morphogenesis.Components.Physics.Collision.RigidBoundary;
import Morphogenesis.Components.Physics.Collision.RingStiffness2D;
import Morphogenesis.Components.Yolk;

import static Framework.Object.Tag.MODEL;

public class ModelLoader {

    public static Entity loadDrosophilaEmbryo(){
        Entity e = new Entity("Physics System", 0, MODEL).
                with(new RingMesh()).
                with(new MouseSelector()).
                with(new ApicalGradient()).
                with(new LateralGradient()).
                with(new CellRingCollider()).
                with(new RigidBoundary()).
                with(new RingStiffness2D()).
                with(new Yolk()
                );
        SelectionEvents.selectEntity(e);
        return e;
    }

    public static Entity loadHexMesh(){
        Entity e =  new Entity("Physics System", 0, MODEL).
                with(new HexMesh()).
                with(new MouseSelector()).
                with(new MeshCollider()
                );
        SelectionEvents.selectEntity(e);
        return e;
    }

    public static Entity loadDebugMesh(){
        Entity e = new Entity("Box", 0, MODEL);
        SelectionEvents.selectEntity(e);
        return e;
    }
}
