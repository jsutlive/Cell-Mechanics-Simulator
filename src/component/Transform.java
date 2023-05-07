package component;

import framework.events.EventHandler;
import framework.object.annotations.DoNotDestroyInGUI;
import utilities.geometry.Vector.Vector2f;

@DoNotDestroyInGUI
public class Transform extends Component {
    public Vector2f position = new Vector2f();
    private Mesh mesh;
    public EventHandler<Vector2f> onPositionChanged = new EventHandler<>();

    @Override
    public void onValidate() {
        onPositionChanged.invoke(this.position);
    }

    @Override
    public void update() {
        if(mesh == null){
            mesh = getComponent(Mesh.class);
        }else{
            position = mesh.calculateCentroid();
        }
    }

    @Override
    public void onDestroy() {
        onPositionChanged.close();
    }
}