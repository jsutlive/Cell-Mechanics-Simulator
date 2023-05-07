package component;

import framework.utilities.Time;

import static framework.states.StateMachine.timer;

public abstract class Experiment extends Component{
    public float startTime;
    private boolean hasActed;

    @Override
    public void update() {
        if(hasActed)return;
        if(timer.elapsedTime <  Time.asNanoseconds(startTime)) {
            act();
            hasActed = true;
        }
    }

    protected abstract void act();
}
