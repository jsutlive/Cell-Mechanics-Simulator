package Framework.States;

import Framework.Object.Component;
import Framework.Object.Entity;
import Morphogenesis.Components.DebuggerComponent;
import Morphogenesis.Components.Meshing.CircleMesh;
import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Components.Meshing.SubdividedPolygonMesh;
import Morphogenesis.Components.Physics.OsmosisForce;
import Morphogenesis.Components.Physics.Spring.ElasticForce;
import Morphogenesis.Rigidbodies.Node2D;

import java.util.ArrayList;
import java.util.Objects;

import static Framework.Object.Tag.MODEL;

public class EditorState extends State
{
    public EditorState(StateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    public void enter() {
        // Find an object to act as base physics/ setup for the simulation
        if(findObjectWithTag(MODEL) != null){
            //ensure mesh object is not null, then reset
            for(Entity obj: stateMachine.allObjects) {
                Objects.requireNonNull(obj.getComponent(Mesh.class)).reset();
            }
        }
        else{
            //Node2D n = new Node2D(0,0);
           //Node2D o = new Node2D(10,10);
            //ArrayList<Node2D> nodes = new ArrayList<>();
           // nodes.add(n); nodes.add(o);
            //Entity e = new Entity("Test").with(new DebuggerComponent()).with(new SubdividedPolygonMesh().build(nodes)).with(new ElasticForce());
            //loadModel();
        }
    }

    @Override
    public void tick() {
    }

    @Override
    void exit() {
        saveInitial();
    }

}
