package Framework.Object;

import Component.*;
import Framework.Utilities.Debug;
import Input.SelectionEvents;
import Component.ElasticForce;
import Component.Yolk;

import static Framework.Object.Tag.MODEL;

public class ModelLoader {

    public static Entity loadDrosophilaEmbryo(){
        Entity e = new Entity("Drosophila Embryo Ring", 0, MODEL).
                with(new RingMesh()).
                with(new ApicalGradient()).
                with(new LateralGradient()).
                with(new MeshCollider()).
                with(new RigidBoundary()).
                with(new Yolk()
                );
        SelectionEvents.selectEntity(e);
        Debug.Log("Loaded Drosophila embryo");
        return e;
    }

    public static Entity loadHexMesh(){
        Entity e =  new Entity("Cell Sheet", 0, MODEL).
                with(new HexMesh()).
                with(new MeshCollider()
                );
        SelectionEvents.selectEntity(e);
        Debug.Log("Loaded hexagon mesh");
        return e;
    }

    public static Entity loadDebugMesh(){
        Entity e = new Entity("Box", 0, MODEL).
                with(new BoxDebugMesh().build()).
                with(new ElasticForce()).
                with(new MeshStiffness2D());
        SelectionEvents.selectEntity(e);
        Debug.Log("Loaded debugger mesh");
        return e;
    }
}
