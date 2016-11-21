package edu.up.cs301.game.rook.CardActions;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.rook.RookCardAction;

/**
 * Created by E6420i5 on 11/20/2016.
 */

public class bFiveAction extends RookCardAction {
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public bFiveAction(GamePlayer player) {
        super(player);
        buttonNum = 5;
    }
}
