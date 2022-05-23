package Engine.States;

import GUI.IRender;
import GUI.Painter;
import Model.Model;
import Utilities.Geometry.Vector2i;

import java.awt.*;

public class EditorState extends State
{
    Model model;
    @Override
    public void Init() throws InstantiationException, IllegalAccessException {
        State.reset();
        model = (Model) State.create(Model.class);
        model.printCells();
    }

    @Override
    public void Tick() {

    }

    @Override
    public void Render() {
        for(IRender rend: renderBatch)
        {
            rend.render();
        }
        Painter.drawPoint(new Vector2i(400,400));
        Painter.drawCircle(new Vector2i(400), 604, Color.gray);
        Painter.drawCircle(new Vector2i(400), 605, Color.gray);
        Painter.drawCircle(new Vector2i(400), 606, Color.gray);
    }


}
