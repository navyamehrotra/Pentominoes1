import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Engine3DStuff.customdatatypes.Vector3D;

public class MainUIFrame {
    // Toly and nat
    private JFrame frame;
    private ImagePanel mainPanel;
    private JButton reset;
    private JToggleButton hideTruck;

    private Knapsacker1 currentKnapsacker;
    private DancingLinks currentLinks;

    public MainUIFrame(Image bgImage, Scene3DGenerator generator) {
        //ImageIcon resetIcon  = new ImageIcon(System.getProperty("user.dir") + "/src/assets/reset.png"); 
        //ImageIcon modesIcon = new ImageIcon(System.getProperty("user.dir") + "/src/assets/human.png");

        javax.swing.plaf.FontUIResource font = new javax.swing.plaf.FontUIResource("Arial", Font.PLAIN, 25);
        UIManager.put("Button.font", font);
        UIManager.put("ToggleButton.font", font);
        UIManager.put("Label.font", font);
        UIManager.put("Spinner.font", font);
        UIManager.put("RadioButton.font", font);

        frame = new JFrame();
        JPanel buttonPanel = new JPanel();
        reset = new JButton("Reset View");
        hideTruck = new JToggleButton("Hide truck");
        
        JButton compute = new JButton("Fill The Truck");
        //JButton resetAll = new JButton("Reset");
        //JToggleButton modes = new JToggleButton("Board Fill Mode");
        //JToggleButton parcel = new JToggleButton("ABC Parcels Mode");

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

        JPanel topRightPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        frame.add(topRightPanel, c);
        topRightPanel.add(reset);
        topRightPanel.add(hideTruck);

        JLabel label = new JLabel("Press the \"Fill The Truck\" button to start");

        bottomPanel.add(label, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.setOpaque(true);
        bottomPanel.setBackground(new Color(255, 255, 255));

        JRadioButton fillMode = new JRadioButton("Complete Fill Mode");
        JRadioButton scoreMode = new JRadioButton("Max Score Mode");
        ButtonGroup targetModeGroup = new ButtonGroup();
        targetModeGroup.add(fillMode);
        targetModeGroup.add(scoreMode);
        fillMode.setSelected(true);

        JRadioButton abcMode = new JRadioButton("ABC Parcels Mode");
        JRadioButton pltMode = new JRadioButton("PLT Parcels Mode");
        ButtonGroup parcelModeGroup = new ButtonGroup();
        parcelModeGroup.add(abcMode);
        parcelModeGroup.add(pltMode);
        abcMode.setSelected(true);

        buttonPanel.setLayout(new GridLayout(2, 4, 10, 0));
        buttonPanel.add(fillMode);
        buttonPanel.add(abcMode);
        buttonPanel.add(compute);

        //buttonPanel.add(resetAll);
        buttonPanel.add(scoreMode);
        buttonPanel.add(pltMode);

        buttonPanel.setOpaque(true);
        buttonPanel.setBackground(new Color(255, 255, 255));

        JSpinner valueA = new JSpinner();
        JSpinner valueB = new JSpinner();
        JSpinner valueC = new JSpinner();
        valueA.setValue(3);
        valueB.setValue(4);
        valueC.setValue(5);

        SpinnerNumberModel modelX = new SpinnerNumberModel(16.5, 0.5, 40.0, 0.5);  
        SpinnerNumberModel modelY = new SpinnerNumberModel(4.0, 0.5, 40.0, 0.5);  
        SpinnerNumberModel modelZ = new SpinnerNumberModel(2.5, 0.5, 40.0, 0.5);  
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

        JLabel aLabel = new JLabel("Value of A:");
        JLabel bLabel = new JLabel("Value of B:");
        JLabel cLabel = new JLabel("Value of C:");

        topPanel.add(aLabel);
        topPanel.add(valueA);
        topPanel.add(bLabel);
        topPanel.add(valueB);
        topPanel.add(cLabel);
        topPanel.add(valueC);

        topPanel.add(new JLabel());
        topPanel.add(new JLabel());

        topPanel.add(new JLabel("Cargo Length:"));
        topPanel.add(sizeX);
        topPanel.add(new JLabel("Cargo Height:"));
        topPanel.add(sizeY);
        topPanel.add(new JLabel("Cargo Width:"));
        topPanel.add(sizeZ);

        //topPanel.add(valueC);

        abcMode.addActionListener(e -> {
            aLabel.setText("Value of A:");
            bLabel.setText("Value of B:");
            cLabel.setText("Value of C:");
        });  

        pltMode.addActionListener(e -> {
            aLabel.setText("Value of P:");
            bLabel.setText("Value of L:");
            cLabel.setText("Value of T:");
        });

        hideTruck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (hideTruck.isSelected()) {
                    hideTruck.setText("Show truck");
                }
                else {
                    hideTruck.setText("Hide truck");
                }
            }
        });

        compute.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentKnapsacker != null) {
                    currentKnapsacker.dead = true;
                }
                if (currentLinks != null) {
                    currentLinks.dead = true;
                }

                if (fillMode.isSelected()) {
                    currentLinks = new DancingLinks(generator, label, 
                        abcMode.isSelected() ? Utility.ShapeSet.ABC : Utility.ShapeSet.PLT, 
                        getSize(sizeX), getSize(sizeY), getSize(sizeZ));
                }
                else {
                    currentKnapsacker = new Knapsacker1(generator, label, 
                        abcMode.isSelected() ? Utility.ShapeSet.ABC : Utility.ShapeSet.PLT, 
                        new int[] { getScore(valueA), getScore(valueB), getScore(valueC)}, 
                        getSize(sizeX), getSize(sizeY), getSize(sizeZ));
                }
            }
        });


        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        frame.setVisible(true);
    }

    private int getSize(JSpinner spinner) {
        double val = (Double)spinner.getValue();
        int ret = (int)Math.round(val * 2);
        return ret;
    }

    private int getScore(JSpinner spinner) {
        int ret = (Integer)spinner.getValue();
        return ret;
    }

    public void addResetViewListener(ActionListener listener) {
        reset.addActionListener(listener);
    }

    public void addHideTruckListener(ActionListener listener) {
        hideTruck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (hideTruck.isSelected()) {
                    listener.actionPerformed(e);
                }
            }
        });
    }

    public void addShowTruckListener(ActionListener listener) {
        hideTruck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!hideTruck.isSelected()) {
                    listener.actionPerformed(e);
                }
            }
        });
    }

    /*public static void main(String[] args) {
        Vector3D cameraPosition = new Vector3D(6, -8, -12);
        Vector3D cameraRotation = new Vector3D(90 * Math.PI / 180, 40 * Math.PI / 180, 0 * Math.PI / 180);
        Scene3DGenerator tempGenerator = new Scene3DGenerator(500, 300, cameraPosition, cameraRotation);
        MainUIFrame mainUIFrame = new MainUIFrame(tempGenerator.render2ImageSample());
    }*/

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
