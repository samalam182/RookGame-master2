package edu.up.cs301.game.rook;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;

/**
 * Created by hoser18 on 11/8/2016.
 */
public class RookLocalGame extends LocalGame {
    protected void sendUpdatedStateTo(GamePlayer p) {

    }

    protected boolean canMove(int playerIdx) {
        return false;
    }

    protected String checkIfGameOver() {
        return null;
    }

    protected boolean makeMove(GameAction action) {
        return false;
    }
}
