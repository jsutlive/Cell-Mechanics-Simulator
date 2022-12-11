package Framework.Object;

import Framework.Events.EventHandler;
import Morphogenesis.Meshing.Mesh;
import Morphogenesis.ReloadComponentOnChange;
import Utilities.Geometry.Vector.Vector2f;

@ReloadComponentOnChange
public class Transform extends Component{
    public Vector2f position = new Vector2f();
    Mesh mesh;

    public EventHandler<Vector2f> onPositionChanged = new EventHandler<>();
    @Override
    public void awake() {
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
