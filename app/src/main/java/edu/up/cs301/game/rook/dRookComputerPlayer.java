package edu.up.cs301.game.rook;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;

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

        //savedState.setSubStage(1);

        if (this.playerNum == 1) {
            if (savedState.getSubStage() == savedState.WAIT) {
                return;
            } else if (savedState.getSubStage() == savedState.BID) {
                Log.i("Reached bidding stage", "" + this.playerNum);
                // the dumb computer player will randomly decide to bid or pass
                double randSelection = 4;

                if (randSelection < 0.5) {
                    // when randSelection is less than 0.5, the dumb computer player will pass
                    game.sendAction(new RookHoldAction(this));
                    Log.i("Sent hold action", "" + this.playerNum);
                } else {
                    // when randSelection is more than 0.5, the dumb computer player will bid

                    //int randBidVal = (int)Math.random()*10;
                    int randBidVal = 4;
                    int addBid;
                    {
                        if (randBidVal <= 3) {
                            addBid = 0;
                        } else if (randBidVal > 3 && randBidVal <= 6) {
                            addBid = 5;
                        } else {
                            addBid = 10;
                        }
                    }


                    int prevBid = savedState.getHighestBid();
                    Log.i("Previous bid", "" + prevBid);

                    int myBid = prevBid + addBid;
                    game.sendAction(new RookBidAction(this, 100));
                    Log.i("Sent Bid Action", "" + this.playerNum + "," + myBid);
                }
            } else if (savedState.getSubStage() == savedState.NEST) {
                // the dumb computer player will randomly select 5 cards from their hand to place
                // into the nest

                ArrayList<Card> handCards = savedState.playerHands[this.playerNum];
                Log.i("Hand cards", "" + this.playerNum);
                ArrayList<Card> nestCards = savedState.nest;
                Log.i("Nest cards", "" + nestCards);


                //Log.i("RandHandCardIndex", "");

                ArrayList<Card> cardsFromNest = new ArrayList<Card>();
                ArrayList<Card> cardsFromHand = new ArrayList<Card>();
                for (int y = 0; y < 5; y++) {
                    double randPilePicker = Math.random();

                    if (randPilePicker < 0.5) {

                        // picks from the nest

                        double randNestCardIndex = Math.random() * 5;
                        int randNestCardIndexInt = (int) randNestCardIndex;
                        cardsFromNest.add(nestCards.get(randNestCardIndexInt));

                    } else {
                        // picks from the hand
                        double randHandCardIndex = Math.random() * 9;
                        int randHandCardIndexInt = (int) randHandCardIndex;
                        cardsFromHand.add(handCards.get(randHandCardIndexInt));

                    }
                }

//            if (randNestCardIndex < 1)
//            {
//                nestCards.get(0);
//            }
//            else if (randNestCardIndex < 2)
//            {
//                nestCards.get(1);
//            }
//            else if (randNestCardIndex < 3)
//            {
//                nestCards.get(2);
//            }
//            else if (randNestCardIndex < 4)
//            {
//                nestCards.get(3);
//            }
//            else
//            {
//                nestCards.get(4);
//            }

//            if (randHandCardIndex < 1)
//            {
//                handCards.get(0);
//            }
//            else if (randHandCardIndex < 2)
//            {
//                handCards.get(1);
//            }
//            else if (randHandCardIndex < 3)
//            {
//                handCards.get(2);
//            }
//            else if (randHandCardIndex < 4)
//            {
//                handCards.get(3);
//            }
//            else if (randHandCardIndex < 5)
//            {
//                handCards.get(4);
//            }
//            else if (randHandCardIndex < 6)
//            {
//                handCards.get(5);
//            }
//            else if (randHandCardIndex < 7)
//            {
//                handCards.get(6);
//            }
//            else if (randHandCardIndex < 8)
//            {
//                handCards.get(7);
//            }
//            else
//            {
//                handCards.get(8);
//            }
                Log.i("Nest Card index", "" + nestCards);
                Log.i("Hand Card Index", "" + handCards);


                game.sendAction(new RookNestAction(this, cardsFromNest, cardsFromHand));
                Log.i("Send Nest Action", "" + this.playerNum + "," + cardsFromNest + "," + cardsFromHand);
            } else if (savedState.getSubStage() == savedState.TRUMP) {
                // the dumb computer player will randomly choose a trump suit

                double randSuitPick = Math.random() * 4;
                int trumpSuit;

                if (randSuitPick < 1) {
                    trumpSuit = Color.RED;
                } else if (randSuitPick >= 1 && randSuitPick < 2) {
                    trumpSuit = Color.BLACK;
                } else if (randSuitPick >= 2 && randSuitPick < 3) {
                    trumpSuit = Color.YELLOW;
                } else {
                    trumpSuit = Color.GREEN;
                }
                game.sendAction(new RookTrumpAction(this, trumpSuit));

            } else if (savedState.getSubStage() == savedState.PLAY) {
                // the dumb computer player will randomly choose a card to play

                double randIndex = Math.random() * 9;

                int indexOfCard;

                if (randIndex >= 0 && randIndex < 1) {
                    indexOfCard = 0;
                } else if (randIndex >= 1 && randIndex < 2) {
                    indexOfCard = 1;
                } else if (randIndex >= 2 && randIndex < 3) {
                    indexOfCard = 2;
                } else if (randIndex >= 3 && randIndex < 4) {
                    indexOfCard = 3;
                } else if (randIndex >= 4 && randIndex < 5) {
                    indexOfCard = 4;
                } else if (randIndex >= 5 && randIndex < 6) {
                    indexOfCard = 5;
                } else if (randIndex >= 6 && randIndex < 7) {
                    indexOfCard = 6;
                } else if (randIndex >= 7 && randIndex < 8) {
                    indexOfCard = 7;
                } else {
                    indexOfCard = 8;
                }
                game.sendAction(new RookCardAction(this, indexOfCard));

            }
        }
    }

//    protected void timerTicked()
//    {
//
//    }
}


