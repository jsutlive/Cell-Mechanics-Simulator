package Model.Components.Physics;

import Model.Components.Component;
import Physics.Rigidbodies.IRigidbody;
import Utilities.Geometry.Vector.Vector;
import Utilities.Physics.ForceVector2D;

public abstract class Force extends Component {

    public transient ForceVector2D forceVector = new ForceVector2D();
    public void addForceToBody(IRigidbody rb, Vector vec){
        rb.addForceVector(getClass().getSimpleName(), vec);
    }
}
