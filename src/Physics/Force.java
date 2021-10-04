package Physics;

import GUI.IColor;
import GUI.IRender;

import java.awt.*;

public abstract class Force extends Component implements IColor, IRender
{
    protected Color color;

    public abstract void Act();

    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void render() {

    }
}
