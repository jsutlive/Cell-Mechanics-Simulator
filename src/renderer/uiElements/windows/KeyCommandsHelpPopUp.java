package renderer.uiElements.windows;
import javax.swing.*;
import java.awt.*;
public class KeyCommandsHelpPopUp {
    public KeyCommandsHelpPopUp() {
        JFrame jFrame = new JFrame();

        JDialog jd = new JDialog(jFrame);

        jd.setLayout(new GridLayout(0,1));

        jd.setBounds(500, 300, 400, 300);

        JLabel jLabel = new JLabel("Key Commands");

        JButton jButton = new JButton("Close");
        jButton.addActionListener(e -> jd.setVisible(false));

        jd.add(jLabel);
        jd.add(new JLabel("Left Click: Select object"));
        jd.add(new JLabel("Shift + Left Click/ Left Click + Drag: Select multiple objects"));
        jd.add(new JLabel("Alt + Click/ Right Click: Deselect objects"));
        jd.add(new JLabel("Space: Start/stop simulation"));
        jd.add(new JLabel("Delete: Delete selected objects"));

        jd.add(jButton);
        jd.setVisible(true);
    }
}
