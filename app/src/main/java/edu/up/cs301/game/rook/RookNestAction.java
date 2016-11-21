package edu.up.cs301.game.rook;

import java.util.ArrayList;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GamePlayer;

/**
 * Created by hoser18 on 11/8/2016.
 */
public class RookNestAction extends GameAction{
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    private ArrayList<Card> littleNest;
    private ArrayList<Card> hand;
    public RookNestAction(GamePlayer player, ArrayList<Card> fromNest, ArrayList<Card> fromHand) {
        super(player);
        littleNest = fromNest;
        hand = fromHand;

    }
    public ArrayList<Card> getNest(){
        return littleNest;
    }
    public ArrayList<Card> getHand(){
        return hand;
    }

}


