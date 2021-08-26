package Engine.Object;

public interface IBehavior
{
    /**
     * Called by each object upon its creation.
     */
    void awake();

    /**
     * This method is called a single time before the physics loop begins
     */
    void start() throws InstantiationException, IllegalAccessException;

    /**
     * This method is called once per frame to advance the system based on the physics loads attached
     */
    void update();

    /**
     * Removes the object and its references from the current state
     */
    void destroy();

    void render();

}
