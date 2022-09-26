package Utilities.Physics;


import Utilities.Geometry.Vector.Vector2f;

public class ForceVector2D extends Vector2f {
    private ForceType type;
    public void setType(ForceType forceType){ type = forceType;}
    public void set(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void set(Vector2f vec) {
        this.x = vec.get(0);
        this.y = vec.get(1);
    }

    public ForceVector2D neg(){
        ForceVector2D fvneg = new ForceVector2D();
        fvneg.type = this.type;
        fvneg.x = this.x*-1;
        fvneg.y = this.y*-1;
        return fvneg;
    }

    public void reset(){
        set(Vector2f.zero);
    }
}
