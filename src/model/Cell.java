package model;

import java.io.Serializable;
/**
 * This class describe the slot for Chess in Board
 * */
public class Cell implements Serializable {
    // the position for chess
    public Chess chess;


    public Chess getPiece() {
        return chess;
    }

    public void setPiece(Chess chess) {
        this.chess = chess;
    }

    public void removePiece() {
        this.chess = null;
    }
}
