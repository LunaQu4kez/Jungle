package model;

import controller.Controller;

public class Timer extends Thread {
    public static int time = 45;
    public Controller controller;

    @Override
    public void run(){
        synchronized (this){
            while (true){
                PlayerColor player = controller.currentPlayer;
                boolean b = true;
                while(time > 0) {
                    time--;
                    try {
                        Thread.sleep(1000);
                        controller.timeLabel.setText("Time: " + time);
                        if (controller.currentPlayer != player){
                            controller.swapColor();
                            b = false;
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                time = 45;

                if (b){
                    BoardPoint[] points = controller.eastAIGetPoint();
                    BoardPoint src = points[0];
                    BoardPoint dest = points[1];

                    if (controller.board.getChessPieceAt(dest) == null){
                        controller.board.move(src, dest);
                        controller.view.setChessViewAtGrid(dest, controller.view.removeChessViewAtGrid(src));
                    } else {
                        controller.board.capture(src, dest);
                        controller.view.removeChessViewAtGrid(dest);
                        controller.view.setChessViewAtGrid(dest, controller.view.removeChessViewAtGrid(src));
                    }
                    controller.canStepPoints = null;
                    controller.setCanStepFalse();
                    controller.swapColor();
                    controller.view.repaint();
                    controller.view.gridViews[dest.getRow()][dest.getCol()].revalidate();
                    controller.checkWin();
                    if (controller.winner != null){
                        controller.doWin();
                        controller.reset();
                    }
                } else {
                    controller.swapColor();
                }

            }
        }

    }

    public Timer(Controller controller){
        this.controller = controller;
    }

}
