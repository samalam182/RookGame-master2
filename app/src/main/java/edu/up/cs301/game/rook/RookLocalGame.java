package edu.up.cs301.game.rook;

import android.util.Log;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.rook.*;


/**
 * Created by hoser18 on 11/8/2016.
 */
public class RookLocalGame extends LocalGame
{
    // the game's state
    public RookState state;
    public final int WAIT = 0;
    public final int BID = 1;
    public final int TRUMP = 2;
    public final int NEST = 3;
    public final int PLAY = 4;
    public final int OVER = 5;
    Card nullCard = new Card(16, 1);

    public RookLocalGame()
    {
        Log.i("RookLocalGame", "Local Game being created");

        state = new RookState();
        state.setSubStage(WAIT);
    }

    // sends the updated state to the given player.
    // nulls out any hidden information that the player shouldn't know
    @Override
    protected void sendUpdatedStateTo(GamePlayer p)
    {
        // if there is no state, then then doesnt update
        if (state == null)
        {
            return;
        }

        // creates a rook state that will contain only the player's
        // imformation that s/he should know
        RookState editedState = new RookState(state);
        //editedState.nullHiddenInformation(state.getActivePlayer());
        p.sendInfo(editedState);
    }

    // if its the active player's turn, they can move
    protected boolean canMove(int playerIdx)
    {
        // only playerIndx of 0-3 are value numbers
        if (playerIdx < 0 || playerIdx > 3)
        {
            return false;
        }
        else
        {
            return state.getActivePlayer() == playerIdx;
        }
    }

    // checks if anyone has 200 or more points
    // if not then end of round
    protected String checkIfGameOver()
    {
        // check if any player has won
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

        return null;
    }


    protected boolean makeMove(GameAction action)
    {
        if(state.getSubStage() == WAIT)
        {
            state.setSubStage(BID);
        }

        int playerIdxx = state.getActivePlayer();
        // checks if its a type of RookAction
        // if not it isnt an action we want
        if (!(action instanceof RookBidAction || action instanceof RookCardAction ||
                action instanceof RookHoldAction || action instanceof RookNestAction ||
                action instanceof RookTrumpAction))
        {
            return false;
        }

        // makes action not a specific rook action
        if (action instanceof RookBidAction)
        {

            if(state.getSubStage() == BID)
            {
                RookBidAction act = (RookBidAction) action;
                int playBid = act.getBid();
                state.setBid(playBid, playerIdxx);

                state.lastBidder = playerIdxx;


                if (state.finalizeBids())
                {
                    state.setSubStage(NEST);
                    return true;
                }
                else
                {
                    state.setPlayer();
                    return true;
                }

            }

            // can the player still bid
        }
        else if (action instanceof RookCardAction)
        {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int startingPlayer;
            int trickWinner = 0;
            int points = 0;
            if(state.getSubStage() == PLAY)
            {
                if(state.currTrick.size() == 0)
                {
                    startingPlayer = playerIdxx;
                }

                if(state.currTrick.size() == 4)
                {
                    state.currTrick.clear();
                }

                RookCardAction act = (RookCardAction) action;
                int handIdx = act.retButtonNum();

                if(state.currTrick.size()<4) {
                    state.currTrick.add(state.playerHands[state.getActivePlayer()].get(handIdx));
                    //state.playerHands[state.getActivePlayer()].set(handIdx, nullCard);
                    state.playerHands[state.getActivePlayer()].get(handIdx).setPlayed();

                }

                if(state.currTrick.size() == 4)
                {
                    points = state.countTrick();
                    Card first = state.currTrick.get(0);
                    int firstCardVal = first.getNumValue();
                    int firstCardSuit = first.getSuit();
                    int currTrump = state.getTrump();
                    Card domCard;

                    int ranking = 1000;
                    int counter = 0;

                    for (Card cards : state.currTrick )
                    {
                        if (cards.getNumValue() == 15)
                        {
                            trickWinner = counter;
                            ranking = 1;
                        }
                        else if (cards.getSuit() == currTrump && cards.getNumValue() == 14)
                        {
                            if (ranking > 1)
                            {
                                trickWinner = counter;
                                counter++;
                                ranking = 2;
                            }
                        }
                        else if (cards.getSuit() == currTrump && cards.getNumValue() == 13)
                        {
                            if (ranking > 2)
                            {
                                trickWinner = counter;
                                counter++;
                                ranking = 3;
                            }
                        }
                        else if (cards.getSuit() == currTrump && cards.getNumValue() == 12)
                        {
                            if (ranking > 3)
                            {
                                trickWinner = counter;
                                counter++;
                                ranking = 4;
                            }
                        }
                        else if (cards.getSuit() == currTrump && cards.getNumValue() == 11)
                        {
                            if (ranking > 4)
                            {
                                trickWinner = counter;
                                counter++;
                                ranking = 5;
                            }
                        }
                        else if (cards.getSuit() == currTrump && cards.getNumValue() == 10)
                        {
                            if (ranking > 5)
                            {
                                trickWinner = counter;
                                counter++;
                                ranking = 6;
                            }
                        }
                        else if (cards.getSuit() == currTrump && cards.getNumValue() == 9)
                        {
                            if (ranking > 6)
                            {
                                trickWinner = counter;
                                counter++;
                                ranking = 7;
                            }
                        }
                        else if (cards.getSuit() == currTrump && cards.getNumValue() == 8)
                        {
                            if (ranking > 7)
                            {
                                trickWinner = counter;
                                counter++;
                                ranking = 8;
                            }
                        }
                        else if (cards.getSuit() == currTrump && cards.getNumValue() == 7)
                        {
                            if (ranking > 8)
                            {
                                trickWinner = counter;
                                counter++;
                                ranking = 9;
                            }
                        }
                        else if (cards.getSuit() == currTrump && cards.getNumValue() == 6)
                        {
                            if (ranking > 9)
                            {
                                trickWinner = counter;
                                counter++;
                                ranking = 10;
                            }
                        }
                        else if (cards.getSuit() == currTrump && cards.getNumValue() == 5)
                        {
                            if (ranking > 10)
                            {
                                trickWinner = counter;
                                counter++;
                                ranking = 11;
                            }
                        }
                        else if (cards.getSuit() == currTrump && cards.getNumValue() == 4)
                        {
                            if (ranking > 11)
                            {
                                trickWinner = counter;
                                counter++;
                                ranking = 12;
                            }
                        }
                        else if (cards.getSuit() == firstCardSuit && cards.getNumValue() == 14)
                        {
                            if (ranking > 12)
                            {
                                trickWinner = counter;
                                counter++;
                                ranking = 13;
                            }
                        }
                        else if (cards.getSuit() == firstCardSuit && cards.getNumValue() == 13)
                        {
                            if (ranking > 13)
                            {
                                trickWinner = counter;
                                counter++;
                                ranking = 14;
                            }
                        }
                        else if (cards.getSuit() == firstCardSuit && cards.getNumValue() == 12)
                        {
                            if (ranking > 14)
                            {
                                trickWinner = counter;
                                counter++;
                                ranking = 15;
                            }
                        }
                        else if (cards.getSuit() == firstCardSuit && cards.getNumValue() == 11)
                        {
                            if (ranking > 15)
                            {
                                trickWinner = counter;
                                counter++;
                                ranking = 16;
                            }
                        }
                        else if (cards.getSuit() == firstCardSuit && cards.getNumValue() == 10)
                        {
                            if (ranking > 16)
                            {
                                trickWinner = counter;
                                counter++;
                                ranking = 17;
                            }
                        }
                        else if (cards.getSuit() == firstCardSuit && cards.getNumValue() == 9)
                        {
                            if (ranking > 17)
                            {
                                trickWinner = counter;
                                counter++;
                                ranking = 18;
                            }
                        }
                        else if (cards.getSuit() == firstCardSuit && cards.getNumValue() == 8)
                        {
                            if (ranking > 18)
                            {
                                trickWinner = counter;
                                counter++;
                                ranking = 19;
                            }
                        }
                        else if (cards.getSuit() == firstCardSuit && cards.getNumValue() == 7)
                        {
                            if (ranking > 19)
                            {
                                trickWinner = counter;
                                counter++;
                                ranking = 20;
                            }
                        }
                        else if (cards.getSuit() == firstCardSuit && cards.getNumValue() == 6)
                        {
                            if (ranking > 20)
                            {
                                trickWinner = counter;
                                counter++;
                                ranking = 21;
                            }
                        }
                        else if (cards.getSuit() == firstCardSuit && cards.getNumValue() == 5)
                        {
                            if (ranking > 21)
                            {
                                trickWinner = counter;
                                counter++;
                                ranking = 22;
                            }
                        }
                        else if (cards.getSuit() == firstCardSuit && cards.getNumValue() == 4)
                        {
                            if (ranking > 22)
                            {
                                trickWinner = counter;
                                counter++;
                                ranking = 23;
                            }
                        }
                    }

                    state.setScore(points, trickWinner);
                    state.setPlayer(trickWinner);

                }
                else
                {
                    state.setPlayer();
                }
            }
        }
        else if (action instanceof RookHoldAction)
        {
            if(state.getSubStage() == BID)
            {
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                RookHoldAction act = (RookHoldAction) action;
                state.setHold(playerIdxx);

                state.pass[playerIdxx] = true;

                if (state.finalizeBids())
                {
                    state.setSubStage(NEST);
                    state.setPlayer(state.winningPlayer);
                    return true;
                }
                state.setPlayer();
            }
        }
        else if (action instanceof RookNestAction)
        {
            if(state.getSubStage() == NEST && playerIdxx == state.winningPlayer) {
                RookNestAction act = (RookNestAction) action;
                state.useNest(act.getNest(), act.getHand(), state.playerHands[playerIdxx]);
                state.setSubStage(TRUMP);
            }

            // checks to see if that player won the bid
        }
        else if (action instanceof RookTrumpAction) {
            if (state.getSubStage() == TRUMP && playerIdxx == state.winningPlayer) {
                RookTrumpAction act = (RookTrumpAction) action;
                state.setTrump(act.getTrumpColor());
                state.setSubStage(PLAY);
            }

        }

        // if it makes it down here, an action was made
        return true;
    }
}
