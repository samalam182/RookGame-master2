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
 * Contains the algorithms and functions that the smart computer player will use for the game
 * of Rook.
 *
 * The smart computer player will determine what to bid, what cards to switch from the nest and the
 * hand, what trump suit to select, and what card to play in the trick based on various algorithms
 * that are defined in each subStage of the game.
 *
 * @author Sam DeWhitt, Eric Hoser, Mitchell Nguyen, Alexander Nowlin
 * @version December 2016
 */
public class sRookComputerPlayer extends RookComputerPlayer {

    // an instance of a Card object, used to determine the suit and number value of a card
    private Card myCard;

    // the highest amount of points that the player will bid
    private int maxBid = 50;

    // whether it is the player's first bid or not
    private boolean firstBid = true;

    // the trump suit for the game
    private int trumpSuit = -1;

    // a copy of the player's hand, used to prevent the player from playing duplicate cards
    private ArrayList<Card> localHandCopy;

    /**
     * Constructor for the sRookComputerPlayer class
     * @param name
     *      the name of the player
     */
    public sRookComputerPlayer(String name) {
        // creates a computer player whose average reaction time is half a second
        super(name, 0.5);
    }

    /**
     * called when we receive a message, typically from the game
     */
    @Override
    protected void receiveInfo(GameInfo info) {
        // if there is no game state, ignore it
        if (!(info instanceof RookState)) {
            return;
        }

        // update the game state
        savedState = (RookState) info;

        // smart computer player makes a move based on what subStage the game is in

        // start the player's bid calculation at 50, which is the minimum bid
        // that players can bid
        int myBid = 50;

        ArrayList<Card> myList;
        ArrayList<Card> nestList;


        // get access to the player's hand and the nest
        myList = savedState.playerHands[this.playerNum];
        nestList = savedState.nest;

        // check to make sure that the player is the one who is allowed to make a move
        if (this.playerNum == savedState.getActivePlayer()) {

            // the player's subStage is set to wait, so do nothing
            if (savedState.getSubStage() == savedState.WAIT)
            {
                return;
            }
            // the player's subStage is set to bid
            else if (savedState.getSubStage() == savedState.BID) {

                if (firstBid) {

                    // it is the player's first bid
                    Log.i("Calculating first bid", "" + firstBid);

                    // create variables that will hold the number of each suit color in the
                    //player's hand

                    int redSuitNum = 0;
                    int yellSuitNum = 0;
                    int greenSuitNum = 0;
                    int blackSuitNum = 0;
                    int rookSuit = 0;


                    for (int j = 0; j < savedState.playerHands[this.playerNum].size(); j++) {
                        Card thisCard = myList.get(j);

                        myCard = new Card(thisCard.getSuit(), thisCard.getNumValue());

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

                        // the smart computer player will determine algorithmically how good its
                        // hand is and will base the bid on this information

                        // determine how many cards in each suit there are, and add 5 to the bid
                        // if there is at least 4 of one suit
                        if (redSuitNum >= 4) {
                            myBid += 5;
                        } else if (greenSuitNum >= 4) {
                            myBid += 5;
                        } else if (yellSuitNum >= 4) {
                            myBid += 5;
                        } else if (blackSuitNum >= 4) {
                            myBid += 5;
                        }

                        // check if the player has the rook card
                        if (rookSuit >= 1) {
                            myBid += 10;
                        }

                        // determine trump suit based on the number of each suit in the
                        // player's hand
                        if (redSuitNum >= greenSuitNum && redSuitNum >= yellSuitNum &&
                                redSuitNum >= blackSuitNum) {
                            // if the player has the most redSuit cards, set trumpSuit to red
                            trumpSuit = 3;
                        } else if (greenSuitNum >= redSuitNum && greenSuitNum >= yellSuitNum &&
                                greenSuitNum >= blackSuitNum) {
                            // if the player has the most greenSuit cards, set trumpSuit to green
                            trumpSuit = 2;
                        } else if (yellSuitNum >= redSuitNum && yellSuitNum >= greenSuitNum &&
                                yellSuitNum >= blackSuitNum) {
                            // if the player has the most yellowSuit cards, set trumpSuit to yellow
                            trumpSuit = 1;
                        } else if (blackSuitNum >= redSuitNum && blackSuitNum >= greenSuitNum &&
                                blackSuitNum >= yellSuitNum) {
                            // if the player has the most blackSuit cards, set trumpSuit to black
                            trumpSuit = 0;
                        }

                        // how many high cards (10-14), add to the bid for every card whose number
                        // value is greater than or equal to 10
                        if (myCard.getNumValue() >= 10) {
                            myBid++;
                        }

                        // how many point cards, add to the bid for each card that has a point value
                        if (myCard.getNumValue() == 5 || myCard.getNumValue() == 10 ||
                                myCard.getNumValue() == 14) {
                            myBid++;
                        } else if (myCard.getNumValue() == 15) {
                            myBid++;
                        }

                        // round the player's bid to nearest 5 points
                        maxBid = myBid / 5;
                        maxBid *= 5;

                        // maximum of 120 point bid
                        if (maxBid > 120) {
                            maxBid = 120;
                        }
                    }

                    firstBid = false;
                }

                // get the previous bid
                int prevBid = savedState.getHighestBid();
                Log.i("Getting a bid request: ", "" + this.playerNum);
                Log.i("Previous bid", "" + prevBid);

                if (maxBid > prevBid) {
                    // if the player's maximum bid is higher than the previous bid, increase
                    // the previous bid by 5 points
                    int thisBid = prevBid + 5;
                    Log.i("Bid action: ", "" + this.playerNum);
                    Log.i("Player's bid: ", "" + thisBid);

                    // send the game the player's bid
                    game.sendAction(new RookBidAction(this, thisBid));
                } else {

                    // maxBid is not greater than the previous bid, so it won't be a valid bid
                    // in this case, send a pass action
                    Log.i("Pass action: ", "" + this.playerNum);
                    game.sendAction(new RookHoldAction(this));
                }
            } else if (savedState.getSubStage() == savedState.NEST) {

                // the smart computer player will select the cards that it wants to place into
                // the nest as well as cards that it will take from the nest; it will try to gain
                // as many of one suit color as possible

                // cardsFromNest will hold the cards that the computer has chosen to add to their
                // hand from the nest
                ArrayList<Card> cardsFromNest = new ArrayList<Card>();

                // cardsFromHand will hold the cards that the computer has chosen to send to the
                // nest from their hand
                ArrayList<Card> cardsFromHand = new ArrayList<Card>();

                // create a copy of the nest ArrayList

                ArrayList<Card> copyNest = (ArrayList<Card>) nestList.clone();

                for (Card c : nestList) {
                    if (c.getPlayed()) {
                        // remove a card from the copyNest, so that when it has been selected,
                        // the computer won't try to select it again
                        copyNest.remove(c);
                    }
                }

                Card myNestCard;

                Log.i("Nest action: player", "" + this.playerNum);
                logCardArray("Starting Hand", myList);
                logCardArray("Staring Nest", nestList);

                for (int i = 0; i < copyNest.size(); i++) {
                    // look at the cards in the nest, so that the player can determine whether to
                    // add them to their hand or not
                    Card thisNestCard = copyNest.get(i);

                    // search the nest for any trump cards, add them to the cardsFromNest
                    if (thisNestCard.getSuit() == trumpSuit) {
                        cardsFromNest.add(thisNestCard);
                    }

                    // add rook
                    if (thisNestCard.getSuit() == 4) {
                        cardsFromNest.add(thisNestCard);
                    }
                }

                // create a copy of the player's current hand
                ArrayList<Card> myHandCard = savedState.playerHands[this.playerNum];
                ArrayList<Card> copyHand = (ArrayList<Card>) myHandCard.clone();

                for (Card c : myHandCard) {
                    if (c.getPlayed()) {
                        // remove a card from the copyHand, so that the computer player won't try
                        // to select it again
                        copyHand.remove(c);
                    }
                }

                // we only want to return to the nest the number of cards that we took from it
                int cardsToReturnToNest = cardsFromNest.size();

                for (int j = 0; cardsToReturnToNest > 0 && j < copyHand.size(); j++) {
                    Card thisCard = copyHand.get(j);

                    if (thisCard.getSuit() != trumpSuit) {
                        // card is not a trump card, so it can be sent to the nest
                        cardsFromHand.add(thisCard);

                        cardsToReturnToNest--;
                    }
                }

                logCardArray("CardsFromNest", cardsFromNest);
                logCardArray("CardsFromHand", cardsFromHand);

                game.sendAction(new RookNestAction(this, cardsFromNest, cardsFromHand));
            }
            else if (savedState.getSubStage() == savedState.TRUMP) {

                // return the trump suit we calculated earlier
                game.sendAction(new RookTrumpAction(this, trumpSuit));

            } else if (savedState.getSubStage() == savedState.PLAY) {

                // the smart computer player will algorithmically decide what card it should play to
                // the trick, with the use of helper methods

                int suitLed = -1;
                int valueTaking = 0;
                int pointsInTrick = 0;
                boolean suitLedIsTrump = false;

                // create a copy of the player's current hand
                localHandCopy = (ArrayList<Card>) savedState.playerHands[this.playerNum].clone();

                for (Card c : savedState.playerHands[this.playerNum]) {
                    if (c.getPlayed()) {
                        // remove a card from the player's hand once it has been played,
                        // so they won't try to play it again
                        localHandCopy.remove(c);
                    }
                }

                for (int i = 0; i < savedState.currTrick.size(); i++) {

                    // look at the cards in the current trick
                    Card newCard = savedState.currTrick.get(i);


                    // set suit, value, and points based on the card in the trick
                    int suit = newCard.getSuit();
                    int value = newCard.getNumValue();
                    int points = newCard.getCounterValue();

                    if (i == 0) {
                        // this is the card that leads the trick

                        // change the suitLed, valueTaking, and pointsInTrick based on the card that
                        // has been played
                        suitLed = suit;
                        valueTaking = value;
                        pointsInTrick = points;

                        //check if the suitLed was the trumpSuit, and if so, set suitLedIsTrump to
                        // true
                        if (suitLed == savedState.getTrump()) {
                            suitLedIsTrump = true;
                        }
                        // also, if the suitLed was the Rook, suitLedIsTrump is set to true
                        else if (suitLed == 4)
                        {
                            suitLedIsTrump = true;
                        }
                    } else {
                        // this is not the card that leads the trick

                        if (suit == suitLed && value > valueTaking) {
                            // if the card played matches the suit led, and is a higher value than
                            // the one that is currently taking the trick, make that card be the
                            // one that's taking the trick
                            valueTaking = value;
                        }

                        // if the card is a point card, add the number of points it has to the total
                        // point value of the trick
                        pointsInTrick += points;
                    }
                }
                Log.i("suitLed= ", "" + suitLed);
                Log.i("valueTaking= ", "" + valueTaking);
                Log.i("pointsInTrick= ", "" + pointsInTrick);
                Log.i("suitLedIsTrump= ", "" + suitLedIsTrump);

                for (int i = 0; i < localHandCopy.size(); i++) {
                    Card playerCard = savedState.playerHands[this.playerNum].get(i);
                    System.out.println("Alex_hand, " + "" + this.playerNum + ", " + "" +
                            playerCard.getSuit() + ", " + "" + playerCard.getNumValue() + ", " + ""
                            + playerCard.getPlayed());

                }


                int winIndex = -1;
                int loseIndex = -1;
                int playIndex = -1;

                if (suitLed != -1) {
                    // this is the case that there are already cards in the trick

                    // if the player has a card that is a higher value than the one currently taking
                    // the trick, set winIndex to that card's index
                    winIndex = checkForHigherValueCard(suitLed, valueTaking, true);

                    // if the player can't win the trick, get a card index using checkForLowestCard
                    loseIndex = checkForLowestCard(suitLed);

                    // set playIndex to either winIndex or loseIndex
                    if (pointsInTrick != 0 && winIndex != -1) {
                        playIndex = winIndex;
                    } else {
                        playIndex = loseIndex;
                    }

                    if (playIndex == -1) {
                        // if the player doesn't have a card to play for the suit led, check the
                        // other suits for the lowest value card that can be played
                        for (int suitIndex = 0; suitIndex < 4; suitIndex++) {
                            if (suitIndex != savedState.getTrump()) {
                                playIndex = checkForLowestCard(suitIndex);
                                if (playIndex != -1) {
                                    break;
                                }
                            }
                        }

                        if (playIndex == -1) {
                            // if the player still can't play a card, check for a low value card in
                            // the trump suit
                            playIndex = checkForLowestCard(savedState.getTrump());
                        }

                        if (playIndex == -1) {

                            // there is no card to play
                            System.out.println("Error. There is no card to play.");
                            // at least return a valid card
                            playIndex=0;
                        }
                    }
                    Card playedCard = savedState.playerHands[this.playerNum].get(playIndex);
                    playedCard.setPlayed();
                    System.out.println("Alex_playedCard1, "+""+playedCard.getSuit()+","+""+
                            playedCard.getNumValue());

                    Card tempCard = localHandCopy.get(playIndex);

                    // send the game the index of the card the player wants to play
                    game.sendAction(new RookCardAction(this, savedState.playerHands[this.playerNum].indexOf(tempCard)));
                } else {
                    // there are no cards in the trick

                    playIndex = checkForHighestValueCardInASuit(savedState.getTrump());
                    if (playIndex == -1) {
                        // check all the suits for the highest value card that can be played
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
                        // there was no card that could be played
                        System.out.println("Error. There is no card to play.");

                        // at least return a valid card
                        playIndex = 0;
                    }
                    Card playedCard = savedState.playerHands[this.playerNum].get(playIndex);
                    playedCard.setPlayed();
                    System.out.println("Alex_playedCard2, " + "" + playedCard.getSuit() + "," + "" + playedCard.getNumValue());

                    Card tempCard = localHandCopy.get(playIndex);
                    // send the game the index of the card the player wants to play
                    game.sendAction(new RookCardAction(this, savedState.playerHands[this.playerNum].indexOf(tempCard)));
                }
            }
        }
    }

    /**
     * A function that checks whether the player has a card that is of a higher value than the card
     * that is currently winning the trick. Determines what card the player should play whether they
     * want to collect points or not.
     *
     *@param suit
     *      the suit that is led in the trick
     * @param value
     *      the value of the card that is the highest value in the trick
     * @param wantPoints
     *      does the player want to collect points
     * @return minimumValueIndex
     *      the index of the card that can be played
     */
    public int checkForHigherValueCard(int suit, int value, boolean wantPoints) {
        int minimumValue = 20;
        int minimumValueIndex = -1;

        for (int j = 0; j < localHandCopy.size(); j++) {
            Card cardToCheck = localHandCopy.get(j);

            // check to see if the card's suit matches the led suit, and is of a higher value than
            // the card that is currently taking the trick
            if (cardToCheck.getSuit() == suit && cardToCheck.getNumValue() > value) {
                if (wantPoints) {
                    // the player wants to get points
                    if (cardToCheck.getCounterValue() > 0) {
                        // the card is worth points
                        if (cardToCheck.getNumValue() < minimumValue) {
                            // the player's card is less than the minimum value, so set the minimum
                            // value to the value of the player's card and return the index of the
                            //card
                            minimumValue = cardToCheck.getNumValue();
                            minimumValueIndex = j;
                        }
                    }
                } else {
                    // the player doesn't want points
                    if (cardToCheck.getCounterValue() == 0) {
                        // the card is not worth any points
                        if (cardToCheck.getNumValue() < minimumValue) {
                            // the player's card is less than the minimum value, so set the minimum
                            // value to the value of the player's card and return the index of the
                            //card
                            minimumValue = cardToCheck.getNumValue();
                            minimumValueIndex = j;
                        }
                    }
                }
            }
        }
        if (wantPoints && minimumValueIndex == -1) {
            // if the player wants points, but they are unable to play a card that is higher than
            // the current highest value card, then recall the method without wanting points
            minimumValueIndex = checkForHigherValueCard(suit, value, false);
        }

        if (minimumValueIndex == -1 && savedState.getTrump() == suit) {
            // if the player is unable to play a card, and the suit led is trump, check for the Rook
            // card
            minimumValueIndex = haveRookCard();
        }
        return minimumValueIndex;
    }

    /**
     * A function that checks the player's hand to see if they have the Rook card.
     *
     * @return k
     *       the index of the Rook card, if the player has it
     */
    public int haveRookCard() {

        for (int k = 0; k < localHandCopy.size(); k++) {
            Card rookCheckCard = localHandCopy.get(k);

            if (rookCheckCard.getSuit() == 4) {
                return k;
            }
        }

        return -1;
    }

    /**
     * A function that checks for the lowest card in a suit in the player's hand. This function is
     * used to help the computer player determine what card to play in the trick.
     *
     * @param suit
     *      the suit that is led in the trick
     * @return minimumValueIndex
     *      the index of the card that can be played
     */
    public int checkForLowestCard(int suit) {
        int minimumValue = 20;
        int minimumValueIndex = -1;

        for (int j = 0; j < localHandCopy.size(); j++) {
            Card cardToCheck = localHandCopy.get(j);

            if (cardToCheck.getSuit() == suit) {
                // if the card matches the suit that is led in the trick, check to see if its value
                // is less than the minimum value
                if (cardToCheck.getNumValue() < minimumValue) {
                    minimumValue = cardToCheck.getNumValue();
                    minimumValueIndex = j;
                }
            }
        }

        if (minimumValueIndex == -1 && savedState.getTrump() == suit) {
            // if the player is unable to play a card, and the suit led is trump, check for the Rook
            // card
            minimumValueIndex = haveRookCard();
        }
        return minimumValueIndex;
    }

    /**
     * A function that checks for the highest value card of a suit in their hand. This function is
     * used to help the computer player determine what card to play in the trick.
     *
     * @param suit
     *      the suit that is led in the trick
     * @return index
     *      the index of the card
     */
    public int checkForHighestValueCardInASuit(int suit) {
        for (int i = 14; i >= 4; i--) {
            int index = checkForHigherValueCard(suit, i, false);

            if (index != -1) {
                return index;
            }
        }
        return -1;
    }

    /**
     * A function that is used for log purposes. It displays the card's suit, and the number value
     * of that card
     *
     * @param card
     *      the card object
     */
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

    /**
     *
     * A function that is used for log statements. The function will output the player's name, and
     * a cardArray, such as that player's hand.
     * @param Name
     *      the name of the player
     * @param cardArray
     *      the size of the ArrayList being accessed
     */
    public void logCardArray(String Name, ArrayList<Card> cardArray) {
        Log.i("", "" + Name);

        for (int i = 0; i < cardArray.size(); i++) {
            logCard(cardArray.get(i));
        }
    }
}

