package controller;


import listener.GameListener;
import model.*;
import model.Timer;
import view.BoardView;
import view.CellView;
import view.chessView.AnimalView;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Controller is the connection between board and view,
 * when a Controller receive a request from a view, the Controller
 * analyzes and then hands over to the board for processing
 * [in this demo the request methods are clickCell() and clickChess()]
 *
*/
public class Controller implements GameListener {

    public Board board;
    public BoardView view;
    public ArrayList<BoardPoint> canStepPoints;
    private Thread thread;

    public PlayerColor currentPlayer;
    public PlayerColor winner;
    public boolean AIPlaying;
    public Difficulty AIDiff;
    public BoardPoint selectedPoint;
    public boolean isPlayback;
    public boolean skip;

    public JLabel timeLabel;
    public static Timer timer;

    public Controller(BoardView view, Board board) {
        this.view = view;
        this.board = board;
        this.currentPlayer = PlayerColor.BLUE;
        this.winner = null;
        timeLabel = view.timeLabel;
        isPlayback = false;
        skip = false;

        view.setController(this);
        view.initiateChessComponent(board);
        view.repaint();
    }

    public void swapColor() {
        currentPlayer = currentPlayer == PlayerColor.BLUE ? PlayerColor.RED : PlayerColor.BLUE;
        if (currentPlayer == PlayerColor.BLUE)
            view.statusLabel.setText("Turn " + (board.steps.size()/2 + 1) + ": BLUE");
        else
            view.statusLabel.setText("Turn " + (board.steps.size()/2 + 1) + ": RED");
    }

    public void checkWin() {
        if (board.grid[0][3].chess != null || board.redDead.size() == 8){
            this.winner = PlayerColor.BLUE;
        }
        if (board.grid[8][3].chess != null || board.blueDead.size() == 8){
            this.winner = PlayerColor.RED;
        }
    }

    public void doWin(){
        JOptionPane.showMessageDialog(view, (winner == PlayerColor.BLUE ? "BLUE" : "RED") + " Win !");
    }

    @Override
    public void clickCell(BoardPoint point, CellView component) {
        if (selectedPoint != null && board.canMove(selectedPoint, point)) {
            board.move(selectedPoint, point);
            setCanStepFalse();
            canStepPoints = null;
            view.setChessViewAtGrid(point, view.removeChessViewAtGrid(selectedPoint));
            selectedPoint = null;
            swapColor();
            view.repaint();
            component.revalidate();
            checkWin();
            if (winner != null){
                doWin();
                reset();
                return;
            }

            if (AIPlaying){
                if (AIDiff == Difficulty.EASY){
                    easyAI();
                } else if (AIDiff == Difficulty.NORMAL){
                    normalAI();
                } else if (AIDiff == Difficulty.HARD){
                    hardAI();
                }
            }
        }
    }

    @Override
    public void clickChess(BoardPoint point, AnimalView component) {
        if (selectedPoint == null) {
            if (board.getChessOwner(point).equals(currentPlayer)) {
                canStepPoints = getCanStepPoints(point);
                selectedPoint = point;
                component.setSelected(true);
                component.revalidate();
                component.repaint();
                view.repaint();
                view.revalidate();
            }
        } else if (selectedPoint.equals(point)) {
            selectedPoint = null;
            canStepPoints = null;
            setCanStepFalse();
            component.setSelected(false);
            component.repaint();
            component.revalidate();
            view.repaint();
            view.revalidate();
        } else if (board.canCapture(selectedPoint, point)) {
            board.capture(selectedPoint, point);
            view.removeChessViewAtGrid(point);
            view.setChessViewAtGrid(point, view.removeChessViewAtGrid(selectedPoint));
            selectedPoint = null;
            setCanStepFalse();
            swapColor();
            view.repaint();
            view.revalidate();
            component.revalidate();

            checkWin();
            if (winner != null){
                doWin();
                reset();
                return;
            }

            if (AIPlaying){
                if (AIDiff == Difficulty.EASY){
                    easyAI();
                } else if (AIDiff == Difficulty.NORMAL){
                    normalAI();
                } else if (AIDiff == Difficulty.HARD){
                    hardAI();
                }
            }
        }
    }

    public void easyAI() {
        System.out.println("easyAI");

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                } catch (Exception e){
                    e.printStackTrace();
                }

                BoardPoint[] points = eastAIGetPoint();
                BoardPoint src = points[0];
                BoardPoint dest = points[1];

                if (board.getChessPieceAt(dest) == null){
                    board.move(src, dest);
                    view.setChessViewAtGrid(dest, view.removeChessViewAtGrid(src));
                } else {
                    board.capture(src, dest);
                    view.removeChessViewAtGrid(dest);
                    view.setChessViewAtGrid(dest, view.removeChessViewAtGrid(src));
                }
                canStepPoints = null;
                setCanStepFalse();
                swapColor();
                view.repaint();
                view.gridViews[dest.getRow()][dest.getCol()].revalidate();
                checkWin();
                if (winner != null){
                    doWin();
                    reset();
                }
            }
        });
        thread.start();
    }

    public BoardPoint[] eastAIGetPoint(){
        ArrayList<BoardPoint> canMove = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                if (board.grid[i][j].chess != null && board.grid[i][j].chess.getOwner() == currentPlayer){
                    ArrayList<BoardPoint> list = getCanStepPoints(new BoardPoint(i, j));
                    if (list.size() != 0) canMove.add(new BoardPoint(i, j));
                }
            }
        }

        int size = canMove.size();
        Random random = new Random();
        int index = random.nextInt(size);
        BoardPoint src = canMove.get(index);

        ArrayList<BoardPoint> list = getCanStepPoints(src);
        size = list.size();
        index = random.nextInt(size);
        BoardPoint dest = list.get(index);

        return new BoardPoint[]{src, dest};
    }

    public void normalAI() {
        System.out.println("normalAI");
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(450);
                } catch (Exception e){
                    e.printStackTrace();
                }

                BoardPoint src = null;
                BoardPoint dest = null;
                Random random = new Random();

                ArrayList<BoardPoint[]> canCapture = new ArrayList<>();
                ArrayList<BoardPoint[]> beCapture = new ArrayList<>(); // index0 己方，被吃
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 7; j++) {
                        BoardPoint src0 = new BoardPoint(i, j);
                        Chess chess = board.getChessPieceAt(src0);
                        if (chess != null && chess.getOwner() == currentPlayer){
                            for (int k = 0; k < 9; k++) {
                                for (int l = 0; l < 7; l++) {
                                    BoardPoint dest0 = new BoardPoint(k, l);
                                    Chess dead = board.getChessPieceAt(dest0);
                                    if (dead != null && dead.getOwner() != currentPlayer){
                                        if (board.canCapture(src0, dest0)){
                                            canCapture.add(new BoardPoint[]{src0, dest0});
                                        }
                                    }
                                }
                            }
                        }
                        if (chess != null && chess.getOwner() != currentPlayer){
                            for (int k = 0; k < 9; k++) {
                                for (int l = 0; l < 7; l++) {
                                    BoardPoint dest1 = new BoardPoint(k, l);
                                    Chess dead = board.getChessPieceAt(dest1);
                                    if (dead != null && dead.getOwner() == currentPlayer){
                                        if (board.canCapture(src0, dest1)){
                                            beCapture.add(new BoardPoint[]{dest1, src0});
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (canCapture.size() != 0) {
                    BoardPoint[] points = canCapture.get(random.nextInt(canCapture.size()));
                    src = points[0];
                    dest = points[1];
                } else if (beCapture.size() != 0) {
                    BoardPoint[] points = beCapture.get(random.nextInt(beCapture.size()));
                    BoardPoint move = points[0];
                    ArrayList<BoardPoint> canMovePoints = getCanStepPoints(move);
                    if (canMovePoints.size() != 0){
                        src = move;
                        dest = canMovePoints.get(random.nextInt(canMovePoints.size()));
                    } else {
                        BoardPoint[] points2 = eastAIGetPoint();
                        src = points2[0];
                        dest = points2[1];
                    }
                } else {
                    BoardPoint[] points = eastAIGetPoint();
                    src = points[0];
                    dest = points[1];
                }

                if (board.getChessPieceAt(dest) == null){
                    board.move(src, dest);
                    view.setChessViewAtGrid(dest, view.removeChessViewAtGrid(src));
                } else {
                    board.capture(src, dest);
                    view.removeChessViewAtGrid(dest);
                    view.setChessViewAtGrid(dest, view.removeChessViewAtGrid(src));
                }
                canStepPoints = null;
                setCanStepFalse();
                swapColor();
                view.repaint();
                view.gridViews[dest.getRow()][dest.getCol()].revalidate();
                checkWin();
                if (winner != null){
                    doWin();
                    reset();
                }
            }
        });
        thread.start();
    }

    public void hardAI() {
        System.out.println("hardAI");
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(600);
                } catch (Exception e){
                    e.printStackTrace();
                }

                //toDo: get src, dest
                BoardPoint src = null;
                BoardPoint dest = null;

                if (board.getChessPieceAt(dest) == null){
                    board.move(src, dest);
                    view.setChessViewAtGrid(dest, view.removeChessViewAtGrid(src));
                } else {
                    board.capture(src, dest);
                    view.removeChessViewAtGrid(dest);
                    view.setChessViewAtGrid(dest, view.removeChessViewAtGrid(src));
                }
                canStepPoints = null;
                setCanStepFalse();
                swapColor();
                view.repaint();
                view.gridViews[dest.getRow()][dest.getCol()].revalidate();
                checkWin();
                if (winner != null){
                    doWin();
                    reset();
                }
            }
        });
        thread.start();
    }

    public void setCanStepFalse() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                view.gridViews[i][j].canStep = false;
            }
        }
    }

    public ArrayList<BoardPoint> getCanStepPoints(BoardPoint src) {
        ArrayList<BoardPoint> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                BoardPoint dest = new BoardPoint(i, j);
                if (board.canMove(src, dest)){
                    view.gridViews[i][j].canStep = true;
                    list.add(dest);
                }
                if (board.canCapture(src, dest)){
                    view.gridViews[i][j].canStep = true;
                    list.add(dest);
                }
            }
        }
        return list;
    }

    public void reset(){
        canStepPoints = null;
        board.initGrid();
        board.initPieces();
        view.removeChessComponent();
        view.initiateChessComponent(board);
        currentPlayer = PlayerColor.BLUE;
        selectedPoint = null;
        setCanStepFalse();
        view.statusLabel.setText("Turn 1: BLUE");
        board.steps = new ArrayList<>();
        view.repaint();
        view.revalidate();
        winner = null;

        board.redDead = new ArrayList<>();
        board.blueDead = new ArrayList<>();
        timer.time = 45;
    }

    public void saveGame(String fileName) {
        String location = "save\\" + fileName + ".txt";
        File file = new File(location);

        try {
            if(file.exists()){     // 若文档存在，询问是否覆盖
                int n = JOptionPane.showConfirmDialog(view, "存档已存在，是否覆盖?", "", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    file.delete();
                }
            }

            // 创建文档
            FileWriter fileWriter = new FileWriter(location,true);

            fileWriter.write(board.steps.size() + "");
            fileWriter.write("\n");

            for (int i = 0; i < board.steps.size(); i++){
                fileWriter.write(board.steps.get(i).toString());
                fileWriter.write("\n");
            }

            fileWriter.write(currentPlayer == PlayerColor.BLUE ? "b" : "r");
            fileWriter.write("\n");

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 7; j++) {
                    Chess chess = board.grid[i][j].chess;
                    fileWriter.write(animal2Str(chess) + " ");
                }
                fileWriter.write("\n");
            }

            fileWriter.close();
            System.out.println("Save Done");
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public boolean loadGame(){
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("save"));
        chooser.showOpenDialog(view);
        File file = chooser.getSelectedFile();

        if (!file.getName().endsWith(".txt")){
            JOptionPane.showMessageDialog(null, "检测到非法修改存档\n已重新开始",
                    "文件后缀错误", JOptionPane.ERROR_MESSAGE);
            //System.out.println("检测到非法修改存档！重新开始游戏");
            //System.out.println("后缀错误");
            reset();
            return false;
        }

        try {
            String temp;
            ArrayList<String> readList = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));

            while((temp = reader.readLine()) != null && !"".equals(temp)){
                readList.add(temp);
                //System.out.println(temp);
            }

            int num = Integer.parseInt(readList.remove(0));
//            System.out.println(num);
//            for (int i = 0; i < readList.size(); i++) {
//                System.out.println(readList.get(i));
//            }
            for (int i = 0; i <= num; i++) {
                String str = readList.get(i);
                if (i % 2 == 0 && str.charAt(0) != 'b'){
                    //System.out.println(str);
                    JOptionPane.showMessageDialog(null, "检测到非法修改存档\n已重新开始",
                            "行棋方错误", JOptionPane.ERROR_MESSAGE);
                    reset();
                    return false;
                }
                if (i % 2 == 1 && str.charAt(0) != 'r'){
                    //System.out.println(str);
                    JOptionPane.showMessageDialog(null, "检测到非法修改存档\n已重新开始",
                            "行棋方错误", JOptionPane.ERROR_MESSAGE);
                    reset();
                    return false;
                }
            }

            try {
                for (int i = num + 1; i < num + 10; i++) {
                    boolean b = true;
                    String[] chess= readList.get(i).split(" ");
                    if (chess.length != 7){
                        JOptionPane.showMessageDialog(null, "检测到非法修改存档\n已重新开始",
                                "棋盘错误，并非9*7", JOptionPane.ERROR_MESSAGE);
                        reset();
                        return false;
                    }
                    if (!checkName(chess)) b = false;
                    if (!b){
                        JOptionPane.showMessageDialog(null, "检测到非法修改存档\n已重新开始",
                                "棋子错误", JOptionPane.ERROR_MESSAGE);
                        reset();
                        return false;
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "检测到非法修改存档\n已重新开始",
                        "棋盘错误，并非9*7", JOptionPane.ERROR_MESSAGE);
                reset();
                return false;
            }

            reset();
            for (int i = 0; i < num; i++) {
                String[] info = readList.get(i).split(" ");
                BoardPoint src = new BoardPoint(Integer.parseInt(info[1].charAt(1) + ""),
                        Integer.parseInt(info[1].charAt(3) + ""));
                BoardPoint dest = new BoardPoint(Integer.parseInt(info[2].charAt(1) + ""),
                        Integer.parseInt(info[2].charAt(3) + ""));
                boolean isCapture = !info[3].equals("null");

                if (!isCapture){
                    if (!board.canMove(src, dest)){
                        JOptionPane.showMessageDialog(null, "检测到非法修改存档\n已重新开始",
                                "行棋步骤错误", JOptionPane.ERROR_MESSAGE);
                        reset();
                        return false;
                    }
                    board.move(src, dest);
                    view.setChessViewAtGrid(dest, view.removeChessViewAtGrid(src));
                    selectedPoint = null;
                    swapColor();
                    view.repaint();

                } else {
                    if (!board.canCapture(src, dest)){
                        JOptionPane.showMessageDialog(null, "检测到非法修改存档\n已重新开始",
                                "行棋步骤错误", JOptionPane.ERROR_MESSAGE);
                        reset();
                        return false;
                    }
                    board.capture(src, dest);
                    view.removeChessViewAtGrid(dest);
                    view.setChessViewAtGrid(dest, view.removeChessViewAtGrid(src));
                    swapColor();
                    view.repaint();
                    view.revalidate();
                }
            }

        } catch (Exception ex){
            //ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "无此存档\n已重新开始",
                    "错误", JOptionPane.ERROR_MESSAGE);
            reset();
        }
        return true;
    }

    public void regretOneStep(){
        board.steps.remove(board.steps.size() - 1);
        ArrayList<Step> list = board.steps;
        reset();
        for (int i = 0; i < list.size(); i++) {
            Step step = list.get(i);
            BoardPoint src = step.src;
            BoardPoint dest = step.dest;
            boolean isCapture = step.captured != null;
            if (!isCapture){
                board.move(src, dest);
                view.setChessViewAtGrid(dest, view.removeChessViewAtGrid(src));
                selectedPoint = null;
                swapColor();
                view.repaint();
            } else {
                board.capture(src, dest);
                view.removeChessViewAtGrid(dest);
                view.setChessViewAtGrid(dest, view.removeChessViewAtGrid(src));
                swapColor();
                view.repaint();
                view.revalidate();
            }
        }
    }

    private static String animal2Str(Chess chess){
        if (chess == null) return "+";
        else if (chess.getName().equals("Elephant")) return "E";
        else if (chess.getName().equals("Lion")) return "L";
        else if (chess.getName().equals("Tiger")) return "T";
        else if (chess.getName().equals("Leopard")) return "l";
        else if (chess.getName().equals("Wolf")) return "w";
        else if (chess.getName().equals("Dog")) return "d";
        else if (chess.getName().equals("Cat")) return "c";
        else if (chess.getName().equals("Rat")) return "r";
        else return "";
    }

    private static boolean checkName(String[] chess){
        for (int i = 0; i < chess.length; i++) {
            if (!chess[i].equals("E") && !chess[i].equals("L") && !chess[i].equals("T") && !chess[i].equals("l")
             && !chess[i].equals("w") && !chess[i].equals("d") && !chess[i].equals("c") && !chess[i].equals("r")
                    && !chess[i].equals("+")){
                return false;
            }
        }
        return true;
    }

    public void playback(){
        isPlayback = true;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Step> steps = board.steps;
                reset();
                for (int i = 0; i < steps.size(); i++) {
                    try {
                        Thread.sleep(500);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    Step step = steps.get(i);
                    BoardPoint src = step.src;
                    BoardPoint dest = step.dest;
                    boolean isCapture = step.captured != null;
                    if (!isCapture) {
                        board.move(src, dest);
                        view.setChessViewAtGrid(dest, view.removeChessViewAtGrid(src));
                        selectedPoint = null;
                        swapColor();
                        view.repaint();
                    } else {
                        board.capture(src, dest);
                        view.removeChessViewAtGrid(dest);
                        view.setChessViewAtGrid(dest, view.removeChessViewAtGrid(src));
                        swapColor();
                        view.repaint();
                        view.revalidate();
                    }
                }
            }
        });
        thread.start();

        isPlayback = false;
    }

}
