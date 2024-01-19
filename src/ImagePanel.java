import java.awt.*;
import javax.swing.*;

public class ImagePanel extends JPanel {

  private Image img;

  /*public ImagePanel(String img) {
    this(new ImageIcon(img).getImage());
  }*/

  public ImagePanel(Image img) {
    update(img);
  }

  public void update(Image img) {
    this.img = img;
    Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
    setPreferredSize(size);
    setMinimumSize(size);
    setMaximumSize(size);
    setSize(size);
    repaint();
    //setLayout(null);
  }

  public void paintComponent(Graphics g) {
    g.drawImage(img, 0, 0, null);
  }

}