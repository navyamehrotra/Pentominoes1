import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Engine3DStuff.customdatatypes.Vector3D;

public class MainUIFrame {
    // Toly and nat
    private JFrame frame;
    private ImagePanel mainPanel;

    public MainUIFrame(Image bgImage) {
        //ImageIcon resetIcon  = new ImageIcon(System.getProperty("user.dir") + "/src/assets/reset.png"); 
        //ImageIcon modesIcon = new ImageIcon(System.getProperty("user.dir") + "/src/assets/human.png");

        javax.swing.plaf.FontUIResource font = new javax.swing.plaf.FontUIResource("Arial", Font.PLAIN, 25);
        UIManager.put("Button.font", font);
        UIManager.put("ToggleButton.font", font);
        UIManager.put("Label.font", font);
        UIManager.put("Spinner.font", font);


        frame = new JFrame();
        JPanel buttonPanel = new JPanel();
        JButton reset = new JButton("Reset View");
        
        JButton compute = new JButton("Compute");
        //JButton resetAll = new JButton("Reset");
        JToggleButton modes = new JToggleButton("Board Fill Mode");
        JToggleButton parcel = new JToggleButton("ABC Parcels Mode");

        mainPanel = new ImagePanel(bgImage);
        frame.setContentPane(mainPanel);
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10,10,0,10);
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.PAGE_END;
        c.weightx = 1.0;
        c.gridy = 2;

        
        JPanel bottomPanel = new JPanel();

        bottomPanel.setLayout(new BorderLayout());
        frame.add(bottomPanel, c);

        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        c.fill = GridBagConstraints.NONE;
        frame.add(reset, c);

        JLabel label = new JLabel("...");

        bottomPanel.add(label, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.setOpaque(true);
        bottomPanel.setBackground(new Color(255, 255, 255));

        buttonPanel.setLayout(new GridLayout(1, 4, 10, 10));
        buttonPanel.add(modes);
        //buttonPanel.add(resetAll);
        buttonPanel.add(parcel);
        buttonPanel.add(compute);

        buttonPanel.setOpaque(true);
        buttonPanel.setBackground(new Color(255, 255, 255));

        JSpinner valueA = new JSpinner();
        JSpinner valueB = new JSpinner();
        JSpinner valueC = new JSpinner();
        valueA.setValue(3);
        valueB.setValue(4);
        valueC.setValue(5);

        SpinnerNumberModel modelX = new SpinnerNumberModel(16.5, 0.5, 40.0, 0.5);  
        SpinnerNumberModel modelY = new SpinnerNumberModel(2.5, 0.5, 40.0, 0.5);  
        SpinnerNumberModel modelZ = new SpinnerNumberModel(4.0, 0.5, 40.0, 0.5);  
        JSpinner sizeX = new JSpinner(modelX);
        JSpinner sizeY = new JSpinner(modelY);
        JSpinner sizeZ = new JSpinner(modelZ);

        JPanel topPanel = new JPanel();

        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridx = 0;
        frame.add(topPanel, c);
        topPanel.setLayout(new GridLayout(8, 2, 10, 0));
        topPanel.setOpaque(true);
        topPanel.setBackground(new Color(255, 255, 255));

        JLabel aLabel = new JLabel("value of A:");
        JLabel bLabel = new JLabel("value of B:");
        JLabel cLabel = new JLabel("value of C:");

        topPanel.add(aLabel);
        topPanel.add(valueA);
        topPanel.add(bLabel);
        topPanel.add(valueB);
        topPanel.add(cLabel);
        topPanel.add(valueC);

        topPanel.add(new JLabel());
        topPanel.add(new JLabel());

        topPanel.add(new JLabel("Truck Length:"));
        topPanel.add(sizeX);
        topPanel.add(new JLabel("Truck Width:"));
        topPanel.add(sizeY);
        topPanel.add(new JLabel("Truck Height:"));
        topPanel.add(sizeZ);

        //topPanel.add(valueC);

        modes.addActionListener(e -> {
             if (modes.isSelected()) {
                // possibility mode is on
                modes.setText("Max Score Mode");
            } else {
                // value mode is on
                modes.setText("Board Fill Mode");
            }
        }); 

        parcel.addActionListener(e -> {
            if (parcel.isSelected()) {
               // possibility mode is on
                parcel.setText("PLT Parcels Mode");
                aLabel.setText("Value of P:");
                bLabel.setText("Value of L:");
                cLabel.setText("Value of T:");
           } else {
            parcel.setText("ABC Parcels Mode");
            aLabel.setText("Value of A:");
            bLabel.setText("Value of B:");
            cLabel.setText("Value of C:");

               // value mode is on
           }
       }); 


        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        //frame.setUndecorated(true);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Vector3D cameraPosition = new Vector3D(6, -8, -12);
        Vector3D cameraRotation = new Vector3D(90 * Math.PI / 180, 40 * Math.PI / 180, 0 * Math.PI / 180);
        Scene3DGenerator tempGenerator = new Scene3DGenerator(500, 300, cameraPosition, cameraRotation);
        MainUIFrame mainUIFrame = new MainUIFrame(tempGenerator.render2ImageSample());
    }

    public JFrame getFrame() {
        return frame;
    }

    public Dimension getSize() {
        return frame.getSize();
    }

    public void update3DImage(Image image) {
        mainPanel.update(image);
    }
}
