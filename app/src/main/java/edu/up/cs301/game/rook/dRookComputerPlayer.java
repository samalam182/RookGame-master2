package edu.up.cs301.game.rook;

import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * Created by hoser18 on 11/8/2016.
 */
public class dRookComputerPlayer extends RookComputerPlayer
{
    /**
     * constructor for the dRookComputerPlayer class
     */
    public dRookComputerPlayer(String name)
    {
        // creates a computer player whose average reaction time is one second
        super(name, 1.0);
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

        // update the game state
        savedState = (RookState)info;

        // the dumb computer player makes a move based on what subStage the game is in

        if (savedState.getSubStage() == savedState.BID)
        {
            // the dumb computer player will randomly decide to bid or pass
            double randSelection = Math.random();

            if (randSelection < 0.5)
            {
                // when randSelection is less than 0.5, the dumb computer player will pass
                game.sendAction(new RookHoldAction(this));
                // this is okay
            }
            else
            {
                // when randSelection is more than 0.5, the dumb computer player will bid

                // create a new RookBidAction variable, set the new bid value, and return it
                //
                game.sendAction(new RookBidAction(this));
            }
        }
        else if (savedState.getSubStage() == savedState.NEST)
        {
            // the dumb computer player will randomly select 5 cards from their hand to place
            // into the nest

            // new Action, set the 5 cards, and return it
            // add parameter to constructor
            game.sendAction(new RookNestAction(this));
        }

        else if (savedState.getSubStage() == savedState.TRUMP)
        {
            // the dumb computer player will randomly choose a trump suit
            game.sendAction(new RookTrumpAction(this));

        }
        else if (savedState.getSubStage() == savedState.PLAY)
        {
            // the dumb computer player will randomly choose a card to play

            // delay for up to two seconds; then play
            sleep((int)(2000*Math.random()));
            game.sendAction(new RookCardAction(this));
        }
    }

//    protected void timerTicked()
//    {
//
//    }
}
