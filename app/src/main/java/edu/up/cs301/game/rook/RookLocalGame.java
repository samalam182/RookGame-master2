package edu.up.cs301.game.rook;

import android.util.Log;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.rook.*;


/**
 * A class that knows how to play the game of Rook. The data in this class represents the
 * state of the current game, which keeps track of which player's turn it is during
 * any stage of the game as well as defining what a legal move is.
 *
 * The state represented by an instance of this class can be a
 * complete state (as might be used by the main game activity) or a partial
 * state as it would be seen from the perspective of a Human or Computer Player.
 *
 * @author Sam DeWhitt, Eric Hoser, Mitchell Nguyen, Alexander Nowlin
 * @version December 2016
 */
public class RookLocalGame extends LocalGame
{
    // the game's state
    public RookState state;

    // the different stages of the game
    public final int WAIT = 0;
    public final int BID = 1;
    public final int TRUMP = 2;
    public final int NEST = 3;
    public final int PLAY = 4;

    // will keep track of how many cards have placed down from any player
    // during the trick-phase.
    // once this value reaches 36 (after 9 full tricks), a new round is started automatically
    public int gameCounter = 0;

    /**
     *
     * Constructor
     * Initially sets the stage to "WAIT" so that the game can be properly initialized.
     *
     */
    public RookLocalGame()
    {
        Log.i("RookLocalGame", "Local Game being created");

        state = new RookState();
        state.setSubStage(WAIT);
    }

    @Override
    /**
     *
     * Sends the updated state to the given player.
     * Nulls out any hidden information that the player shouldn't know.
     *
     */
    protected void sendUpdatedStateTo(GamePlayer p)
    {
        // if there is no state, then the game doesn't update
        if (state == null)
        {
            return;
        }

        // creates a Rook-state that will contain only the player's
        // information that they should know
        RookState editedState = new RookState(state);
        p.sendInfo(editedState);
    }

    /**
     *
     * If it's the active player's turn, then that player can make a move.
     * This method is used during all stages of the game of Rook, including
     * the bidding, nest, trump, and trick phases.
     *
     */
    protected boolean canMove(int playerIdx)
    {
        // only playerIdx of 0-3 are value numbers that represent the 4 players of the game
        if (playerIdx < 0 || playerIdx > 3)
        {
            return false;
        }
        else
        {
            // return true if the currently active player
            // matches with the checked, given playerIdx
            return state.getActivePlayer() == playerIdx;
        }
    }

    /**
     *
     * Checks to see if the game is over by checking if any player has 200 or more points.
     * If no player has reached at least 200 total points, then the game initiates the
     * start of a new round (as opposed to ending the game).
     *
     */
    protected String checkIfGameOver()
    {
        // check if any player has won if they have reached at least 200 points
        if (state.getScore(0) >= 200)
        {
            return "Player 1 is the winner!";
        }

        if (state.getScore(1) >= 200)
        {
            return "Player 2 is the winner!";
        }

        if (state.getScore(2) >= 200)
        {
            return "Player 3 is the winner!";
        }

        if (state.getScore(3) >= 200)
        {
            return "Player 4 is the winner!";
        }

        // if no player has reached 200 total points, do not display any String
        return null;
    }

    /**
     *
     * Checks to make sure that every action made by any player is a legal move.
     * This method is called during any stage of the game, including the bidding,
     * nest, trump, and trick phases.
     * @param action  The move that the player has sent to the game
     *
     */
    protected boolean makeMove(GameAction action)
    {
        // makes sure that the first stage of the game that the user can interact with
        // is during the bidding phase
        if(state.getSubStage() == WAIT)
        {
            state.setSubStage(BID);
        }

        // variable that represents the active player's index
        int playerIdx = state.getActivePlayer();

        // checks if the given game-action is one of the types of RookAction;
        // if not, then the given action isn't an action that we want
        if (!(action instanceof RookBidAction || action instanceof RookCardAction ||
                action instanceof RookHoldAction || action instanceof RookNestAction ||
                action instanceof RookTrumpAction))
        {
            return false;
        }

        // checks if the given action is a bid-action
        if (action instanceof RookBidAction)
        {
            try {
                // makes sure that Android system / networking has time
                // to process the action before moving onto the next step
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // make sure that the bid-action happens during the bidding phase
            if(state.getSubStage() == BID)
            {
                // gather information about the amount of points the current player has bid,
                // and set the given bid amount to the player's current bid amount
                RookBidAction act = (RookBidAction) action;
                int playBid = act.getBid();
                state.setBid(playBid, playerIdx);

                // sets the latest bidder as the player who sent the bid-action,
                // which will be displayed on the GUI
                state.lastBidder = playerIdx;

                // once the winning bidder has been decided (after 3 players have passed or
                // the last bidder has made a maximum bid amount of 120 points), set the
                // next stage of the game after the bid-phase as the nest-phase
                if (state.finalizeBids())
                {
                    // sets the winning bidder as the player to firstly place down a card
                    // during the first trick of that round
                    state.currTrickWinner = state.winningPlayer;

                    // set the stage to the nest-phase after the bid-phase
                    state.setSubStage(NEST);
                    return true;
                }
                else
                {
                    // if there is no winning bidder that has been decided, then
                    // continue rotating in a clockwise-direction to allow players
                    // to either bid or pass
                    state.setPlayer();
                    return true;
                }
            }
        }

        // checks if the given action is a hold-action
        // (when the player has a pass during the bidding-phase
        else if (action instanceof RookHoldAction)
        {
            // make sure that the action was sent during the bidding stage
            if(state.getSubStage() == BID)
            {
                try {
                    // makes sure that Android system / networking has time
                    // to process the action before moving onto the next step
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // set the player's status during the bidding phase as "Hold", where they
                // have to wait until the bidding phase is over
                state.setHold(playerIdx);

                // make sure that the player's status is set to "pass" when they decide
                // to make a pass during the bidding phase
                state.pass[playerIdx] = true;

                // once the winning bidder has been decided (after 3 players have passed or
                // the last bidder has made a maximum bid amount of 120 points), set the
                // next stage of the game after the bid-phase as the nest-phase
                if (state.finalizeBids())
                {
                    // sets the winning bidder as the player to firstly place down a card
                    // during the first trick of that round
                    state.setPlayer(state.winningPlayer);
                    state.currTrickWinner = state.winningPlayer;

                    // set the next stage to the nest-phase after the bid-phase
                    state.setSubStage(NEST);
                    return true;
                }

                // if there is no winning bidder that has been decided, then
                // continue rotating in a clockwise-direction to allow players
                // to either bid or pass
                state.setPlayer();
            }
        }

        // checks to see if the given action is a nest-action
        else if (action instanceof RookNestAction)
        {
            // checks if the given action occurs during the time when the winning bidder
            // gets to interact with the nest
            if(state.getSubStage() == NEST && playerIdx == state.winningPlayer)
            {
                // make a proper trade between the player's hand and nest if the
                // same number of cards that are chosen from the nest equals the number
                // of cards chosen to donate from the player's hand
                RookNestAction act = (RookNestAction) action;
                state.useNest(act.getNest(), act.getHand(), state.playerHands[playerIdx]);

                // set the next stage to the trump-phase after the nest-phase
                state.setSubStage(TRUMP);
            }
        }

        // checks to see if the given action is a trump-action
        else if (action instanceof RookTrumpAction)
        {
            // makes sure if the trump-action happens during the time when the winning bidder
            // gets to choose the trump suit for that round
            if (state.getSubStage() == TRUMP && playerIdx == state.winningPlayer) {
                // sets the trump suit of the current round as the suit chosen by the
                // winner through their trump-action
                RookTrumpAction act = (RookTrumpAction) action;
                state.setTrump(act.getTrumpColor());

                // make sure that the winning bidder is the first player to place down
                // a card during the first trick of the round
                state.currTrickWinner = state.winningPlayer;

                // set the next stage to the play-phase after the trump-phase
                state.setSubStage(PLAY);
            }
        }

        // checks if the given action is a card-action (when the player can place
        // down cards in the trick)
        else if (action instanceof RookCardAction)
        {
            try {
                // makes sure that Android system / networking has time
                // to process the action before moving onto the next step
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // increase the gameCounter  each time any player places down
            // a card from their hand into the trick
            gameCounter++;

            // variable keeps track of the index of the player who wins a certain trick
            int trickWinner = 0;

            // variable that gathers the amount of points that are included in a trick
            // based on the counter-values of the Counter-cards placed into the trick
            int points = 0;

            // checks if the given card-action happens during the play-stage when players
            // can place down cards from their hand into the trick
            if(state.getSubStage() == PLAY)
            {
                // once the trick reaches its max value of 4 cards from each player's hands,
                // clear out all the cards included in the trick to start a new trick
                if(state.currTrick.size() == 4)
                {
                    state.currTrick.clear();
                }

                // gather information about which specific card (including data about that card's
                // suit and number value) that the player has sent to the local-game
                RookCardAction act = (RookCardAction) action;
                int handIdx = act.retButtonNum();

                // check to see if the trick size is less than the max value of 4 cards
                if(state.currTrick.size() < 4)
                {
                    // add the chosen card from the player's hand into the trick
                    state.currTrick.add(state.playerHands[state.getActivePlayer()].get(handIdx));

                    // set the status of the card that was placed into the trick from the
                    // player's hand as "already-played" such that the GUI will make the
                    // ImageButton related to that particular card as invisible
                    state.playerHands[state.getActivePlayer()].get(handIdx).setPlayed();
                }

                // once the trick reaches a max value of 4 cards (after the last player of the
                // trick places down their card into the trick), account for all the counter-points
                // that are included in the current trick
                if(state.currTrick.size() == 4)
                {
                    // gather information about how many counter-points are
                    // included in the current trick
                    points = state.countTrick();

                    // gather information about the suit of the first card placed into
                    // the trick as well as the trump-suit for the current round
                    Card first = state.currTrick.get(0);
                    int firstCardSuit = first.getSuit();
                    int currTrump = state.getTrump();

                    // variable that represents how one card has priority over another card
                    // that would cause the player who placed down that card from their hand
                    // to ultimately win that trick
                    int ranking = 1000;

                    // keeps track of which cards were placed down in which order according
                    // to the index of the player who placed down that card into the trick
                    int[] trickOrder = {0,1,2,3};

                    // reorder the order of players who placed down cards based on
                    // which player was the one who first placed down a card into the trick
                    if (state.currTrickWinner == 0)
                    {
                        trickOrder[0] = 0;
                        trickOrder[1] = 1;
                        trickOrder[2] = 2;
                        trickOrder[3] = 3;
                    }
                    else if (state.currTrickWinner == 1)
                    {
                        trickOrder[0] = 1;
                        trickOrder[1] = 2;
                        trickOrder[2] = 3;
                        trickOrder[3] = 0;
                    }
                    else if (state.currTrickWinner == 2)
                    {
                        trickOrder[0] = 2;
                        trickOrder[1] = 3;
                        trickOrder[2] = 0;
                        trickOrder[3] = 1;
                    }
                    else if (state.currTrickWinner == 3)
                    {
                        trickOrder[0] = 3;
                        trickOrder[1] = 0;
                        trickOrder[2] = 1;
                        trickOrder[3] = 2;
                    }

                    for (int f = 0; f < state.currTrick.size(); f++)
                    {
                        // the player who placed down the Rook card automatically wins that trick
                        if (state.currTrick.get(f).getNumValue() == 15)
                        {
                            trickWinner = trickOrder[f];
                            ranking = 1;
                        }

                        // the player who placed down a card of the trump suit with a number value
                        // of 14 will be deemed as the next highest "ranking" after the Rook card
                        else if (state.currTrick.get(f).getSuit() == currTrump && state.currTrick.get(f).getNumValue() == 14)
                        {
                            if (ranking > 1)
                            {
                                trickWinner = trickOrder[f];
                                ranking = 2;
                            }
                        }

                        // the player who placed down a card of the trump suit with a number value
                        // of 13 will be deemed as the next highest "ranking" after the card of the
                        // trump suit with a number value of 14
                        else if (state.currTrick.get(f).getSuit() == currTrump && state.currTrick.get(f).getNumValue() == 13)
                        {
                            if (ranking > 2)
                            {
                                trickWinner = trickOrder[f];
                                ranking = 3;
                            }
                        }

                        // the player who placed down a card of the trump suit with a number value
                        // of 12 will be deemed as the next highest "ranking" after the card of the
                        // trump suit with a number value of 13
                        else if (state.currTrick.get(f).getSuit() == currTrump && state.currTrick.get(f).getNumValue() == 12)
                        {
                            if (ranking > 3)
                            {
                                trickWinner = trickOrder[f];
                                ranking = 4;
                            }
                        }

                        // the player who placed down a card of the trump suit with a number value
                        // of 11 will be deemed as the next highest "ranking" after the card of the
                        // trump suit with a number value of 12
                        else if (state.currTrick.get(f).getSuit() == currTrump && state.currTrick.get(f).getNumValue() == 11)
                        {
                            if (ranking > 4)
                            {
                                trickWinner = trickOrder[f];
                                ranking = 5;
                            }
                        }

                        // the player who placed down a card of the trump suit with a number value
                        // of 10 will be deemed as the next highest "ranking" after the card of the
                        // trump suit with a number value of 11
                        else if (state.currTrick.get(f).getSuit() == currTrump && state.currTrick.get(f).getNumValue() == 10)
                        {
                            if (ranking > 5)
                            {
                                trickWinner = trickOrder[f];
                                ranking = 6;
                            }
                        }

                        // the player who placed down a card of the trump suit with a number value
                        // of 9 will be deemed as the next highest "ranking" after the card of the
                        // trump suit with a number value of 10
                        else if (state.currTrick.get(f).getSuit() == currTrump && state.currTrick.get(f).getNumValue() == 9)
                        {
                            if (ranking > 6)
                            {
                                trickWinner = trickOrder[f];
                                ranking = 7;
                            }
                        }

                        // the player who placed down a card of the trump suit with a number value
                        // of 8 will be deemed as the next highest "ranking" after the card of the
                        // trump suit with a number value of 9
                        else if (state.currTrick.get(f).getSuit() == currTrump && state.currTrick.get(f).getNumValue() == 8)
                        {
                            if (ranking > 7)
                            {
                                trickWinner = trickOrder[f];
                                ranking = 8;
                            }
                        }

                        // the player who placed down a card of the trump suit with a number value
                        // of 7 will be deemed as the next highest "ranking" after the card of the
                        // trump suit with a number value of 8
                        else if (state.currTrick.get(f).getSuit() == currTrump && state.currTrick.get(f).getNumValue() == 7)
                        {
                            if (ranking > 8)
                            {
                                trickWinner = trickOrder[f];
                                ranking = 9;
                            }
                        }

                        // the player who placed down a card of the trump suit with a number value
                        // of 6 will be deemed as the next highest "ranking" after the card of the
                        // trump suit with a number value of 7
                        else if (state.currTrick.get(f).getSuit() == currTrump && state.currTrick.get(f).getNumValue() == 6)
                        {
                            if (ranking > 9)
                            {
                                trickWinner = trickOrder[f];
                                ranking = 10;
                            }
                        }

                        // the player who placed down a card of the trump suit with a number value
                        // of 5 will be deemed as the next highest "ranking" after the card of the
                        // trump suit with a number value of 6
                        else if (state.currTrick.get(f).getSuit() == currTrump && state.currTrick.get(f).getNumValue() == 5)
                        {
                            if (ranking > 10)
                            {
                                trickWinner = trickOrder[f];
                                ranking = 11;
                            }
                        }

                        // the player who placed down a card of the trump suit with a number value
                        // of 4 will be deemed as the next highest "ranking" after the card of the
                        // trump suit with a number value of 5
                        else if (state.currTrick.get(f).getSuit() == currTrump && state.currTrick.get(f).getNumValue() == 4)
                        {
                            if (ranking > 11)
                            {
                                trickWinner = trickOrder[f];
                                ranking = 12;
                            }
                        }

                        // the player who placed down a card of the suit of the firstly placed card
                        // of the trick with a number value of 14 will be deemed as the next highest
                        // "ranking" after the card of the trump suit with a number value of 4
                        else if (state.currTrick.get(f).getSuit() == firstCardSuit && state.currTrick.get(f).getNumValue() == 14)
                        {
                            if (ranking > 12)
                            {
                                trickWinner = trickOrder[f];
                                ranking = 13;
                            }
                        }

                        // the player who placed down a card of the suit of the firstly placed card
                        // of the trick with a number value of 13 will be deemed as the next highest
                        // "ranking" after the card of the suit of the first card of the trick with
                        // a number value of 14
                        else if (state.currTrick.get(f).getSuit() == firstCardSuit && state.currTrick.get(f).getNumValue() == 13)
                        {
                            if (ranking > 13)
                            {
                                trickWinner = trickOrder[f];
                                ranking = 14;
                            }
                        }

                        // the player who placed down a card of the suit of the firstly placed card
                        // of the trick with a number value of 12 will be deemed as the next highest
                        // "ranking" after the card of the suit of the first card of the trick with
                        // a number value of 13
                        else if (state.currTrick.get(f).getSuit() == firstCardSuit && state.currTrick.get(f).getNumValue() == 12)
                        {
                            if (ranking > 14)
                            {
                                trickWinner = trickOrder[f];
                                ranking = 15;
                            }
                        }

                        // the player who placed down a card of the suit of the firstly placed card
                        // of the trick with a number value of 11 will be deemed as the next highest
                        // "ranking" after the card of the suit of the first card of the trick with
                        // a number value of 12
                        else if (state.currTrick.get(f).getSuit() == firstCardSuit && state.currTrick.get(f).getNumValue() == 11)
                        {
                            if (ranking > 15)
                            {
                                trickWinner = trickOrder[f];
                                ranking = 16;
                            }
                        }

                        // the player who placed down a card of the suit of the firstly placed card
                        // of the trick with a number value of 10 will be deemed as the next highest
                        // "ranking" after the card of the suit of the first card of the trick with
                        // a number value of 11
                        else if (state.currTrick.get(f).getSuit() == firstCardSuit && state.currTrick.get(f).getNumValue() == 10)
                        {
                            if (ranking > 16)
                            {
                                trickWinner = trickOrder[f];
                                ranking = 17;
                            }
                        }

                        // the player who placed down a card of the suit of the firstly placed card
                        // of the trick with a number value of 9 will be deemed as the next highest
                        // "ranking" after the card of the suit of the first card of the trick with
                        // a number value of 10
                        else if (state.currTrick.get(f).getSuit() == firstCardSuit && state.currTrick.get(f).getNumValue() == 9)
                        {
                            if (ranking > 17)
                            {
                                trickWinner = trickOrder[f];
                                ranking = 18;
                            }
                        }

                        // the player who placed down a card of the suit of the firstly placed card
                        // of the trick with a number value of 8 will be deemed as the next highest
                        // "ranking" after the card of the suit of the first card of the trick with
                        // a number value of 9
                        else if (state.currTrick.get(f).getSuit() == firstCardSuit && state.currTrick.get(f).getNumValue() == 8)
                        {
                            if (ranking > 18)
                            {
                                trickWinner = trickOrder[f];
                                ranking = 19;
                            }
                        }

                        // the player who placed down a card of the suit of the firstly placed card
                        // of the trick with a number value of 7 will be deemed as the next highest
                        // "ranking" after the card of the suit of the first card of the trick with
                        // a number value of 8
                        else if (state.currTrick.get(f).getSuit() == firstCardSuit && state.currTrick.get(f).getNumValue() == 7)
                        {
                            if (ranking > 19)
                            {
                                trickWinner = trickOrder[f];
                                ranking = 20;
                            }
                        }

                        // the player who placed down a card of the suit of the firstly placed card
                        // of the trick with a number value of 6 will be deemed as the next highest
                        // "ranking" after the card of the suit of the first card of the trick with
                        // a number value of 7
                        else if (state.currTrick.get(f).getSuit() == firstCardSuit && state.currTrick.get(f).getNumValue() == 6)
                        {
                            if (ranking > 20)
                            {
                                trickWinner = trickOrder[f];
                                ranking = 21;
                            }
                        }

                        // the player who placed down a card of the suit of the firstly placed card
                        // of the trick with a number value of 5 will be deemed as the next highest
                        // "ranking" after the card of the suit of the first card of the trick with
                        // a number value of 6
                        else if (state.currTrick.get(f).getSuit() == firstCardSuit && state.currTrick.get(f).getNumValue() == 5)
                        {
                            if (ranking > 21)
                            {
                                trickWinner = trickOrder[f];
                                ranking = 22;
                            }
                        }

                        // the player who placed down a card of the suit of the firstly placed card
                        // of the trick with a number value of 4 will be deemed as the next highest
                        // "ranking" after the card of the suit of the first card of the trick with
                        // a number value of 5
                        else if (state.currTrick.get(f).getSuit() == firstCardSuit && state.currTrick.get(f).getNumValue() == 4)
                        {
                            if (ranking > 22)
                            {
                                trickWinner = trickOrder[f];
                                ranking = 23;
                            }
                        }

                    }

                    // after figuring out which player is the winner of the trick,
                    // add the counter-points included in that trick to the total points
                    // of the trick-winner
                    state.setScore(points, trickWinner);
                    state.setPlayer(trickWinner);
                    state.pointsThisRound[trickWinner] += points;

                    // the player who is the winner of the trick is the player who
                    // places down the first card of the next trick
                    state.currTrickWinner = trickWinner;
                }
                else
                {
                    // if the trick hasn't reach 4 cards yet, then continue to rotate
                    // player's turns in the clockwise direction
                    state.setPlayer();
                }

                // once 9 tricks have gone through where 36 cards have been placed down by each
                // player, then go through the process that checks through the players' scores at
                // the end of the round
                if(gameCounter >= 36)
                {
                    // give counter-points that are included in the nest to the final trick-winner
                    state.setScore(state.countNest(), trickWinner);
                    state.pointsThisRound[trickWinner] += state.countNest();

                    // set the player who won the last trick as the player who first places
                    // down a card during the next trick (during the next round)
                    state.setPlayer(trickWinner);
                    state.currTrickWinner = trickWinner;

                    // check to see if the number of points that the winning bidder has gathered
                    // after 9 tricks is greater than the winning bid amount that they made
                    // at the beginning of the round
                    if (state.pointsThisRound[state.lastBidder] < state.getHighestBid())
                    {
                        // if they've gathered a lower amount of points than their winning bid,
                        // then subtract their winning bid amount from their total poitns
                        state.setScore((-1 * state.getHighestBid()), state.lastBidder);
                    }

                    // initiate the start of a new state after 9 tricks have been played out
                    state = new RookState(true, state);

                    // restart the counter that keeps track of how many cards have been placed down
                    gameCounter = 0;
                }
            }
        }

        // if it makes it down here, an action was made
        return true;
    }
}
