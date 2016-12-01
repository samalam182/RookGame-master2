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

    private Card myCard;
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

        // update the game state
        savedState = (RookState) info;

        // smart computer player makes a move based on what subStage the game is in


        int myBid = 50;

        ArrayList<Card> myList;
        ArrayList<Card>nestList;

        myList = savedState.playerHands[this.playerNum];
        nestList = savedState.nest;

        //savedState.setSubStage(1);

//            ArrayList<Card> thisList = savedState.playerHands[this.playerNum];
//
//            myList = thisList;

        //Card myCard;

        int redSuitNum = 0;
        int yellSuitNum = 0;
        int greenSuitNum = 0;
        int blackSuitNum = 0;
        int rookSuit = 0;

        for (int j = 0; j < 9; j++) {
            Card thisCard = myList.get(j);

            myCard = new Card(thisCard.getSuit(), thisCard.getNumValue());

            //myCard = arrayCard;

            if (myCard.getSuit() == 3) {
                // the card is a red suit
                redSuitNum++;
            } else if (myCard.getSuit() == 1) {
                // the card is a yellow suit
                yellSuitNum++;
            } else if (myCard.getSuit() == 2) {
                // the Card is a green suit
                greenSuitNum++;
            } else if (myCard.getSuit() == 0) {
                // the card is a Black suit
                blackSuitNum++;
            } else {
                // the card is the Rook
                rookSuit++;
            }

            // the smart computer player will determine, algorithmically, how good its hand is
            // and will base the bid on this information

            // how many cards in each suit

            if (redSuitNum >= 4) {
                myBid++;
            } else if (greenSuitNum >= 4) {
                myBid++;
            } else if (yellSuitNum >= 4) {
                myBid++;
            } else if (blackSuitNum >= 4) {
                myBid++;
            }

            if (rookSuit == 1) {
                myBid++;
            }

            // how many high cards (10-14)

            if (myCard.getNumValue() >= 10) {
                myBid++;
            }

            // how many point cards

            if (myCard.getNumValue() == 5 || myCard.getNumValue() == 10 || myCard.getNumValue() == 14) {
                myBid++;
            } else if (myCard.getNumValue() == 15) {
                myBid++;
            }
        }
        if (savedState.getSubStage() == savedState.BID) {
            //int prevBid = savedState.getHighestBid();
            int prevBid = savedState.getHighestBid();

            if (myBid > prevBid && myBid % 5 == 0) {
                // create a new action
                game.sendAction(new RookBidAction(this, myBid));
            }
            else if (myBid > prevBid && myBid % 5 != 0) {
                int myNewBid = myBid - (myBid % 5);
                game.sendAction(new RookBidAction(this, myNewBid));
            }
            else {
                game.sendAction(new RookHoldAction(this));
            }
        }
        else if (savedState.getSubStage() == savedState.NEST) {
            // the smart computer player will select the cards that it wants to place into the nest
            // it will try to gain as many of one suit color as possible

            ArrayList<Card> cardsFromNest = new ArrayList<Card>();
            ArrayList<Card> cardsFromHand = new ArrayList<Card>();

            Card myNestCard;

            for (int i = 0; i < nestList.size(); i++) {
                // need to access the suits in the nest to see if we should add them to the hand
                Card thisNestCard = nestList.get(i);

                myNestCard = new Card(thisNestCard.getSuit(), thisNestCard.getNumValue());

                if (redSuitNum > greenSuitNum && redSuitNum > yellSuitNum && redSuitNum > blackSuitNum) {
                    // search the nest for any red cards, add them to the cardsFromNest
                    if (myNestCard.getSuit() == 3) {
                        cardsFromNest.add(myNestCard);
                    }
                } else if (greenSuitNum > redSuitNum && greenSuitNum > yellSuitNum && greenSuitNum > blackSuitNum) {
                    // search the nest for any green cards, add them to the cardsFromNest
                    if (myNestCard.getSuit() == 2) {
                        cardsFromNest.add(myNestCard);
                    }
                } else if (yellSuitNum > redSuitNum && yellSuitNum > greenSuitNum && yellSuitNum > blackSuitNum) {
                    // search the nest for any yellow cards, add them to the cardsFromNest
                    if (myNestCard.getSuit() == 1) {
                        cardsFromNest.add(myNestCard);
                    }
                } else if (blackSuitNum > redSuitNum && blackSuitNum > greenSuitNum && blackSuitNum > yellSuitNum) {
                    // search the nest for any black cards, add them to the cardsFromNest
                    if (myNestCard.getSuit() == 0) {
                        cardsFromNest.add(myNestCard);
                    }
                }

                if (myNestCard.getSuit() == 4)
                {
                    cardsFromNest.add(myNestCard);
                }
            }

            Card myHandCard;

            for (int j = 0; j < savedState.playerHands[this.playerNum].size(); j++)
            {
                Card thisCard = myList.get(j);

                myHandCard = new Card(thisCard.getSuit(), thisCard.getNumValue());

                for (int h = 0; h < cardsFromNest.size(); h++) {



                    if (redSuitNum > greenSuitNum && redSuitNum > yellSuitNum && redSuitNum > blackSuitNum) {
                        if (myCard.getSuit() != 3) {
                            // card is not a red suit
                            if (!cardsFromHand.contains(myHandCard)) {
                                cardsFromHand.add(myHandCard);
                            }
                        }
                    } else if (greenSuitNum > redSuitNum && greenSuitNum > yellSuitNum && greenSuitNum > blackSuitNum) {
                        if (myHandCard.getSuit() != 2) {
                            // card is not a green suit
                            if (!cardsFromHand.contains(myHandCard)) {
                                cardsFromHand.add(myHandCard);
                            }
                        }
                    } else if (yellSuitNum > redSuitNum && yellSuitNum > greenSuitNum && yellSuitNum > blackSuitNum) {
                        if (myHandCard.getSuit() != 1) {
                            // card is not a yellow suit
                            if (!cardsFromHand.contains(myHandCard)) {
                                cardsFromHand.add(myHandCard);
                            }
                        }
                    } else if (blackSuitNum > redSuitNum && blackSuitNum > greenSuitNum && blackSuitNum > yellSuitNum) {
                        if (myHandCard.getSuit() != 0) {
                            // card is not a black suit
                            if (!cardsFromHand.contains(myHandCard)) {
                                cardsFromHand.add(myHandCard);
                            }
                        }
                    }
                }
            }

            game.sendAction(new RookNestAction(this, cardsFromNest, cardsFromHand));
        }
        else if (savedState.getSubStage() == savedState.TRUMP)
        {
            // the smart computer player will select the trump suit, based on the number of cards
            // it has for each suit; the one it has the most of will be the trump suit

            int trumpSuit=0;

            if (redSuitNum >= 3 && greenSuitNum <= 3 && blackSuitNum <= 3 && yellSuitNum <= 3)
            {
                // has the most of red cards, so trump suit is red
                trumpSuit = Color.RED;
            }
            else if (greenSuitNum >= 3 && redSuitNum <= 3 && blackSuitNum <= 3 && yellSuitNum <= 3)
            {
                // has the most of green cards, so trump suit is green
                trumpSuit = Color.GREEN;
            }
            else if (yellSuitNum >= 3 && redSuitNum <= 3 && greenSuitNum <= 3 && blackSuitNum <= 3)
            {
                // has the most of yellow cards, so trump suit is yellow
                trumpSuit = Color.YELLOW;
            }
            else if (blackSuitNum >= 3 && redSuitNum <= 3 && greenSuitNum <= 3 && yellSuitNum <= 3)
            {
                // has the most of black cards, so trump suit is black
                trumpSuit = Color.BLACK;
            }
            game.sendAction(new RookTrumpAction(this, trumpSuit));
        }
        else if (savedState.getSubStage() == savedState.PLAY)
        {
            // smart computer player will play a card, and will try to win each trick
            //needs to not be hard coded in. Placed for convenience
            game.sendAction(new RookCardAction(this, 0));
        }
    }
}
