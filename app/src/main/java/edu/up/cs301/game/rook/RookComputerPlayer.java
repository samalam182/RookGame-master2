package edu.up.cs301.game.rook;

import edu.up.cs301.game.GameComputerPlayer;

/**
 * Created by hoser18 on 11/8/2016.
 */
public abstract class RookComputerPlayer extends GameComputerPlayer {
    public double minReactionTimeInMillis;

    public RookState savedState;

    /**
     * constructor for RookComputerPlayer class
     *
     * @param name the player's name
     */
    public RookComputerPlayer(String name) {
        // invoke  general constructor to create a player whose average
        // reaction time is half a second
        this(name, 0.5);
    }

    public RookComputerPlayer(String name, double avgReactionTime) {
        super(name);

        //set min reaction time, which is half the average reaction time,
        // converted to milliseconds (0.5*1000 = 500)
        minReactionTimeInMillis = 500 * avgReactionTime;
    }

    // invoked when the player's time has ticked
    @Override
    protected abstract void timerTicked();

    // callback method, called when we receive a message, typically from the game
    protected abstract void receiveInfo();


}
