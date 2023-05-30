package view;


import controller.Controller;
import model.*;
import view.chessView.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;

import static model.Constant.CHESSBOARD_COL_SIZE;
import static model.Constant.CHESSBOARD_ROW_SIZE;

/**
 * This class represents the checkerboard component object on the panel
 */
public class BoardView extends JPanel {
    public CellView[][] gridViews = new CellView[CHESSBOARD_ROW_SIZE.getNum()][CHESSBOARD_COL_SIZE.getNum()];
    private int CHESS_SIZE;
    private Set<BoardPoint> riverCell = new HashSet<>();
    private Set<BoardPoint> trapCell = new HashSet<>();
    private Set<BoardPoint> denCell = new HashSet<>();

    public Controller controller;

    public JLabel statusLabel;
    public JLabel timeLabel;

    public static final Color springGrass = new Color(96, 194, 73);
    public static final Color springRiver = new Color(161, 227, 226);
    public static final Color autumnGrass = new Color(215, 211, 113);
    public static final Color autumnRiver = new Color(115, 195, 186);

    public static final Color trapColor = new Color(163, 166, 164);
    public static final Color denColor = new Color(255, 255, 255);

    public BoardView(int chessSize, JLabel statusLabel, JLabel timeLabel) {
        this.statusLabel = statusLabel;
        this.timeLabel = timeLabel;
        CHESS_SIZE = chessSize;
        int width = CHESS_SIZE * 7;
        int height = CHESS_SIZE * 9;
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);// Allow mouse events to occur
        setLayout(null); // Use absolute layout.
        setSize(width, height);
        System.out.printf("chessboard width, height = [%d : %d], chess size = %d\n", width, height, CHESS_SIZE);

        initiateGridComponents();
    }


    /**
     * This method represents how to initiate ChessComponent
     * according to Board information
     */
    public void initiateChessComponent(Board board) {
        Cell[][] grid = board.getGrid();
        for (int i = 0; i < CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < CHESSBOARD_COL_SIZE.getNum(); j++) {

                if (grid[i][j].getPiece() != null) {
                    Chess chess = grid[i][j].getPiece();
                    //System.out.println(chess.getOwner());
                    if (chess.getName().equals("Elephant")){
                        gridViews[i][j].add(new ElephantView(chess.getOwner(), CHESS_SIZE));
                    }
                    if (chess.getName().equals("Lion")){
                        gridViews[i][j].add(new LionView(chess.getOwner(), CHESS_SIZE));
                    }
                    if (chess.getName().equals("Tiger")){
                        gridViews[i][j].add(new TigerView(chess.getOwner(), CHESS_SIZE));
                    }
                    if (chess.getName().equals("Leopard")){
                        gridViews[i][j].add(new LeopardView(chess.getOwner(), CHESS_SIZE));
                    }
                    if (chess.getName().equals("Wolf")){
                        gridViews[i][j].add(new WolfView(chess.getOwner(), CHESS_SIZE));
                    }
                    if (chess.getName().equals("Dog")){
                        gridViews[i][j].add(new DogView(chess.getOwner(), CHESS_SIZE));
                    }
                    if (chess.getName().equals("Cat")){
                        gridViews[i][j].add(new CatView(chess.getOwner(), CHESS_SIZE));
                    }
                    if (chess.getName().equals("Rat")){
                        gridViews[i][j].add(new RatView(chess.getOwner(), CHESS_SIZE));
                    }

                }
            }
        }

    }

    public void removeChessComponent() {
        for (int i = 0; i < CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < CHESSBOARD_COL_SIZE.getNum(); j++) {
                try {
                    gridViews[i][j].remove(0);
                } catch (Exception e){}
            }
        }

    }

    public void initiateGridComponents() {

        riverCell.add(new BoardPoint(3,1));
        riverCell.add(new BoardPoint(3,2));
        riverCell.add(new BoardPoint(4,1));
        riverCell.add(new BoardPoint(4,2));
        riverCell.add(new BoardPoint(5,1));
        riverCell.add(new BoardPoint(5,2));

        riverCell.add(new BoardPoint(3,4));
        riverCell.add(new BoardPoint(3,5));
        riverCell.add(new BoardPoint(4,4));
        riverCell.add(new BoardPoint(4,5));
        riverCell.add(new BoardPoint(5,4));
        riverCell.add(new BoardPoint(5,5));

        trapCell.add(new BoardPoint(8, 2));
        trapCell.add(new BoardPoint(8, 4));
        trapCell.add(new BoardPoint(7, 3));

        trapCell.add(new BoardPoint(0, 2));
        trapCell.add(new BoardPoint(0, 4));
        trapCell.add(new BoardPoint(1, 3));

        denCell.add(new BoardPoint(8, 3));
        denCell.add(new BoardPoint(0, 3));

        for (int i = 0; i < CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < CHESSBOARD_COL_SIZE.getNum(); j++) {
                BoardPoint temp = new BoardPoint(i, j);
                CellView cell;
                if (riverCell.contains(temp)) {
                    cell = new CellView(calculatePoint(i, j), CHESS_SIZE, CellType.SPRING_RIVER);
                    this.add(cell);
                }
                else if (trapCell.contains(temp)){
                    cell = new CellView(calculatePoint(i, j), CHESS_SIZE, CellType.TRAP);
                    this.add(cell);
                }
                else if (denCell.contains(temp)){
                    cell = new CellView(calculatePoint(i, j), CHESS_SIZE, CellType.DEN);
                    this.add(cell);
                }
                else {
                    cell = new CellView(calculatePoint(i, j), CHESS_SIZE, CellType.SPRING_GRASS);
                    this.add(cell);
                }
                gridViews[i][j] = cell;
            }
        }
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setChessViewAtGrid(BoardPoint point, AnimalView chess) {
        getGridViewAt(point).add(chess);
    }

    public AnimalView removeChessViewAtGrid(BoardPoint point) {
        // Note re-validation is required after remove / removeAll.
        AnimalView chess = (AnimalView) getGridViewAt(point).getComponents()[0];
        getGridViewAt(point).removeAll();
        getGridViewAt(point).revalidate();
        chess.setSelected(false);
        return chess;
    }

    private CellView getGridViewAt(BoardPoint point) {
        return gridViews[point.getRow()][point.getCol()];
    }

    private BoardPoint getBoardPoint(Point point) {
        System.out.println("[" + point.y/CHESS_SIZE +  ", " +point.x/CHESS_SIZE + "] Clicked");
        return new BoardPoint(point.y/CHESS_SIZE, point.x/CHESS_SIZE);
    }

    private Point calculatePoint(int row, int col) {
        return new Point(col * CHESS_SIZE, row * CHESS_SIZE);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    @Override
    public void processMouseEvent(MouseEvent e) {
        if (e.getID() == MouseEvent.MOUSE_PRESSED) {
            if ((controller.AIPlaying && controller.currentPlayer == PlayerColor.RED) || controller.isPlayback) {
            } else {
                new SoundEffect().playEffect("resource\\sound\\click.wav");
                JComponent clickedComponent = (JComponent) getComponentAt(e.getX(), e.getY());
                if (clickedComponent.getComponentCount() == 0) {
                    System.out.print("None chess here and ");
                    controller.clickCell(getBoardPoint(e.getPoint()), (CellView) clickedComponent);
                } else {
                    System.out.print("One chess here and ");
                    controller.clickChess(getBoardPoint(e.getPoint()), (AnimalView) clickedComponent.getComponents()[0]);
                }
            }
        }
    }

    @Override
    public void processMouseMotionEvent(MouseEvent e){
        if (e.getID() == MouseEvent.MOUSE_PRESSED) {
            System.out.println("pressed");
        } else if (e.getID() == MouseEvent.MOUSE_DRAGGED){
            System.out.println("dragged");
        } else if (e.getID() == MouseEvent.MOUSE_MOVED){
            System.out.println("moved");
            CellView component = (CellView) getComponentAt(e.getX(), e.getY());
            setMouseAtFalse();
            component.mouseAt = true;
            component.repaint();
            component.revalidate();
            repaint();
            revalidate();
        } else if (e.getID() == MouseEvent.MOUSE_CLICKED){
            System.out.println("clicked");
        } else if (e.getID() == MouseEvent.MOUSE_ENTERED){
            System.out.println("entered");
        } else if (e.getID() == MouseEvent.MOUSE_EXITED){
            System.out.println("exited");
        } else if (e.getID() == MouseEvent.MOUSE_RELEASED){
            System.out.println("released");
        } else if (e.getID() == MouseEvent.MOUSE_WHEEL){
            System.out.println("wheel");
        }

    }

    private void setMouseAtFalse() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                gridViews[i][j].mouseAt = false;
            }
        }
    }

    public void changeTheme(boolean isSpring){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                if (gridViews[i][j].type == CellType.SPRING_GRASS) gridViews[i][j].type = CellType.AUTUMN_GRASS;
                else if (gridViews[i][j].type == CellType.SPRING_RIVER) gridViews[i][j].type = CellType.AUTUMN_RIVER;
                else if (gridViews[i][j].type == CellType.AUTUMN_GRASS) gridViews[i][j].type = CellType.SPRING_GRASS;
                else if (gridViews[i][j].type == CellType.AUTUMN_RIVER) gridViews[i][j].type = CellType.SPRING_RIVER;
                else if (gridViews[i][j].type == CellType.OTHER_GRASS && isSpring) gridViews[i][j].type = CellType.AUTUMN_GRASS;
                else if (gridViews[i][j].type == CellType.OTHER_RIVER && isSpring) gridViews[i][j].type = CellType.AUTUMN_RIVER;
                else if (gridViews[i][j].type == CellType.OTHER_GRASS && !isSpring) gridViews[i][j].type = CellType.SPRING_GRASS;
                else if (gridViews[i][j].type == CellType.OTHER_RIVER && !isSpring) gridViews[i][j].type = CellType.SPRING_RIVER;
                else if (gridViews[i][j].type == CellType.OTHER_TRAP) gridViews[i][j].type = CellType.TRAP;
                else if (gridViews[i][j].type == CellType.OTHER_DEN) gridViews[i][j].type = CellType.DEN;
            }
        }
        repaint();
        revalidate();
    }
}
