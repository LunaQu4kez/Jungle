package view;

import model.CellType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * This is the equivalent of the Cell class,
 * but this class only cares how to draw Cells on BoardView
 */

public class CellView extends JPanel {
    public Color background;
    int size;
    public boolean canStep;
    public boolean mouseAt;
    public CellType type;

    public CellView(Point location, int size, CellType type) {
        setLayout(new GridLayout(1, 2));
        setLocation(location);
        setSize(size, size);
        this.size = size;
        this.type = type;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        if (type == CellType.SPRING_GRASS) g.setColor(BoardView.springGrass);
        else if (type == CellType.SPRING_RIVER) g.setColor(BoardView.springRiver);
        else if (type == CellType.AUTUMN_GRASS) g.setColor(BoardView.autumnGrass);
        else if (type == CellType.AUTUMN_RIVER) g.setColor(BoardView.autumnRiver);
        else if (type == CellType.TRAP) g.setColor(BoardView.trapColor);
        else if (type == CellType.DEN) g.setColor(BoardView.denColor);
        else g.setColor(background);
        g.fillRect(1, 1, this.getWidth() - 1, this.getHeight() - 1);

        if (canStep) { // Highlights the model if selected.
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(255, 253, 87, 150));
            RoundRectangle2D roundedRectangle = new RoundRectangle2D.Double(1, 1,
                    this.getWidth() - 1, this.getHeight() - 1, size / 4, size / 4);
            g2d.fill(roundedRectangle);
        }

        if (mouseAt){
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(255, 172, 155, 120));
            RoundRectangle2D roundedRectangle = new RoundRectangle2D.Double(1, 1,
                    this.getWidth() - 1, this.getHeight() - 1, size / 4, size / 4);
            g2d.fill(roundedRectangle);
        }
    }

}
