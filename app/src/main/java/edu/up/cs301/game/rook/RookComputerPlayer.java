package edu.up.cs301.game.rook;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * Created by hoser18 on 11/8/2016.
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
