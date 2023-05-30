package listener;

import model.BoardPoint;
import view.CellView;
import view.chessView.AnimalView;
import view.chessView.ElephantView;

public interface GameListener {

    void clickCell(BoardPoint point, CellView component);


    void clickChess(BoardPoint point, AnimalView component);

}
