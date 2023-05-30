package model;

import java.util.ArrayList;

/**
 * This class store the real chess information.
 * The Board has 9*7 cells, and each cell has a position for chess
 */
public class Board {
    public Cell[][] grid;

    public ArrayList<Step> steps;
    public ArrayList<Chess> blueDead;
    public ArrayList<Chess> redDead;

    public Board() {
        this.grid =
                new Cell[Constant.CHESSBOARD_ROW_SIZE.getNum()][Constant.CHESSBOARD_COL_SIZE.getNum()];//9X7
        steps = new ArrayList<>();
        blueDead = new ArrayList<>();
        redDead = new ArrayList<>();

        initGrid();
        initPieces();
    }

    public void initGrid() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    public void initPieces() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                grid[i][j].removePiece();
            }
        }

        grid[6][0].setPiece(new Chess(PlayerColor.BLUE, "Elephant",8));
        grid[2][6].setPiece(new Chess(PlayerColor.RED, "Elephant",8));
        grid[8][6].setPiece(new Chess(PlayerColor.BLUE, "Lion", 7));
        grid[0][0].setPiece(new Chess(PlayerColor.RED, "Lion", 7));
        grid[8][0].setPiece(new Chess(PlayerColor.BLUE, "Tiger", 6));
        grid[0][6].setPiece(new Chess(PlayerColor.RED, "Tiger", 6));
        grid[6][4].setPiece(new Chess(PlayerColor.BLUE, "Leopard", 5));
        grid[2][2].setPiece(new Chess(PlayerColor.RED, "Leopard", 5));
        grid[6][2].setPiece(new Chess(PlayerColor.BLUE, "Wolf", 4));
        grid[2][4].setPiece(new Chess(PlayerColor.RED, "Wolf", 4));
        grid[7][5].setPiece(new Chess(PlayerColor.BLUE, "Dog", 3));
        grid[1][1].setPiece(new Chess(PlayerColor.RED, "Dog", 3));
        grid[7][1].setPiece(new Chess(PlayerColor.BLUE, "Cat", 2));
        grid[1][5].setPiece(new Chess(PlayerColor.RED, "Cat", 2));
        grid[6][6].setPiece(new Chess(PlayerColor.BLUE, "Rat", 1));
        grid[2][0].setPiece(new Chess(PlayerColor.RED, "Rat", 1));
    }

    public Chess getChessPieceAt(BoardPoint point) {
        return getGridAt(point).getPiece();
    }

    public Cell getGridAt(BoardPoint point) {
        return grid[point.getRow()][point.getCol()];
    }

    private int calculateDistance(BoardPoint src, BoardPoint dest) {
        return Math.abs(src.getRow() - dest.getRow()) + Math.abs(src.getCol() - dest.getCol());
    }

    private Chess removeChess(BoardPoint point) {
        Chess chess = getChessPieceAt(point);
        getGridAt(point).removePiece();
        return chess;
    }

    private void setChess(BoardPoint point, Chess chess) {
        getGridAt(point).setPiece(chess);
        if (isOpponentTrap(point, chess.getOwner())){
            chess.rank = 0;
        } else {
            if (chess.getName().equals("Elephant")){
                chess.rank = 8;
            }
            if (chess.getName().equals("Lion")){
                chess.rank = 7;
            }
            if (chess.getName().equals("Tiger")){
                chess.rank = 6;
            }
            if (chess.getName().equals("Leopard")){
                chess.rank = 5;
            }
            if (chess.getName().equals("Wolf")){
                chess.rank = 4;
            }
            if (chess.getName().equals("Dog")){
                chess.rank = 3;
            }
            if (chess.getName().equals("Cat")){
                chess.rank = 2;
            }
            if (chess.getName().equals("Rat")){
                chess.rank = 1;
            }
        }
    }

    public void move(BoardPoint src, BoardPoint dest) {
        if (!canMove(src, dest)) {
            throw new IllegalArgumentException("Illegal chess move!");
        }
        Chess chess = removeChess(src);
        setChess(dest, chess);
        steps.add(new Step(src, dest, chess.getOwner()));
    }

    public void capture(BoardPoint src, BoardPoint dest) {
        if (!canCapture(src, dest)) {
            throw new IllegalArgumentException("Illegal chess capture!");
        }
        Chess eater = removeChess(src);
        Chess dead = removeChess(dest);
        setChess(dest, eater);
        if (dead.getOwner() == PlayerColor.BLUE){
            blueDead.add(dead);
            System.out.println("blue eaten " + blueDead.size());
        } else {
            redDead.add(dead);
            System.out.println("red eaten " + redDead.size());
        }
        steps.add(new Step(src, dest, eater.getOwner(), dead));
    }

    public Cell[][] getGrid() {
        return grid;
    }
    public PlayerColor getChessOwner(BoardPoint point) {
        return getGridAt(point).getPiece().getOwner();
    }

    public boolean canMove(BoardPoint src, BoardPoint dest) {
        Chess chess = getChessPieceAt(src);
        //System.out.println("chess == null " + (chess == null));
        //System.out.println("getChessPieceAt(dest) != null " + (getChessPieceAt(dest) != null));
        if (chess == null || getChessPieceAt(dest) != null) {
            return false;
        }
        if (isOwnDens(dest, chess.getOwner())) return false;
        boolean b = calculateDistance(src, dest) == 1;
        if (chess.getName().equals("Elephant")){
            //System.out.println("canMove " + (b && !isRiver(dest)));
            return  b && !isRiver(dest);
        }
        if (chess.getName().equals("Lion")){
            return (b && !isRiver(dest)) || canJumpRiver(src, dest);
        }
        if (chess.getName().equals("Tiger")){
            return (b && !isRiver(dest)) || canJumpRiver(src, dest);
        }
        if (chess.getName().equals("Leopard")){
            return b && !isRiver(dest);
        }
        if (chess.getName().equals("Wolf")){
            return b && !isRiver(dest);
        }
        if (chess.getName().equals("Dog")){
            return b && !isRiver(dest);
        }
        if (chess.getName().equals("Cat")){
            return b && !isRiver(dest);
        }
        if (chess.getName().equals("Rat")){
            return b;
        }
        return false;
    }

    public boolean canCapture(BoardPoint src, BoardPoint dest) {
        Chess eater = getChessPieceAt(src);
        Chess dead = getChessPieceAt(dest);
        if (eater == null || dead == null){
            return false;
        }
        if (eater.getOwner() == dead.getOwner()){
            return false;
        }
        boolean b = calculateDistance(src, dest) == 1;
        if (eater.getName().equals("Elephant")){
            return  b && !isRiver(dest) && dead.rank != 1;
        }
        if (eater.getName().equals("Lion")){
            return ((b && !isRiver(dest)) || canJumpRiver(src, dest)) && dead.rank <= 7;
        }
        if (eater.getName().equals("Tiger")){
            return ((b && !isRiver(dest)) || canJumpRiver(src, dest)) && dead.rank <= 6;
        }
        if (eater.getName().equals("Leopard")){
            return b && !isRiver(dest) && dead.rank <= 5;
        }
        if (eater.getName().equals("Wolf")){
            return b && !isRiver(dest) && dead.rank <= 4;
        }
        if (eater.getName().equals("Dog")){
            return b && !isRiver(dest) && dead.rank <= 3;
        }
        if (eater.getName().equals("Cat")){
            return b && !isRiver(dest) && dead.rank <= 2;
        }
        if (eater.getName().equals("Rat")){
            return b && (dead.rank <= 1 || dead.rank == 8) && !(isRiver(src) && !isRiver(dest));
        }
        return false;
    }

    private boolean isRiver(BoardPoint point){
        return point.getRow() >= 3 && point.getRow() <= 5 &&
                (point.getCol() == 1 || point.getCol() == 2 || point.getCol() == 4 || point.getCol() == 5);
    }

    private boolean isOwnDens(BoardPoint point, PlayerColor color){
        if (color == PlayerColor.BLUE){
            return point.getRow() == 8 && point.getCol() == 3;
        } else {
            return point.getRow() == 0 && point.getCol() == 3;
        }
    }

    private boolean isOpponentDens(BoardPoint point, PlayerColor color){
        if (color == PlayerColor.RED){
            return point.getRow() == 8 && point.getCol() == 3;
        } else {
            return point.getRow() == 0 && point.getCol() == 3;
        }
    }

    private boolean isOpponentTrap(BoardPoint point, PlayerColor color){
        if (color == PlayerColor.BLUE){
            return (point.getRow() == 1 && point.getCol() == 3)
                    || (point.getRow() == 0 && point.getCol() == 2)
                    || (point.getRow() == 0 && point.getCol() == 4);
        } else {
            return (point.getRow() == 7 && point.getCol() == 3)
                    || (point.getRow() == 8 && point.getCol() == 2)
                    || (point.getRow() == 8 && point.getCol() == 4);
        }
    }

    private boolean canJumpRiver(BoardPoint src, BoardPoint dest){
        if ((src.getRow() == 3 && src.getCol() == 0 && dest.getRow() == 3 && dest.getCol() == 3)
                || (src.getRow() == 3 && src.getCol() == 3 && dest.getRow() == 3 && dest.getCol() == 0)){
            return getChessPieceAt(new BoardPoint(3, 1)) == null
                    && getChessPieceAt(new BoardPoint(3, 2)) == null;
        }
        if ((src.getRow() == 4 && src.getCol() == 0 && dest.getRow() == 4 && dest.getCol() == 3)
                || (src.getRow() == 4 && src.getCol() == 3 && dest.getRow() == 4 && dest.getCol() == 0)){
            return getChessPieceAt(new BoardPoint(4, 1)) == null
                    && getChessPieceAt(new BoardPoint(4, 2)) == null;
        }
        if ((src.getRow() == 5 && src.getCol() == 0 && dest.getRow() == 5 && dest.getCol() == 3)
                || (src.getRow() == 5 && src.getCol() == 3 && dest.getRow() == 5 && dest.getCol() == 0)){
            return getChessPieceAt(new BoardPoint(5, 1)) == null
                    && getChessPieceAt(new BoardPoint(5, 2)) == null;
        }
        if ((src.getRow() == 3 && src.getCol() == 3 && dest.getRow() == 3 && dest.getCol() == 6)
                || (src.getRow() == 3 && src.getCol() == 6 && dest.getRow() == 3 && dest.getCol() == 3)){
            return getChessPieceAt(new BoardPoint(3, 4)) == null
                    && getChessPieceAt(new BoardPoint(3, 5)) == null;
        }
        if ((src.getRow() == 4 && src.getCol() == 3 && dest.getRow() == 4 && dest.getCol() == 6)
                || (src.getRow() == 4 && src.getCol() == 6 && dest.getRow() == 4 && dest.getCol() == 3)){
            return getChessPieceAt(new BoardPoint(4, 4)) == null
                    && getChessPieceAt(new BoardPoint(4, 5)) == null;
        }
        if ((src.getRow() == 5 && src.getCol() == 3 && dest.getRow() == 5 && dest.getCol() == 6)
                || (src.getRow() == 5 && src.getCol() == 6 && dest.getRow() == 5 && dest.getCol() == 3)){
            return getChessPieceAt(new BoardPoint(5, 4)) == null
                    && getChessPieceAt(new BoardPoint(5, 5)) == null;
        }

        if ((src.getRow() == 2 && src.getCol() == 1 && dest.getRow() == 6 && dest.getCol() == 1)
                || (src.getRow() == 6 && src.getCol() == 1 && dest.getRow() == 2 && dest.getCol() == 1)){
            return getChessPieceAt(new BoardPoint(3, 1)) == null
                    && getChessPieceAt(new BoardPoint(4, 1)) == null
                    && getChessPieceAt(new BoardPoint(5, 1)) == null;
        }
        if ((src.getRow() == 2 && src.getCol() == 2 && dest.getRow() == 6 && dest.getCol() == 2)
                || (src.getRow() == 6 && src.getCol() == 2 && dest.getRow() == 2 && dest.getCol() == 2)){
            return getChessPieceAt(new BoardPoint(3, 2)) == null
                    && getChessPieceAt(new BoardPoint(4, 2)) == null
                    && getChessPieceAt(new BoardPoint(5, 2)) == null;
        }
        if ((src.getRow() == 2 && src.getCol() == 4 && dest.getRow() == 6 && dest.getCol() == 4)
                || (src.getRow() == 6 && src.getCol() == 4 && dest.getRow() == 2 && dest.getCol() == 4)){
            return getChessPieceAt(new BoardPoint(3, 4)) == null
                    && getChessPieceAt(new BoardPoint(4, 4)) == null
                    && getChessPieceAt(new BoardPoint(5, 4)) == null;
        }
        if ((src.getRow() == 2 && src.getCol() == 5 && dest.getRow() == 6 && dest.getCol() == 5)
                || (src.getRow() == 6 && src.getCol() == 5 && dest.getRow() == 2 && dest.getCol() == 5)){
            return getChessPieceAt(new BoardPoint(3, 5)) == null
                    && getChessPieceAt(new BoardPoint(4, 5)) == null
                    && getChessPieceAt(new BoardPoint(5, 5)) == null;
        }
        return false;
    }
}
