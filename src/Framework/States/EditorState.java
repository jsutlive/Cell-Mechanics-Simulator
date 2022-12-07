package Framework.States;


import Framework.Object.Entity;
import Morphogenesis.Components.Meshing.Mesh;


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
                if(obj.getComponent(Mesh.class) == null) continue;
                obj.getComponent(Mesh.class).reset();
            }
        }
        else{
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
