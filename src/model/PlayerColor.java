package model;

import java.awt.*;

/**
 * This class is mainly used to wrap the Color object that describes
 * the ownership of the piece and the current player.
 */
public enum PlayerColor {
    BLUE(Color.BLUE), RED(Color.RED);

    private final Color color;

    PlayerColor(Color color) {
        this.color = color;
    }


    public Color getColor() {
        return color;
    }


}
