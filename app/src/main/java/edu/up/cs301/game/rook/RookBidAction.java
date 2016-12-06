package edu.up.cs301.game.rook;

import edu.up.cs301.game.GamePlayer;

/**
 * A game-move-action object that will take place during the bidding phase of the Rook game,
 * which will be sent to the local-game once a player places a certain bid amount
 *
 * @author Sam DeWhitt, Eric Hoser, Mitchell Nguyen, Alexander Nowlin
 * @version December 2016
 */
public class RookBidAction extends GameAction{
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public int bid = 0;
    public RookBidAction(GamePlayer player, int playerBid)
    {
        super(player);
        bid = playerBid;
    }
    public int getBid(){
        return bid;
    }
}


