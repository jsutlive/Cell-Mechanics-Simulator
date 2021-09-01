package Physics.Forces.Springs;

import java.util.HashSet;

public class ElasticSpring extends Spring
{
    public static ElasticSpring configureNew(float constant, float ratio)
    {
        ElasticSpring spring = new ElasticSpring();
        spring.constant = constant;
        spring.ratio = ratio;
        spring.listeners = new HashSet<>();
        return spring;
    }

    private ElasticSpring(){}

    @Override
    public void start() {

    }
}
