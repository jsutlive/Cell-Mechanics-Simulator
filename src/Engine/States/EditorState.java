package Engine.States;

import Engine.Object.Tag;
import GUI.IRender;
import GUI.Painter;
import Model.*;
import Utilities.Geometry.Vector2i;

import java.awt.*;
import java.util.ConcurrentModificationException;

public class EditorState extends State
{
    Model model;
    @Override
    public void Init() throws InstantiationException, IllegalAccessException {
        State.reset();
        model = (Model) State.create(Model.class);
        model.addTag(Tag.MODEL);
    }

    @Override
    public void Tick() {
    }

    @Override
    public void Render() {
        try {
            for(IRender rend: renderBatch)
            {
                rend.render();
            }
        } catch (ConcurrentModificationException e){
            e.printStackTrace();
        }
        Painter.drawPoint(new Vector2i(400,400));
        Painter.drawCircle(new Vector2i(400), 604, Color.gray);
        Painter.drawCircle(new Vector2i(400), 605, Color.gray);
        Painter.drawCircle(new Vector2i(400), 606, Color.gray);
    }


}
