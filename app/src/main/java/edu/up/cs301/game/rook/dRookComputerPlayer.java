package edu.up.cs301.game.rook;

import android.graphics.Color;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.infoMsg.GameInfo;

import static java.lang.reflect.Array.getInt;

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
        super(name, 2.0);
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

                double randBidVal = Math.random()*10;
                int addBid;
                {
                    if (randBidVal <= 3)
                    {
                        addBid = 0;
                    }
                    else if (randBidVal > 3 && randBidVal <= 6)
                    {
                        addBid = 5;
                    }
                    else
                    {
                        addBid=10;
                    }
                }


                int prevBid = 0;
                int[] prevBidArray=null;
                for (int p = 0; p < savedState.getBids().length; p++)
                {
                    prevBidArray = savedState.getBids();
                    prevBid = getInt(prevBidArray, savedState.getBids().length-1);
                }

                int myBid = prevBid+addBid;
                game.sendAction(new RookBidAction(this, myBid));
            }
        }
        else if (savedState.getSubStage() == savedState.NEST)
        {
            // the dumb computer player will randomly select 5 cards from their hand to place
            // into the nest

            Card myCard = null;
            for (int j = 0; j < 5; j++)
            {
                double cardIndex = Math.random()*5;








                //game.sendAction(new RookNestAction(this, myCard));
            }

        }

        else if (savedState.getSubStage() == savedState.TRUMP)
        {
            // the dumb computer player will randomly choose a trump suit

            double randSuitPick = Math.random()*4;
            int trumpSuit;

            if (randSuitPick < 1)
            {
                trumpSuit = Color.RED;
            }
            else if (randSuitPick >=1 && randSuitPick < 2)
            {
                trumpSuit = Color.BLACK;
            }
            else if (randSuitPick >= 2 && randSuitPick < 3)
            {
                trumpSuit = Color.YELLOW;
            }
            else
            {
                trumpSuit = Color.GREEN;
            }
            game.sendAction(new RookTrumpAction(this, trumpSuit));

        }
        else if (savedState.getSubStage() == savedState.PLAY)
        {
            // the dumb computer player will randomly choose a card to play

            double randIndex = Math.random()*9;

            int indexOfCard;

            if (randIndex >=0 && randIndex < 1)
            {
                indexOfCard = 0;
            }
            else if (randIndex >= 1 && randIndex < 2)
            {
                indexOfCard = 1;
            }
            else if (randIndex >= 2 && randIndex < 3)
            {
                indexOfCard = 2;
            }
            else if (randIndex >= 3 && randIndex < 4)
            {
                indexOfCard = 3;
            }
            else if (randIndex >= 4 && randIndex < 5)
            {
                indexOfCard = 4;
            }
            else if (randIndex >= 5 && randIndex < 6)
            {
                indexOfCard = 5;
            }
            else if (randIndex >= 6 && randIndex < 7)
            {
                indexOfCard = 6;
            }
            else if (randIndex >= 7 && randIndex < 8)
            {
                indexOfCard = 7;
            }
            else
            {
                indexOfCard = 8;
            }
            game.sendAction(new RookCardAction(this, indexOfCard));

        }
    }

//    protected void timerTicked()
//    {
//
//    }
}


