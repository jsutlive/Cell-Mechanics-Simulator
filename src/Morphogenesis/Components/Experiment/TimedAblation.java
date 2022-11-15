package Morphogenesis.Components.Experiment;

import Framework.Object.Component;
import Morphogenesis.Components.Physics.Force;

import java.util.ArrayList;
import java.util.List;

import static Framework.Timer.Time.elapsedTime;
import static Framework.Timer.Time.asNanoseconds;

public class TimedAblation extends Component {
    public float timeUntilAblation = 5f;
    private boolean hasActed = false;
    private List<Component> targets = new ArrayList<>();

    @Override
    public void awake() {
        for(Component c: parent.getComponents()){
            if(Force.class.isAssignableFrom(c.getClass())){
                targets.add(c);
            }
        }
    }

    @Override
    public void update() {
        if(hasActed) return;
        if(elapsedTime >= asNanoseconds(timeUntilAblation)){


        }
    }
}
