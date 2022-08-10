package Model.Components.Physics;

import Model.Components.Component;

public abstract class Force extends Component {

    @Override
    public void init() {

    }

    public abstract void apply();

    public abstract void setup();
}
