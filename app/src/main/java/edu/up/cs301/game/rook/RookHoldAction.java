package edu.up.cs301.game.rook;

import edu.up.cs301.game.GamePlayer;

/**
 * A game-move-action object that will take place during the bidding phase of the Rook game,
 * which will be sent to the local-game once a player decides to make a pass as alternative
 * choice for a player to make instead of bidding a certain amount
 *
 * @author Sam DeWhitt, Eric Hoser, Mitchell Nguyen, Alexander Nowlin
 * @version December 2016
 */
public class RookHoldAction extends GameAction{
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     *
     */
    public RookHoldAction(GamePlayer player)

    {
        super(player);
    }
}
