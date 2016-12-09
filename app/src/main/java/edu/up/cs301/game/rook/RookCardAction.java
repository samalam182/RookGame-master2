package edu.up.cs301.game.rook;

import edu.up.cs301.game.GamePlayer;

/**
 * A game-move-action object that will take place during the time when all 4 players are placing
 * down cards from their owns into the trick-pile of the Rook game,
 * which will be sent to the local-game once a player presses the specific ImageButton
 * that correlates with the specific card that they would like to select and interact with
 *
 * @author Sam DeWhitt, Eric Hoser, Mitchell Nguyen, Alexander Nowlin
 * @version December 2016
 */
public class RookCardAction extends GameAction
{

    // gathers information about which Button was pressed that represents a certain
    // card in the Human Player's hand
    public int buttonNum;

    /**
     * constructor for RookCardAction
     *
     * @param player
     *     the player who created the action
     */
    public RookCardAction(GamePlayer player, int initButton) {
        super(player);
        buttonNum = initButton;
    }

    // return the number of button, which is based on its position in the row
    // of ImageButtons that represents the cards in the Human Player's hand
    public int retButtonNum(){return buttonNum;}
}


