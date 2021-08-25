package Engine.Object;

public interface IBehavior
{
    /**
     * This method is called a single time before the physics system initializes
     */
    void start();

    /**
     * This method is called once per frame to advance the system based on the physics loads attached
     */
    void update();

    /**
     * Removes the object and its references from the current state
     */
    void destroy();

    //<T extends MonoBehavior> T createObject();
}
