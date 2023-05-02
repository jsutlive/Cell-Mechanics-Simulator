package Framework.Rigidbodies;

import Utilities.Geometry.Vector.Vector;

/**
 * Interface IRigidbody is responsible for physics application to individual components within physics objects.
 *
 * Copyright (c) 2023 Joseph Sutlive
 * All rights reserved
 */
public interface IRigidbody {
    /**
     * Add a force vector of arbitrary dimension to tbe resultant force of this object
     * @param forceVector vector to be applied
     */
    void addForceVector(Vector forceVector);

    /**
     * Add force vector (with name/type specified) to the resultant force of this object
     * @param name type of force being applied
     * @param vec vector to be applied
     */
    void addForceVector(String name, Vector vec);

    /**
     * Move this object to a new position, explicitly declared.
     * @param newPosition explicit new x,y coordinate to move the object to.
     */
    void moveTo(Vector newPosition);

    /**
     * Move this object to a new position to be defined within this function
     */
    void move();



}
