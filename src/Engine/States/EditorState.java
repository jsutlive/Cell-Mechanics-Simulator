package Engine.States;

import Engine.Object.Entity;
import Engine.Object.Tag;
import GUI.IRender;
import Model.*;
import java.util.ConcurrentModificationException;

public class EditorState extends State
{
    Entity model;
    @Override
    public void Init() {
        State.reset();
        model = State.create(DrosophilaRingModel.class);
        assert model != null;
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
    }

    @Override
    void OnChangeState() {

    }
}
