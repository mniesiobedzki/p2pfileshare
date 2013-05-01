import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.LineMetrics;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class RichJLabel extends JLabel {

  private int tracking;

  public RichJLabel(String text, int tracking) {
    super(text);
    this.tracking = tracking;
    setLeftShadow(1, 1, new Color(255,255,255,50));
    setRightShadow(1, 1, new Color(255,255,255,50));
  }

  private int left_x, left_y, right_x, right_y;

  private Color left_color, right_color;

  public void setLeftShadow(int x, int y, Color color) {
    left_x = x;
    left_y = y;
    left_color = color;
  }

  public void setRightShadow(int x, int y, Color color) {
    right_x = x;
    right_y = y;
    right_color = color;
  }

  public Dimension getPreferredSize() {
    String text = getText();
    FontMetrics fm = this.getFontMetrics(getFont());

    int w = fm.stringWidth(text);
    w += (text.length() - 1) * tracking;
    w += left_x + right_x;

    int h = fm.getHeight();
    h += left_y + right_y;

    return new Dimension(w, h);
  }

  public void paintComponent(Graphics g) {
    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    char[] chars = getText().toCharArray();

    FontMetrics fm = this.getFontMetrics(getFont());
    int h = fm.getAscent();
    LineMetrics lm = fm.getLineMetrics(getText(), g);
    g.setFont(getFont());

    int x = 0;

    for (int i = 0; i < chars.length; i++) {
      char ch = chars[i];
      int w = fm.charWidth(ch) + tracking;

      g.setColor(left_color);
      g.drawString("" + chars[i], x - left_x, h - left_y);

      g.setColor(right_color);
      g.drawString("" + chars[i], x + right_x, h + right_y);

      g.setColor(getForeground());
      g.drawString("" + chars[i], x, h);

      x += w;
    }

  }

}