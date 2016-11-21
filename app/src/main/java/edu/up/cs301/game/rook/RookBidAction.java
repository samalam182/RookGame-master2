package edu.up.cs301.game.rook;

import edu.up.cs301.game.GamePlayer;

/**
 * Created by hoser18 on 11/8/2016.
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


