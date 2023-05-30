package view.chessView;

import model.PlayerColor;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class AnimalView extends JComponent {
    public PlayerColor owner;

    public boolean selected;
    int size;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public AnimalView() {}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isSelected()) { // Highlights the model if selected.
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(255, 255, 255, 150));
            RoundRectangle2D roundedRectangle = new RoundRectangle2D.Double(1, 1,
                    this.getWidth() - 1, this.getHeight() - 1, size / 4, size / 4);
            g2d.fill(roundedRectangle);
        }


    }
}
