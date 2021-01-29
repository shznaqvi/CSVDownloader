package edu.aku.hassannaqvi.csvdownloader;

import androidx.databinding.ObservableArrayMap;
import androidx.lifecycle.ViewModel;

public class ChildViewModel extends ViewModel {

    public ObservableArrayMap<String, String> cells;
    private Child child;

    public void init(String player1, String player2) {
        child = new Child();
        cells = new ObservableArrayMap<>();
    }

/*    public void onClickedCellAt(int row, int column) {
        if (child.cells[row][column] == null) {
            game.cells[row][column] = new Cell(game.currentPlayer);
            cells.put(stringFromNumbers(row, column), game.currentPlayer.value);
            if (game.hasGameEnded())
                game.reset();
            else
                game.switchPlayer();
        }
    }

    public LiveData<Player> getWinner() {
        return game.winner;
    }*/


}
