package Data;

import Model.Components.Physics.Collision.CellRingCollider;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class LogDataExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        return false;
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        if(aClass.isAnnotationPresent(LogOnce.class)) return true;
        else if(CellRingCollider.class.isAssignableFrom(aClass)) return true;
        else return false;
    }
}
