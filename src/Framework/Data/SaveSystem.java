package Framework.Data;

import Framework.Object.Component;
import Morphogenesis.ReloadComponentOnChange;

import static Framework.Data.FileBuilder.setFullPathName;

@ReloadComponentOnChange
public class SaveSystem extends Component {
    public String saveFolder = "";

    @Override
    public void awake() {
        setFullPathName(saveFolder);
    }
}
