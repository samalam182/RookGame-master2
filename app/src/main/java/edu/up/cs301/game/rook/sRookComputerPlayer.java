package edu.up.cs301.game.rook;

import android.graphics.Color;
import android.util.Log;

import java.text.Bidi;
import java.util.ArrayList;
import static java.lang.reflect.Array.getInt;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.ProxyGame;
import edu.up.cs301.game.infoMsg.GameInfo;


/**
 * Created by hoser18 on 11/8/2016.
 */
public class sRookComputerPlayer extends RookComputerPlayer {

    private Card myCard;
    private int maxBid = 50;
    private boolean firstBid = true;
    private int trumpSuit = -1;
    private ArrayList<Card> localHandCopy;

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
        ArrayList<Card> nestList;

        myList = savedState.playerHands[this.playerNum];
        nestList = savedState.nest;

        //savedState.setSubStage(1);

//            ArrayList<Card> thisList = savedState.playerHands[this.playerNum];
//
//            myList = thisList;

        //Card myCard;
        if (this.playerNum == savedState.getActivePlayer()) {

            if (savedState.getSubStage() == savedState.WAIT)
            {
                return;
            }
            else if (savedState.getSubStage() == savedState.BID) {

                if (firstBid) {

                    Log.i("Calculating first bid", "" + firstBid);
                    int redSuitNum = 0;
                    int yellSuitNum = 0;
                    int greenSuitNum = 0;
                    int blackSuitNum = 0;
                    int rookSuit = 0;


                    for (int j = 0; j < savedState.playerHands[this.playerNum].size(); j++) {
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
                            myBid += 5;
                        } else if (greenSuitNum >= 4) {
                            myBid += 5;
                        } else if (yellSuitNum >= 4) {
                            myBid += 5;
                        } else if (blackSuitNum >= 4) {
                            myBid += 5;
                        }

                        if (rookSuit >= 1) {
                            myBid += 10;
                        }

                        // determine trump suit we want

                        if (redSuitNum >= greenSuitNum && redSuitNum >= yellSuitNum && redSuitNum >= blackSuitNum) {
                            trumpSuit = 3;
                        } else if (greenSuitNum >= redSuitNum && greenSuitNum >= yellSuitNum && greenSuitNum >= blackSuitNum) {
                            trumpSuit = 2;
                        } else if (yellSuitNum >= redSuitNum && yellSuitNum >= greenSuitNum && yellSuitNum >= blackSuitNum) {
                            trumpSuit = 1;
                        } else if (blackSuitNum >= redSuitNum && blackSuitNum >= greenSuitNum && blackSuitNum >= yellSuitNum) {
                            trumpSuit = 0;
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

                        // round to nearest 5 points

                        maxBid = myBid / 5;
                        maxBid *= 5;

                        // maximum of 120 point bid

                        if (maxBid > 120) {
                            maxBid = 120;
                        }
                    }

                    firstBid = false;
                }

                //int prevBid = savedState.getHighestBid();
                int prevBid = savedState.getHighestBid();
                Log.i("Getting a bid request: ", "" + this.playerNum);
                Log.i("Previous bid", "" + prevBid);

                if (maxBid > prevBid) {
                    // create a new action
                    int thisBid = prevBid + 5;
                    Log.i("Bid action: ", "" + this.playerNum);
                    Log.i("Player's bid: ", "" + thisBid);
                    game.sendAction(new RookBidAction(this, thisBid));
                } else {
                    Log.i("Pass action: ", "" + this.playerNum);
                    game.sendAction(new RookHoldAction(this));
                }
            } else if (savedState.getSubStage() == savedState.NEST) {
                // the smart computer player will select the cards that it wants to place into the nest
                // it will try to gain as many of one suit color as possible

                ArrayList<Card> cardsFromNest = new ArrayList<Card>();
                ArrayList<Card> cardsFromHand = new ArrayList<Card>();

                ArrayList<Card> nestCardList = savedState.nest;
                ArrayList<Card> copyNest = (ArrayList<Card>) nestCardList.clone();

                for (Card c : nestCardList) {
                    if (c.getPlayed()) {
                        copyNest.remove(c);
                    }
                }

                Card myNestCard;

                Log.i("Nest action: player", "" + this.playerNum);
                logCardArray("Starting Hand", myList);
                logCardArray("Staring Nest", nestList);

                for (int i = 0; i < copyNest.size(); i++) {
                    // need to access the suits in the nest to see if we should add them to the hand
                    Card thisNestCard = copyNest.get(i);

                    //myNestCard = new Card(thisNestCard.getSuit(), thisNestCard.getNumValue());

                    // search the nest for any trump cards, add them to the cardsFromNest
                    if (thisNestCard.getSuit() == trumpSuit) {
                        cardsFromNest.add(thisNestCard);
                    }

                    // add rook

                    if (thisNestCard.getSuit() == 4) {
                        cardsFromNest.add(thisNestCard);
                    }
                }

                ArrayList<Card> myHandCard = savedState.playerHands[this.playerNum];
                ArrayList<Card> copyHand = (ArrayList<Card>) myHandCard.clone();

                for (Card c : myHandCard) {
                    if (c.getPlayed() == true) {
                        copyHand.remove(c);
                    }
                }

                // we only want to return to the nest the number of cards that we took from it
                int cardsToReturnToNest = cardsFromNest.size();

                for (int j = 0; cardsToReturnToNest > 0 && j < copyHand.size(); j++) {
                    Card thisCard = copyHand.get(j);

                    //myHandCard = new Card(thisCard.getSuit(), thisCard.getNumValue());

                    if (thisCard.getSuit() != trumpSuit) {
                        // card is not a trump card
                        cardsFromHand.add(thisCard);

                        cardsToReturnToNest--;
                    }
                }

                logCardArray("CardsFromNest", cardsFromNest);
                logCardArray("CardsFromHand", cardsFromHand);

                game.sendAction(new RookNestAction(this, cardsFromNest, cardsFromHand));
            } else if (savedState.getSubStage() == savedState.TRUMP) {
                // return the trump suit we calculated earlier

                game.sendAction(new RookTrumpAction(this, trumpSuit));
            } else if (savedState.getSubStage() == savedState.PLAY) {
                // smart computer player will play a card, and will try to win each trick
                //needs to not be hard coded in. Placed for convenience


                int suitLed = -1;
                int valueTaking = 0;
                int pointsInTrick = 0;
                boolean suitLedIsTrump = false;

                localHandCopy = (ArrayList<Card>) savedState.playerHands[this.playerNum].clone();

                for (Card c : savedState.playerHands[this.playerNum]) {
                    if (c.getPlayed()) {
                        localHandCopy.remove(c);
                    }
                }

                for (int i = 0; i < savedState.currTrick.size(); i++) {
                    Card newCard = savedState.currTrick.get(i);

                    int suit = newCard.getSuit();
                    int value = newCard.getNumValue();
                    int points = newCard.getCounterValue();

                    if (i == 0) {
                        // this is the card that leads the trick
                        suitLed = suit;
                        valueTaking = value;
                        pointsInTrick = points;
                        if (suitLed == savedState.getTrump()) {
                            suitLedIsTrump = true;
                        } else if (suitLed == 4) // should be able to use savedState.ROOK instead of 4
                        {
                            suitLedIsTrump = true;
                        }
                    } else {
                        if (suit == suitLed && value > valueTaking) {
                            valueTaking = value;
                        }
                        pointsInTrick += points;
                    }
                }
                Log.i("suitLed= ", "" + suitLed);
                Log.i("valueTaking= ", "" + valueTaking);
                Log.i("pointsInTrick= ", "" + pointsInTrick);
                Log.i("suitLedIsTrump= ", "" + suitLedIsTrump);

                for (int i = 0; i < localHandCopy.size(); i++) {
                    Card playerCard = savedState.playerHands[this.playerNum].get(i);
                    System.out.println("Alex_hand, " + "" + this.playerNum + ", " + "" + playerCard.getSuit() + ", " + "" + playerCard.getNumValue() + ", " + "" + playerCard.getPlayed());

                }


                int winIndex = -1;
                int loseIndex = -1;
                int playIndex = -1;

                if (suitLed != -1) {
                    // this is the case that there are already cards in the trick
                    winIndex = checkForHigherValueCard(suitLed, valueTaking, true);
                    loseIndex = checkForLowestCard(suitLed);

                    if (pointsInTrick != 0 && winIndex != -1) {
                        playIndex = winIndex;
                    } else {
                        playIndex = loseIndex;
                    }

                    if (playIndex == -1) {
                        for (int suitIndex = 0; suitIndex < 4; suitIndex++) {
                            if (suitIndex != savedState.getTrump()) {
                                playIndex = checkForLowestCard(suitIndex);
                                if (playIndex != -1) {
                                    break;
                                }
                            }
                        }

                        if (playIndex == -1) {
                            playIndex = checkForLowestCard(savedState.getTrump());
                        }

                        if (playIndex == -1) {
                            System.out.println("Error. There is no card to play low.");
                            // at least return a valid card
                            //playIndex=0;
                        }
                    }
                    //Card playedCard = savedState.playerHands[this.playerNum].get(playIndex);
                    //playedCard.setPlayed();
                    //System.out.println("Alex_playedCard1, "+""+playedCard.getSuit()+","+""+playedCard.getNumValue());

                    Card tempCard = localHandCopy.get(playIndex);
                    //new Card(copyHand.get(pickedCard).getSuit(), copyHand.get(pickedCard).getNumValue());
                    game.sendAction(new RookCardAction(this, savedState.playerHands[this.playerNum].indexOf(tempCard)));
                } else {
                    playIndex = checkForHighestValueCardInASuit(savedState.getTrump());
                    if (playIndex == -1) {
                        for (int suitIndex = 0; suitIndex < 4; suitIndex++) {
                            if (suitIndex != savedState.getTrump()) {
                                playIndex = checkForHighestValueCardInASuit(suitIndex);
                                if (playIndex != -1) {
                                    break;
                                }
                            }
                        }
                    }
                    if (playIndex == -1) {
                        System.out.println("Error. There is no card to play.");
                        // at least return a valid card
                        //playIndex = 0;
                    }
                    Card playedCard = savedState.playerHands[this.playerNum].get(playIndex);
                    playedCard.setPlayed();
                    System.out.println("Alex_playedCard2, " + "" + playedCard.getSuit() + "," + "" + playedCard.getNumValue());

                    Card tempCard = localHandCopy.get(playIndex);
                    //new Card(copyHand.get(pickedCard).getSuit(), copyHand.get(pickedCard).getNumValue());
                    game.sendAction(new RookCardAction(this, savedState.playerHands[this.playerNum].indexOf(tempCard)));
                }
            }
        }
    }

    public int checkForHigherValueCard(int suit, int value, boolean wantPoints) {
        int minimumValue = 20;
        int minimumValueIndex = -1;

        for (int j = 0; j < localHandCopy.size(); j++) {
            Card cardToCheck = localHandCopy.get(j);

            if (cardToCheck.getSuit() == suit && cardToCheck.getNumValue() > value) {
                if (wantPoints) {
                    if (cardToCheck.getCounterValue() > 0) {
                        if (cardToCheck.getNumValue() < minimumValue) {
                            minimumValue = cardToCheck.getNumValue();
                            minimumValueIndex = j;
                        }
                    }

                } else {
                    if (cardToCheck.getCounterValue() == 0) {
                        if (cardToCheck.getNumValue() < minimumValue) {
                            minimumValue = cardToCheck.getNumValue();
                            minimumValueIndex = j;
                        }
                    }
                }
            }
        }
        if (wantPoints && minimumValueIndex == -1) {
            minimumValueIndex = checkForHigherValueCard(suit, value, false);
        }

        if (minimumValueIndex == -1 && savedState.getTrump() == suit) {
            minimumValueIndex = haveRookCard();
        }
        return minimumValueIndex;
    }

    public int haveRookCard() {

        //ArrayList<Card> rookCheckCardClone = (ArrayList<Card>)savedState.playerHands[this.playerNum].clone();
        for (int k = 0; k < localHandCopy.size(); k++) {
            Card rookCheckCard = localHandCopy.get(k);


            if (rookCheckCard.getSuit() == 4) {
                return k;
            }
        }

        return -1;
    }

    public int checkForLowestCard(int suit) {

        //ArrayList<Card> cardToCheckClone = (ArrayList<Card>) savedState.playerHands[this.playerNum].clone();
        int minimumValue = 20;
        int minimumValueIndex = -1;
        for (int j = 0; j < localHandCopy.size(); j++) {
            Card cardToCheck = localHandCopy.get(j);


            if (cardToCheck.getSuit() == suit) {
                if (cardToCheck.getNumValue() < minimumValue) {
                    minimumValue = cardToCheck.getNumValue();
                    minimumValueIndex = j;
                }
            }
        }

        if (minimumValueIndex == -1 && savedState.getTrump() == suit) {
            minimumValueIndex = haveRookCard();
        }
        return minimumValueIndex;
    }

    public int checkForHighestValueCardInASuit(int suit) {
        for (int i = 14; i >= 4; i--) {
            int index = checkForHigherValueCard(suit, i, false);

            if (index != -1) {
                return index;
            }
        }
        return -1;
    }

    public void logCard(Card card) {
        String stringOut;

        switch (card.getSuit()) {
            case 0:
                stringOut = "BLACK";
                break;
            case 1:
                stringOut = "YELLOW";
                break;
            case 2:
                stringOut = "GREEN";
                break;
            case 3:
                stringOut = "RED";
                break;
            case 4:
                stringOut = "ROOK";
                break;
            default:
                stringOut = "ERROR: unknown";
                break;
        }

        Log.i(stringOut, "" + card.getNumValue());
    }

    public void logCardArray(String Name, ArrayList<Card> cardArray) {
        Log.i("", "" + Name);

        for (int i = 0; i < cardArray.size(); i++) {
            logCard(cardArray.get(i));
        }
    }
}

