package Utilities.Physics;

import Utilities.Geometry.Vector.Vector2f;

public class ForceVector extends Vector2f {
    private ForceType type;
    public void setType(ForceType forceType){ type = forceType;}
    public void set(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void set(Vector2f v){
        this.x = v.x;
        this.y = v.y;
    }

    public ForceVector neg(){
        ForceVector fvneg = new ForceVector();
        fvneg.type = this.type;
        fvneg.x = this.x*-1;
        fvneg.y = this.y*-1;
        return fvneg;
    }

    public void reset(){
        set(Vector2f.zero);
    }
}
