package Morphogenesis.Render;

import Morphogenesis.ReloadEntityOnChange;
import Renderer.Graphics.IColor;
import Renderer.Graphics.IRender;
import Framework.Object.Component;

import java.awt.*;

import static Renderer.Renderer.DEFAULT_COLOR;


public abstract class ObjectRenderer extends Component implements IRender, IColor {

    protected Color color = DEFAULT_COLOR;
    @ReloadEntityOnChange
    public Color defaultColor = DEFAULT_COLOR;

}