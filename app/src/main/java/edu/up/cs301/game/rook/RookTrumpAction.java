package edu.up.cs301.game.rook;

import edu.up.cs301.game.GamePlayer;

/**
 * A game-move-action object that will take place during the trump phase of the Rook game,
 * which will be sent to the local-game once a player decides what trump color will be picked
 *
 * @author Sam DeWhitt, Eric Hoser, Mitchell Nguyen, Alexander Nowlin
 * @version December 2016
 */
public class RookTrumpAction extends GameAction
{
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    // the color of the trump suit
    private int trumpColor;

    // constructs a trump action
    public RookTrumpAction(GamePlayer player, int suitColor) {
        super(player);
        trumpColor = suitColor;
    }
    // returns the value of the trump suit
    public int getTrumpColor(){
        return trumpColor;
    }
}


