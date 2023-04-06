package Framework.Object;

import Component.Component;
import Framework.Events.EventHandler;
import Framework.Object.Annotations.DoNotDestroyInGUI;
import Component.Mesh;
import Annotations.ReloadComponentOnChange;
import Utilities.Geometry.Vector.Vector2f;

@ReloadComponentOnChange
@DoNotDestroyInGUI
public class Transform extends Component {
    public Vector2f position = new Vector2f();
    Mesh mesh;
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
