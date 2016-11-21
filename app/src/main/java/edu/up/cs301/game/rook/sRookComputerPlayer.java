package edu.up.cs301.game.rook;

import android.graphics.Color;

import java.text.Bidi;
import java.util.ArrayList;
import static java.lang.reflect.Array.getInt;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * Created by hoser18 on 11/8/2016.
 */
public class sRookComputerPlayer extends RookComputerPlayer {
    /**
     * Constructor for the sRookComputerPlayer class
     */
    public sRookComputerPlayer(String name) {
        // creates a computer player whose average reaction time is half a second
        super(name, 0.5);
    }

    /**
     * called when we receive a message, typically from the game
     */
    protected void receiveInfo(GameInfo info) {
        // if there is no game state, ignore it
        if (!(info instanceof RookState)) {
            return;
        }

        // smart computer player makes a move based on what subStage the game is in
        if (savedState.getSubStage() == savedState.BID) {

            int myBid = 50;

            ArrayList<Card> myList = null;

            for (int i = 0; i < 4; i++) {
                ArrayList<Card> thisList = savedState.playerHands[i];

                myList = thisList;
            }

            Card myCard = null;

            int redSuitNum = 0;
            int yellSuitNum = 0;
            int greenSuitNum = 0;
            int blackSuitNum = 0;
            int rookSuit = 0;

            for (int j = 0; j < 9; j++) {
                Card thisCard = myList.get(j);

                Card arrayCard = new Card(thisCard.getSuit(), thisCard.getNumValue());

                myCard = arrayCard;

                if (myCard.getSuit() == Color.RED) {
                    // the card is a red suit
                    redSuitNum++;
                } else if (myCard.getSuit() == Color.YELLOW) {
                    // the card is a yellow suit
                    yellSuitNum++;
                } else if (myCard.getSuit() == Color.GREEN) {
                    // the Card is a green suit
                    greenSuitNum++;
                } else if (myCard.getSuit() == Color.BLACK) {
                    // the card is a Black suit
                    blackSuitNum++;
                } else {
                    // the card is the Rook
                    rookSuit++;
                }
            }

            // the smart computer player will determine, algorithmically, how good its hand is
            // and will base the bid on this information

            // how many cards in each suit

            if (redSuitNum >= 4)
            {
                myBid++;
            }
            else if (greenSuitNum >= 4)
            {
                myBid++;
            }
            else if (yellSuitNum >= 4)
            {
                myBid++;
            }
            else if (blackSuitNum >= 4)
            {
                myBid++;
            }

            if (rookSuit == 1)
            {
                myBid++;
            }

            // how many high cards (10-14)

            if (myCard.getNumValue() >= 10)
            {
                myBid++;
            }


            // how many point cards

            if (myCard.getNumValue() == 5 || myCard.getNumValue() == 10 || myCard.getNumValue() == 14)
            {
                myBid++;
            }
            else if (myCard.getNumValue() == 15)
            {
                myBid++;
            }

            int[] prevBidArray = savedState.getBids();
            int prevBid = getInt(prevBidArray, savedState.getBids().length-1);

            if (savedState.getSubStage() == savedState.BID)
            {
                if (myBid > prevBid && myBid%5==0)
                {
                    // create a new action
                    game.sendAction(new RookBidAction(this, myBid));
                }
                else if (myBid > prevBid && myBid%5 !=0)
                {
                    int myNewBid = myBid - (myBid%5);
                    game.sendAction(new RookBidAction(this, myNewBid));
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

                game.sendAction(new RookNestAction(this, null, null));
            }
            else if (savedState.getSubStage() == savedState.TRUMP)
            {
                // the smart computer player will select the trump suit, based on the number of cards
                // it has for each suit; the one it has the most of will be the trump suit

                int trumpSuit=0;

                if (redSuitNum > 3 && greenSuitNum <= 3 && blackSuitNum <= 3 && yellSuitNum <= 3)
                {
                    trumpSuit = Color.RED;
                }
                else if (greenSuitNum > 3 && redSuitNum <= 3 && blackSuitNum <= 3 && yellSuitNum <= 3)
                {
                    trumpSuit = Color.GREEN;
                }
                else if (yellSuitNum > 3 && redSuitNum <= 3 && greenSuitNum <= 3 && blackSuitNum <= 3)
                {
                    trumpSuit = Color.YELLOW;
                }
                else if (blackSuitNum > 3 && redSuitNum <= 3 && greenSuitNum <= 3 && yellSuitNum <= 3)
                {
                    trumpSuit = Color.BLACK;
                }
                game.sendAction(new RookTrumpAction(this, trumpSuit));
            }
            else if (savedState.getSubStage() == savedState.PLAY)
            {
                // smart computer player will play a card, and will try to win each trick
                game.sendAction(new RookCardAction(this));
            }
        }
    }
}
