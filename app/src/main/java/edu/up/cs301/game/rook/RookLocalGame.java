package edu.up.cs301.game.rook;

import android.util.Log;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;

/**
 * Created by hoser18 on 11/8/2016.
 */
public class RookLocalGame extends LocalGame
{
    // the game's state
    public RookState state;

    public RookLocalGame()
    {
        Log.i("RookLocalGame", "Local Game being created");

        state = new RookState();
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
        editedState.nullHiddenInformation(p);

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

        // if all the scores are under 200
        if (state.getScore(0) < 200 && state.getScore(1) < 200 && state.getScore(2) < 200 && state.getScore(3) < 200)
        {
            return "End of this round. Prepare for the next.";
        }

        return "";
    }


    protected boolean makeMove(GameAction action)
    {
        return false;
    }
}
