package Framework.States;

import Framework.Object.Entity;
import Morphogenesis.Models.BasicCellsModel;
import Renderer.Graphics.IRender;
import Morphogenesis.Models.DrosophilaRingModel;

import java.util.ConcurrentModificationException;
import static Framework.Object.Tag.MODEL;

public class EditorState extends State
{
    Entity model;
    @Override
    public void Init() {
        State.reset();
        if(State.findObjectWithTag(MODEL) == null) {
            model = State.create(DrosophilaRingModel.class);
        }
        assert model != null;
        model.addTag(MODEL);
        for(Entity obj: allObjects){
            obj.start();
        }
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
            return;
        }
    }

    @Override
    void OnChangeState() {

    }
}
