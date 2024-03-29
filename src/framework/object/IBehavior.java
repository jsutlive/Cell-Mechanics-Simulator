package framework.object;

/**
 * Interface for any object which will be responding to physics loop states such as update, late update, start, and awake
 * Used primarily by Entity and Component objects
 */
public interface IBehavior
{
    /**
     * Called by the state for each object upon its creation, in any program state
     */
    void awake();

    /**
     * Checks/ methods to be run just after awake and refreshes objects/ components after modification
     */
    void onValidate();

    /**
     * This method is called a single time before the physics loop begins, when the "Run State" is entered
     */
    void start();

    /**
     * This method is called once per "tick" to update the physics, only during the "Run State"
     */
    void update();

    /**
     * This method is called once per "tick" after the update method, only during the "Run State"
     */
    void lateUpdate();

    /**
     * This method is called once per "tick" before the update method, only during the "Run State"
     */
    void earlyUpdate();

    /**
     * This method is called just prior to this object's destruction, in any program state
     */
    void onDestroy();

}
