package Morphogenesis.Components.Render;

import Morphogenesis.Components.ReloadEntityOnChange;
import Renderer.Graphics.IColor;
import Renderer.Graphics.IRender;
import Framework.Object.Component;
import Renderer.Graphics.Painter;

import java.awt.*;

public abstract class ObjectRenderer extends Component implements IRender, IColor {

    protected Color color = Painter.DEFAULT_COLOR;
    @ReloadEntityOnChange
    public Color defaultColor = Painter.DEFAULT_COLOR;

}