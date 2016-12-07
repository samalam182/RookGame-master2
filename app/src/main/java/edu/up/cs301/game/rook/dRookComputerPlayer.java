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

    public int handSize = 9;
    /**
     * constructor for the dRookComputerPlayer class
     */
    public dRookComputerPlayer(String name) {
        // creates a computer player whose average reaction time is one second
        super(name, 2.0);
    }

    /**
     * called when we receive a message, typically from the game
     */
    protected void receiveInfo(GameInfo info) {
        // if there is no game state, ignore it
        if (!(info instanceof RookState)) {
            return;
        }

        // update the game state
        savedState = (RookState) info;

        // the dumb computer player makes a move based on what subStage the game is in

        //savedState.setSubStage(1);

        if (this.playerNum == savedState.getActivePlayer()) {
            if (savedState.getSubStage() == savedState.WAIT) {
                return;
            } else if (savedState.getSubStage() == savedState.BID)
            {
                if (savedState.pass[this.playerNum])
                {
                    game.sendAction(new RookHoldAction(this));
                }
                Log.i("Reached bidding stage", "" + this.playerNum);
                // the dumb computer player will randomly decide to bid or pass
                //int randSelection = (int)(Math.random()*10);
                int randSelection = (int) (Math.random() * 10);

                if (randSelection < 4) {
                    // when randSelection is less than 0.5, the dumb computer player will pass
                    game.sendAction(new RookHoldAction(this));
                    Log.i("Sent hold action", "" + this.playerNum);
                } else {
                    // when randSelection is more than 0.5, the dumb computer player will bid

                    int randBidVal = (int) (Math.random() * 10);

                    int addBid = 0;

                    if (randBidVal <= 7 && savedState.getHighestBid() + 5 <= 120) {
                        addBid = 5;
                    }
                    else if (randBidVal >= 8 && savedState.getHighestBid() + 10 <= 120) {
                        addBid = 10;

                    }
                    else
                    {
                        game.sendAction(new RookHoldAction(this));
                    }


                    int prevBid = savedState.getHighestBid();
                    Log.i("Previous bid", "" + prevBid);

                    if (prevBid < 120) {
                        //int myBid = prevBid + addBid;
                        //int myBid = savedState.getHighestBid() + addBid;
                        int myBid = prevBid + addBid;
                        game.sendAction(new RookBidAction(this, myBid));
                        Log.i("Sent Bid Action", "" + this.playerNum + "," + myBid);
                    }
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
                //int cardIndex = 0;
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
                ArrayList<Integer> numberToAdd = new ArrayList<Integer>(9);
                ArrayList<Integer> numbersPicked = new ArrayList<Integer>(9);

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
//                    }


                    Card tempCard = copyHand.get(pickedCard);
                    game.sendAction(new RookCardAction(this, cardInHand.indexOf(tempCard)));
                        //new Card(copyHand.get(pickedCard).getSuit(), copyHand.get(pickedCard).getNumValue());

            }
        }
    }

//    protected void timerTicked()
//    {
//
//    }
}


