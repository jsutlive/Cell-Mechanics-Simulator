package component;

public class LaserAblation extends Experiment{

    @Override
    protected void act(){
        for(Component component: parent.getComponents()){
            if(Force.class.isAssignableFrom(component.getClass())){
                if(!(component instanceof OsmosisForce)){
                    component.setEnabled(false);
                }
            }
        }
    }
}
