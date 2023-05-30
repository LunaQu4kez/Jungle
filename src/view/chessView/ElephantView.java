package view.chessView;


import model.PlayerColor;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

/**
 * This is the equivalent of the Chess class,
 * but this class only cares how to draw Chess on BoardView
 */
public class ElephantView extends AnimalView {

    public ElephantView(PlayerColor owner, int size) {
        this.owner = owner;
        this.selected = false;
        this.size = size;
        setSize(size/2, size/2);
        setLocation(0,0);
        setVisible(true);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        ImageIcon pic = new ImageIcon("resource\\chess\\re.png");
        if (owner == PlayerColor.BLUE){
            pic = new ImageIcon("resource\\chess\\be.png");
        }
        Image image = pic.getImage();
        pic = new ImageIcon(image.getScaledInstance(size,size,Image.SCALE_SMOOTH));
        JLabel label = new JLabel(pic);
        label.setSize(size, size);
        label.setLocation(0, 0);
        add(label);
    }
}
