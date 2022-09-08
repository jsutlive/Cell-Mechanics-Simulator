package Engine.Object;

/**
 * Interface for any object which will be responding to physics loop states such as update, late update, start, and awake
 * Used primarily by MonoBehavior and Component objects
 */
public interface IBehavior
{
    /**
     * Called by the state for each object upon its creation.
     */
    void awake() throws InstantiationException, IllegalAccessException;

    /**
     * This method is called a single time before the physics loop begins
     */
    void start() throws InstantiationException, IllegalAccessException;

    /**
     * This method is called once per frame to update the physics
     */
    void update();

    /**
     * This method is called once per frame after the update method
     */
    void lateUpdate();

    /**
     * This method is called once per frame before the update method
     */
    void earlyUpdate();

    /**
     * This method is called just prior to this object's destruction
     */
    void onDestroy();
}
