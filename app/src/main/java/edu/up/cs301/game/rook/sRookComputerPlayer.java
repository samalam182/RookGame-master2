package edu.up.cs301.game.rook;

import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * Created by hoser18 on 11/8/2016.
 */
public class sRookComputerPlayer extends RookComputerPlayer
{
    /**
     * Constructor for the sRookComputerPlayer class
     */
    public sRookComputerPlayer(String name)
    {
        // creates a computer player whose average reaction time is half a second
        super(name, 0.5);
    }

    /**
     * called when we receive a message, typically from the game
     */
    protected void receiveInfo(GameInfo info)
    {
        // if there is no game state, ignore it
        if (!(info instanceof RookState))
        {
            return;
        }

        // smart computer player makes a move based on what subStage the game is in
        if (savedState.getSubStage() == savedState.BID)
        {
            // the smart computer player will determine, algorithmically, how good its hand is
            // and will base the bid on this information

            // how many cards in each suit
            // howm many high cards (10-14)
            // how many point cards

            if (true)
            {
                // create a new action
                game.sendAction(new RookBidAction(this));
            }
            else
            {
                game.sendAction(new RookHoldAction(this));
            }
        }
        else if (savedState.getSubStage() == savedState.NEST)
        {
            // the smart computer player will select the cards that it wants to place into the nest
            // it will try to gain as many of one suit color as possible
            game.sendAction(new RookNestAction(this));
        }
        else if (savedState.getSubStage() == savedState.TRUMP)
        {
            // the smart computer player will select the trump suit, based on the number of cards
            // it has for each suit; the one it has the most of will be the trump suit
            game.sendAction(new RookTrumpAction(this));
        }
        else if (savedState.getSubStage() == savedState.PLAY)
        {
            // smart computer player will play a card, and will try to win each trick
            game.sendAction(new RookCardAction(this));
        }
    }
}