package Morphogenesis.Experiment;

import Framework.Object.Component;
import Morphogenesis.Physics.Force;

import java.util.ArrayList;
import java.util.List;


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


    }
}
