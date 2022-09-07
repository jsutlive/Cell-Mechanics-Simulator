package Model.Components.Physics;

import Model.Components.Component;
import Model.Components.Physics.ForceVector.ForceVector;

public abstract class Force extends Component {

    protected transient ForceVector forceVector = new ForceVector();
}
