package Model.Components.Physics;

import Model.Components.Component;
import Utilities.Physics.ForceVector;

public abstract class Force extends Component {

    public transient ForceVector forceVector = new ForceVector();
}
