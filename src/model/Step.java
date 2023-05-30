package model;

public class Step {
    public BoardPoint src;
    public BoardPoint dest;
    public PlayerColor color;
    public Chess captured;


    public Step(BoardPoint src, BoardPoint dest, PlayerColor color) {
        this.src = src;
        this.dest = dest;
        this.color = color;
        captured = null;
    }

    public Step(BoardPoint src, BoardPoint dest, PlayerColor color, Chess captured) {
        this.src = src;
        this.dest = dest;
        this.color = color;
        this.captured = captured;
    }

    @Override
    public String toString() {
        if (captured == null)
            return (color == PlayerColor.BLUE ? "b " : "r ") +
                    "(" + src.getRow() + "," + src.getCol() + ") " +
                    "(" + dest.getRow() +"," + dest.getCol() + ") " +
                    "null";
        else
            return (color == PlayerColor.BLUE ? "b " : "r ") +
                    "(" + src.getRow() + "," + src.getCol() + ") " +
                    "(" + dest.getRow() +"," + dest.getCol() + ") " +
                    captured.getName();
    }
}
