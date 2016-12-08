package edu.up.cs301.game.rook;

import java.util.ArrayList;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GamePlayer;

/**
 * A game-move-action object that will take place during the bidding phase of the Rook game,
 * which will be sent to the local-game once a player decides to no longer bid
 *
 * @author Sam DeWhitt, Eric Hoser, Mitchell Nguyen, Alexander Nowlin
 * @version December 2016
 */
public class RookNestAction extends GameAction{
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    // the cards in the nest
    private ArrayList<Card> littleNest;

    // the cards int he player who is interacting with the nest hand
    private ArrayList<Card> hand;

    // constructs a nest action
    public RookNestAction(GamePlayer player, ArrayList<Card> fromNest, ArrayList<Card> fromHand)
    {
        super(player);
        littleNest = fromNest;
        hand = fromHand;

    }
    // returns the cards in the nest
    public ArrayList<Card> getNest(){
        return littleNest;
    }
    // returns the cards in the player who is interacting with the nest hand
    public ArrayList<Card> getHand(){
        return hand;
    }

}


