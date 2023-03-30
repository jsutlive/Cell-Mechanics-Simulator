package Component;

import static Framework.States.StateMachine.timer;
import static Framework.Timer.Time.asNanoseconds;


public class LateralShorteningSpringForce extends SpringForce {

    public float onsetTime;

    @Override
    public void awake() {

        RingCellMesh mesh = parent.getComponent(RingCellMesh.class);
        int size = mesh.nodes.size();
        for(int i = 0; i < size; i++){
            if(i != mesh.lateralResolution || i != size-1){
                edges.add(mesh.edges.get(i));
            }
        }
        targetLengthRatio = 0.7f;
        constant = 3f;
    }

    @Override
    public void update() {
        if(timer.elapsedTime < asNanoseconds(onsetTime)) return;
        super.update();
    }
}
