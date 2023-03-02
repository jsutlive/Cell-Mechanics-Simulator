package Framework.Data;

import Framework.Object.Component;
import Morphogenesis.ReloadComponentOnChange;

import static Framework.Data.FileBuilder.setFullPathName;
import static Framework.Data.FileBuilder.setSaveFrequency;

@ReloadComponentOnChange
public class SaveSystem extends Component {
    public String saveFolder = "";
    public float secondsPerSaveInterval = 20;
    @Override
    public void awake() {
        setFullPathName(saveFolder);
        setSaveFrequency(secondsPerSaveInterval);
    }
}
