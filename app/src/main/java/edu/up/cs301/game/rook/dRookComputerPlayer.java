package edu.up.cs301.game.rook;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.infoMsg.GameInfo;

import static java.lang.reflect.Array.getInt;

/**
 * Contains the algorithms and functions that are included in the "Dumb Computer Player"
 * for the game of Rook.
 *
 * At any stage of the game including the phases for bidding, interacting with the nest,
 * choosing the trump suit, and placing down cards from their hand into the trick pile,
 * the Dumb Computer Player will make random decisions on their interactions of the cards
 * that are available to them.
 *
 * @author Sam DeWhitt, Eric Hoser, Mitchell Nguyen, Alexander Nowlin
 * @version December 2016
 */
public class dRookComputerPlayer extends RookComputerPlayer {

    /**
     *
     * constructor for the dRookComputerPlayer class
     *
     */
    public dRookComputerPlayer(String name) {
        // creates a Dumb Computer Player whose average reaction time is one second
        super(name, 2.0);
    }

    /**
     * called when we receive a message, typically from the local game
     */
    protected void receiveInfo(GameInfo info) {
        // if there is no game state, ignore it
        if (!(info instanceof RookState)) {
            return;
        }

        // update the game state
        savedState = (RookState) info;

        // if it's the Dumb Computer Player's turn...
        if (this.playerNum == savedState.getActivePlayer()) {

            // the Dumb Computer Player does not perform any actions during the "WAIT"-stage
            if (savedState.getSubStage() == savedState.WAIT) {
                return;
            }

            // the Dumb Computer Player will make a random decision during the bidding phase
            else if (savedState.getSubStage() == savedState.BID)
            {
                // the Dumb Computer Player continues to "Hold" from bidding after making a pass
                if (savedState.pass[this.playerNum])
                {
                    game.sendAction(new RookHoldAction(this));
                }

                Log.i("Reached bidding stage", "" + this.playerNum);

                // the Dumb Computer Player will randomly decide to bid or pass
                int randSelection = (int) (Math.random() * 10);

                // when randSelection is less than 0.5, the dumb computer player will pass
                if (randSelection < 4) {
                    game.sendAction(new RookHoldAction(this));
                    Log.i("Sent hold action", "" + this.playerNum);
                } else {
                    // when randSelection is more than 0.5, the dumb computer player will bid
                    // a random amount that is either 5 or 10 points above the previous bid
                    int randBidVal = (int) (Math.random() * 10);

                    // variable that keeps track of how much higher
                    // the Dumb Computer Player will bid
                    int addBid = 0;

                    if (randBidVal <= 7 && savedState.getHighestBid() + 5 <= 120) {
                        // there's a 70% chance that the Dumb Computer Player will
                        // make a bid 5 points higher than the previous bid (while also
                        // checking to see that their bid doesn't exceed the max value of 120 points)
                        addBid = 5;
                    }
                    else if (randBidVal >= 8 && savedState.getHighestBid() + 10 <= 120) {
                        // there's a 20% chance that the Dumb Computer Player will
                        // make a bid 10 points higher than the previous bid (while also
                        // checking to see that their bid doesn't exceed the max value of 120 points)
                        addBid = 10;
                    }
                    else
                    {
                        // makes sure that Dumb Computer Player ultimately either bids or passes
                        game.sendAction(new RookHoldAction(this));
                    }

                    // gather information about the previous bid that was made by
                    // the latest highest bidder of the round
                    int prevBid = savedState.getHighestBid();
                    Log.i("Previous bid", "" + prevBid);

                    // make sure that the previous bid hasn't reached the max value of 120 points
                    if (prevBid < 120) {
                        // add the randomly chosen bid-amount to the previous bid's value
                        int myBid = prevBid + addBid;

                        // send a bid-action to the local-game
                        game.sendAction(new RookBidAction(this, myBid));
                        Log.i("Sent Bid Action", "" + this.playerNum + "," + myBid);
                    }
                }
            }

            // the Dumb Computer Player will make a random decision on what cards to trade
            // from their own hand with the nest
            else if (savedState.getSubStage() == savedState.NEST) {
                // the Dumb Computer Player randomly selects 5 cards from
                // their hand to place into the nest (and vice versa)
                ArrayList<Card> handCards = savedState.playerHands[this.playerNum];
                Log.i("Hand cards", "" + this.playerNum);
                ArrayList<Card> nestCards = savedState.nest;
                Log.i("Nest cards", "" + nestCards);

                // gathers information about which cards will be traded between
                // the Dumb Computer Player's hand and the nest
                ArrayList<Card> cardsFromNest = new ArrayList<Card>();
                ArrayList<Card> cardsFromHand = new ArrayList<Card>();

                //
                for (int y = 0; y < 5; y++) {
                    double randPilePicker = Math.random();

                    if (randPilePicker < 0.5) {

                        // picks from the nest

                        int randNestCardIndex = (int) (Math.random() * 5);
                        if (!cardsFromNest.contains(nestCards.get(randNestCardIndex))) {
                            cardsFromNest.add(nestCards.get(randNestCardIndex));
                        }
                    }
                }

                int handIndex = 0;
                for (int z = 0; z < cardsFromNest.size(); z++) {
                    // picks from the hand
                    int randHandCardIndex = (int) (Math.random() * 9);
                    if (!cardsFromHand.contains(handCards.get(randHandCardIndex))) {
                        cardsFromHand.add(handCards.get(randHandCardIndex));
                    } else {
                        int nextTryRandHandCard = (int) (Math.random() * 9);
                        if (!cardsFromHand.contains(handCards.get(nextTryRandHandCard))) ;
                        {
                            cardsFromHand.add(handCards.get(nextTryRandHandCard));
                        }
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
            } else if (savedState.getSubStage() == savedState.TRUMP)
            {
                // the dumb computer player will randomly choose a trump suit

                double randSuitPick = Math.random() * 4;
                int trumpSuit;

                if (randSuitPick < 1)
                {
                    trumpSuit = 0;
                    Log.i("Trump", "Picking BLACK");
                }
                else if (randSuitPick >= 1 && randSuitPick < 2)
                {
                    trumpSuit = 1;
                    Log.i("Trump", "Picking YELLOW");
                }
                else if (randSuitPick >= 2 && randSuitPick < 3)
                {
                    trumpSuit = 2;
                    Log.i("Trump", "Picking GREEN");
                }
                else
                {
                    trumpSuit = 3;
                    Log.i("Trump", "Picking RED");
                }

                savedState.setTrump(trumpSuit);
                game.sendAction(new RookTrumpAction(this, trumpSuit));

            } else if (savedState.getSubStage() == savedState.PLAY) {
                // the dumb computer player will randomly choose a card to play

                int randIndex = (int) (Math.random() * 9);

                int indexOfCard;
                //ArrayList<Integer> numberToAdd = new ArrayList<Integer>(9);
                //ArrayList<Integer> numbersPicked = new ArrayList<Integer>(9);

                ArrayList<Card> cardInHand = savedState.playerHands[this.playerNum];


                ArrayList<Card> copyHand = (ArrayList<Card>) cardInHand.clone();
                for(Card c : cardInHand)
                {
                    if(c.getPlayed() == true)
                    {
                        copyHand.remove(c);
                    }
                }
//                    if (randIndex == 0) {
//                        indexOfCard = 0;
//                    } else if (randIndex == 1) {
//                        indexOfCard = 1;
//                    } else if (randIndex == 2) {
//                        indexOfCard = 2;
//                    } else if (randIndex == 3) {
//                        indexOfCard = 3;
//                    } else if (randIndex == 4) {
//                        indexOfCard = 4;
//                    } else if (randIndex == 5) {
//                        indexOfCard = 5;
//                    } else if (randIndex == 6) {
//                        indexOfCard = 6;
//                    } else if (randIndex == 7) {
//                        indexOfCard = 7;
//                    } else {
//                        indexOfCard = 8;
//                    }


                int pickedCard = (int)(Math.random()*copyHand.size());

                    //numberToAdd.add(indexOfCard);
//                    if (!cardInHand.get(indexOfCard).beenPlayed) {
//                        //numbersPicked.add(indexOfCard);
//                        cardInHand.get(indexOfCard).setPlayed();
//                        game.sendAction(new RookCardAction(this, indexOfCard));
//                    } else {
//                        int nextIndexOfCard = (int) (Math.random() * 9);
//                        numbersPicked.add(nextIndexOfCard);
//                        if (!cardInHand.get(nextIndexOfCard).beenPlayed){
//                            cardInHand.get(nextIndexOfCard).setPlayed();
//                            game.sendAction(new RookCardAction(this, nextIndexOfCard));
//                        }
//
                    Card tempCard = copyHand.get(pickedCard);
                    //new Card(copyHand.get(pickedCard).getSuit(), copyHand.get(pickedCard).getNumValue());
                    game.sendAction(new RookCardAction(this, cardInHand.indexOf(tempCard)));


            }
        }
    }

//    protected void timerTicked()
//    {
//
//    }
}


