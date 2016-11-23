package edu.up.cs301.game.rook;

import android.util.Log;

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

    public RookLocalGame()
    {
        Log.i("RookLocalGame", "Local Game being created");

        state = new RookState();
        state.setSubStage(BID);
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
        RookState editedState = new RookState();
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
            if(state.getSubStage() == BID) {
                RookBidAction act = (RookBidAction) action;
                int playBid = act.getBid();
                state.setBid(playBid, playerIdxx);
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
            int startingPlayer;
            int trickWinner;
            if(state.getSubStage() == PLAY) {
                if(state.currTrick.size() == 0) {
                    startingPlayer = playerIdxx;
                }
                RookCardAction act = (RookCardAction) action;
                int handIdx = act.retButtonNum();
                state.currTrick.add(state.playerHands[playerIdxx].get(handIdx));

                if(state.currTrick.size() == 4){
                    int points = state.countTrick();
                }
            }

        }
        else if (action instanceof RookHoldAction)
        {
            if(state.getSubStage() == BID) {
                RookHoldAction act = (RookHoldAction) action;
                state.setHold(playerIdxx);
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
