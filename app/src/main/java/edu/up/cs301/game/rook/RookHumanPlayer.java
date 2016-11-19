package edu.up.cs301.game.rook;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
//import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * Created by hoser18 on 11/8/2016.
 */
public class RookHumanPlayer extends GameHumanPlayer
{
    //game state
    protected RookState state;

    // activity
    protected Activity activity;

    // background color
    private int backgroundColor;

    /** constructor
     *
     * @param name
     *      the player's name
     *
     * @param bkColor
     *      the background color
     */
    public RookHumanPlayer(String name, int bkColor) {
        super(name);
        backgroundColor = bkColor;
    }

    public void receiveInfo(GameInfo info)
    {

    }

    public View getTopView()
    {
        return null;
    }

    public int backgroundColor()
    {
        return backgroundColor;
    }

    public void setAsGui(GameMainActivity newActivity)
    {

    }

    public void onTouch(MotionEvent event)
    {
        // ignore everything except down touch events
        if (event.getAction() != MotionEvent.ACTION_DOWN)
        {
            return;
        }

        // get the location of the touch on the surface
        int x = (int)event.getX();
        int y = (int)event.getY();
    }
}


