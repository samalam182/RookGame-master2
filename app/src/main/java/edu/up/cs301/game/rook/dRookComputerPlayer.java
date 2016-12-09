package edu.up.cs301.game.rook;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.infoMsg.GameInfo;

import static java.lang.reflect.Array.getInt;

/**
 * Contains the algorithms and functions that are included in the dumb computer player
 * for the game of Rook.
 *
 * At any stage of the game including the phases for bidding, interacting with the nest,
 * choosing the trump suit, and placing down cards from their hand into the trick pile,
 * the dumb computer player will make random decisions on their interactions of the cards
 * that are available to them.
 *
 * @author Sam DeWhitt, Eric Hoser, Mitchell Nguyen, Alexander Nowlin
 * @version December 2016
 */
public class dRookComputerPlayer extends RookComputerPlayer {

    /**
     * constructor for the dRookComputerPlayer class
     * @param name
     *      the name of the player
     */
    public dRookComputerPlayer(String name) {
        // creates a computer player whose average reaction time is two seconds
        super(name, 2.0);
    }

    /**
     * called when we receive a message, typically from the local game
     */
    @Override
    protected void receiveInfo(GameInfo info) {
        // if there is no game state, ignore it
        if (!(info instanceof RookState)) {
            return;
        }

        // update the game state
        savedState = (RookState) info;

        // checks to see if it the player's turn
        if (this.playerNum == savedState.getActivePlayer()) {

            // the dumb computer player does not perform any actions during the "WAIT"-stage
            if (savedState.getSubStage() == savedState.WAIT) {
                return;
            }

            // the player will make a random decision during the bidding phase
            else if (savedState.getSubStage() == savedState.BID)
            {
                // the player won't be able to bid after already passing
                if (savedState.pass[this.playerNum])
                {
                    game.sendAction(new RookHoldAction(this));
                }

                Log.i("Reached bidding stage", "" + this.playerNum);

                // the player will randomly decide to bid or pass
                int randSelection = (int) (Math.random() * 10);

                // when randSelection is less than 4, the player will pass
                if (randSelection < 4) {
                    game.sendAction(new RookHoldAction(this));
                    Log.i("Sent hold action", "" + this.playerNum);
                } else {
                    // when randSelection is greater than or equal to 4, the player
                    // will bid a random amount that is either 5 or 10 points above the previous bid
                    int randBidVal = (int) (Math.random() * 10);

                    // variable that keeps track of how much higher
                    // the player will bid
                    int addBid = 0;

                    if (randBidVal <= 7 && savedState.getHighestBid() + 5 <= 120) {
                        // there's a 70% chance that the player will
                        // make a bid 5 points higher than the previous bid (while also
                        // checking to see that their bid doesn't exceed the max value of 120 points)
                        addBid = 5;
                    }
                    else if (randBidVal >= 8 && savedState.getHighestBid() + 10 <= 120) {
                        // there's a 20% chance that the player will
                        // make a bid 10 points higher than the previous bid (while also
                        // checking to see that their bid doesn't exceed the max value of 120 points)
                        addBid = 10;
                    }
                    else
                    {
                        // makes sure that player ultimately either bids or passes
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

                        // send the game the player's bid
                        game.sendAction(new RookBidAction(this, myBid));
                        Log.i("Sent Bid Action", "" + this.playerNum + "," + myBid);
                    }
                }
            }

            // the player will make a random decision on what cards to trade
            // from their own hand with the nest
            else if (savedState.getSubStage() == savedState.NEST) {
                // the player randomly selects 5 cards from
                // their hand to place into the nest (and vice versa)
                ArrayList<Card> handCards = savedState.playerHands[this.playerNum];
                Log.i("Hand cards", "" + this.playerNum);
                ArrayList<Card> nestCards = savedState.nest;
                Log.i("Nest cards", "" + nestCards);

                // gathers information about which cards will be traded between
                // the player's hand and the nest
                ArrayList<Card> cardsFromNest = new ArrayList<Card>();
                ArrayList<Card> cardsFromHand = new ArrayList<Card>();

                for (int y = 0; y < 5; y++) {
                    double randPilePicker = Math.random();

                    if (randPilePicker < 0.5) {

                        // picks from the nest
                        int randNestCardIndex = (int) (Math.random() * 5);

                        // adds the card to the ArrayList if the ArrayList doesn't already contain
                        // the card
                        if (!cardsFromNest.contains(nestCards.get(randNestCardIndex))) {
                            cardsFromNest.add(nestCards.get(randNestCardIndex));
                        }
                    }
                }

                for (int z = 0; z < cardsFromNest.size(); z++) {
                    // picks from the hand
                    int randHandCardIndex = (int) (Math.random() * 9);
                    if (!cardsFromHand.contains(handCards.get(randHandCardIndex))) {
                        // adds the card to the ArrayList if the ArrayList doesn't already contain
                        // the card
                        cardsFromHand.add(handCards.get(randHandCardIndex));
                    } else {
                        // if the card randomly selected is already in the cardsFromHand array,
                        // randomly pick another card
                        int nextTryRandHandCard = (int) (Math.random() * 9);
                        if (!cardsFromHand.contains(handCards.get(nextTryRandHandCard))) ;
                        {
                            cardsFromHand.add(handCards.get(nextTryRandHandCard));
                        }
                    }

                }

                // send the game the list of cards to take from the nest and cards to send from
                // the player's hand
                game.sendAction(new RookNestAction(this, cardsFromNest, cardsFromHand));
                Log.i("Send Nest Action", "" + this.playerNum + "," + cardsFromNest + "," + cardsFromHand);
            }
            else if (savedState.getSubStage() == savedState.TRUMP)
            {

                // the player will randomly choose a trump suit
                double randSuitPick = Math.random() * 4;
                int trumpSuit;

                if (randSuitPick < 1)
                {
                    // trumpSuit is set to black
                    trumpSuit = 0;
                    Log.i("Trump", "Picking BLACK MOON");
                }
                else if (randSuitPick >= 1 && randSuitPick < 2)
                {
                    // trumpSuit is set to yellow
                    trumpSuit = 1;
                    Log.i("Trump", "Picking YELLOW SUN");
                }
                else if (randSuitPick >= 2 && randSuitPick < 3)
                {
                    // trumpSuit is set to green
                    trumpSuit = 2;
                    Log.i("Trump", "Picking GREEN LEAF");
                }
                else
                {
                    // trumpSuit is set to red
                    trumpSuit = 3;
                    Log.i("Trump", "Picking RED HEART");
                }

                // set the trump suit for the round
                savedState.setTrump(trumpSuit);

                // send the game the selected trump suit
                game.sendAction(new RookTrumpAction(this, trumpSuit));

            } else if (savedState.getSubStage() == savedState.PLAY) {
                // the player will randomly choose a card to play


                // create a copy of the player's hand
                ArrayList<Card> copyHand = (ArrayList<Card>)savedState.playerHands[this.playerNum].clone();
                for(Card c : savedState.playerHands[this.playerNum])
                {
                    if(c.getPlayed())
                    {
                        // if the card has been played, remove it from the copy hand so that the
                        // player won't choose it again
                        copyHand.remove(c);
                    }
                }
                int pickedCard = (int)(Math.random()*copyHand.size());

                Card tempCard = copyHand.get(pickedCard);

                // send the game the index of the card the player wants to play
                game.sendAction(new RookCardAction(this, savedState.playerHands[this.playerNum].indexOf(tempCard)));
            }
        }
    }
}


