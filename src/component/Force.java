package component;

import framework.rigidbodies.IRigidbody;
import utilities.geometry.Vector.Vector;

public abstract class Force extends Component {

    public void addForceToBody(IRigidbody rb, Vector vec){
        rb.addForceVector(getClass().getSimpleName(), vec);
    }

    public static Vector limitForce(Vector force, float limit){
        if(force.mag() < limit) return force;
        else {
            Vector limitedForce = force.unit();
            limitedForce.mul(limit);
            return limitedForce;
        }
    }
}
