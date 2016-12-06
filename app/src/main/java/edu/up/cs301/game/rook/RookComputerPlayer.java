package edu.up.cs301.game.rook;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * An abstract class that represents the generic Computer Player specifically for the game of Rook.
 * Both the "dumb" and "smart" computer player classes will extend this class to inherit
 * the functionality of being able to receive data from the RookState and LocalGame
 *
 * @author Sam DeWhitt, Eric Hoser, Mitchell Nguyen, Alexander Nowlin
 * @version December 2016
 */
public abstract class RookComputerPlayer extends GameComputerPlayer {
    public double minReactionTimeInMillis;

    public RookState savedState;

    //public double reactionTime;

    /**
     * constructor for RookComputerPlayer class
     *
     * @param name
     *     the player's name
     *
     * @param avgReactionTime
     *     the average reaction time of the computer player
     */
    public RookComputerPlayer(String name, double avgReactionTime) {
        super(name);

        //set min reaction time, which is half the average reaction time,
        // converted to milliseconds (0.5*1000 = 500)
        minReactionTimeInMillis = 500 * avgReactionTime;
    }



    @Override
//    protected abstract void timerTicked();

    /**
     * callback method, called when we receive a message, typically from
     * the game
     */
    protected abstract void receiveInfo(GameInfo info);

}
