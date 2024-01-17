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

    private int firstX;
    private int firstY;
    private int lastX;
    private int lastY;
    private static int XCoord;
    private static int YCoord;
    
    private boolean mouseDrag = false;


    public MainUIFrame(Image bgImage) {
        //ImageIcon resetIcon  = new ImageIcon(System.getProperty("user.dir") + "/src/assets/reset.png"); 
        //ImageIcon modesIcon = new ImageIcon(System.getProperty("user.dir") + "/src/assets/human.png");

        frame = new JFrame();
        JPanel buttonPanel = new JPanel();
        JButton reset = new JButton("Reset Angle");
        JButton compute = new JButton("Compute");
        JButton resetAll = new JButton("Reset");
        JToggleButton modes = new JToggleButton("Modes");
        JToggleButton parcel = new JToggleButton("Poss Mode");

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

        JLabel label = new JLabel("test");

        bottomPanel.add(label, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.setOpaque(true);
        bottomPanel.setBackground(new Color(255, 255, 255));

        buttonPanel.setLayout(new GridLayout(1, 4));
        buttonPanel.add(compute);
        buttonPanel.add(modes);
        buttonPanel.add(resetAll);
        buttonPanel.add(parcel);
        buttonPanel.setOpaque(true);
        buttonPanel.setBackground(new Color(255, 255, 255));

        JSpinner valueA = new JSpinner();
        JSpinner valueB = new JSpinner();
        JSpinner valueC = new JSpinner();

        JPanel topPanel = new JPanel();

        topPanel.setLayout(new GridLayout(3, 2));
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridx = 0;
        frame.add(topPanel, c);
        topPanel.setOpaque(true);
        topPanel.setBackground(new Color(255, 255, 255));
        JLabel labelA = new JLabel("A");
        JLabel labelB = new JLabel("B");
        JLabel labelC = new JLabel("C");

        topPanel.add(labelA);
        topPanel.add(valueA);
        topPanel.add(labelB);
        topPanel.add(valueB);
        topPanel.add(labelC);
        topPanel.add(valueC);

        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseDrag = true;
                firstX = e.getX();
                firstY = e.getY();

                System.out.println("clicked");
                
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // 
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseDrag = false;
            
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (mouseDrag) {
                lastX = e.getX();
                lastY = e.getY();

                int deltaX = lastX - firstX;
                int deltaY = lastY - firstY;
                XCoord = firstX + deltaX;
                YCoord = firstY + deltaY;
                
                }
            }
        });


        //parcel.addActionListener(e -> {
        /*     if (!parcel.isSelected()) {
                // possibility mode is on
            } else {
                parcel.setText("Val Mode");
                // value mode is on
            }
        }); */
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Vector3D cameraPosition = new Vector3D(6, -8, -12);
        Vector3D cameraRotation = new Vector3D(90 * Math.PI / 180, 40 * Math.PI / 180, 0 * Math.PI / 180);
        Scene3DGenerator tempGenerator = new Scene3DGenerator(500, 300, cameraPosition, cameraRotation);
        MainUIFrame mainUIFrame = new MainUIFrame(tempGenerator.render2ImageSample());
    }

    public Dimension getSize() {
        return frame.getSize();
    }

    public void update3DImage(Image image) {
        mainPanel.update(image);
    }

    public static int getXCoord() {
        return XCoord;
    }

    public static int getYCoord() {
        return YCoord;
    }

}
