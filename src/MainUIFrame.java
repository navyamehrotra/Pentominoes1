import java.awt.*;
import javax.swing.*;

public class MainUIFrame {
    // Toly and nat

    private Engine3D engine3d;

    public MainUIFrame(Engine3D engine3d) {
        this.engine3d = engine3d;
        JFrame frame = new JFrame();
        // JTextField textfield = new JTextField(); not sure if we are using this
        
        JToggleButton valPossToggler = new JToggleButton("possMode");
        //valPossToggler.setSelectedIcon(valMode); --- graphics to be added once we have them

        valPossToggler.addActionListener(e -> {
            if (!valPossToggler.isSelected()) {
                // possibility mode is on
            } else {
                valPossToggler.setText("valMode");
                // value mode is on
            }
        });

        frame.add(valPossToggler);
    }

    public void update() {
    }
}
