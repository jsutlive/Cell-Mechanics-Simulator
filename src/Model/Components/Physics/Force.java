package Model.Components.Physics;

import Model.Components.Component;
import Utilities.Physics.ForceType;
import Utilities.Physics.ForceVector2D;
import Utilities.Physics.ForceVectorData;

public abstract class Force extends Component {

    public transient ForceVector2D forceVector = new ForceVector2D();
    public transient ForceVectorData forceData;
}
