package edu.up.cs301.game.rook;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import edu.up.cs301.animation.AnimationSurface;
import edu.up.cs301.animation.Animator;
import edu.up.cs301.card.Card;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;
import edu.up.cs301.game.rook.CardActions.*;

/**
 * Created by hoser18 on 11/8/2016.
 */
public class RookHumanPlayer extends GameHumanPlayer implements Animator, View.OnClickListener
{
    //game state object
    protected RookState state;

    // activity object
    protected Activity activity;

    // animation surface object
    private AnimationSurface surface;

    // background color
    private int backgroundColor;

    // buttons
    public Button quit;
    public ImageButton card0;
    public ImageButton card1;
    public ImageButton card2;
    public ImageButton card3;
    public ImageButton card4;
    public ImageButton card5;
    public ImageButton card6;
    public ImageButton card7;
    public ImageButton card8;



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

    /**
     * @param info
     *      the message we have received from the game
     */
    public void receiveInfo(GameInfo info)
    {
        if (info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo)
        {
            // if the user attempts to play an illegal move, or not on their turn, flash the screen
            surface.flash(Color.RED, 50);
        }
        else if (!(info instanceof RookState))
        {
            // there is no game state, so ignore it
            return;
        }
        else
        {
            // there is a game state object, so update the state
            this.state = (RookState)info;
        }
    }

    /**
     * @return
     *     the top view GUI
     */
    @Override
    public View getTopView()
    {
        return activity.findViewById(R.id.top_gui_layout);

    }

    /**
     * @return
     *     the backgroud color
     */
    public int backgroundColor()
    {
        return backgroundColor;
    }

    /**
     *
     * @param newActivity
     *      the current activity
     */
    public void setAsGui(GameMainActivity newActivity)
    {
        //  animate the UI
        activity = newActivity;
        newActivity.setContentView(R.layout.rook_human_player);
        surface = (AnimationSurface) activity.findViewById(R.id.animation_surface);
        surface.setAnimator(this);

        // makes buttons

        quit = (Button) activity.findViewById(R.id.buttonQuitGame);
        quit.setOnClickListener(this);

        card0 = (ImageButton) activity.findViewById((R.id.imageButton_HumanHand_0));
        card0.setOnClickListener(this);

        card1 = (ImageButton) activity.findViewById((R.id.imageButton_HumanHand_1));
        card1.setOnClickListener(this);

        card2 = (ImageButton) activity.findViewById((R.id.imageButton_HumanHand_2));
        card2.setOnClickListener(this);

        card3 = (ImageButton) activity.findViewById((R.id.imageButton_HumanHand_3));
        card3.setOnClickListener(this);

        card4 = (ImageButton) activity.findViewById((R.id.imageButton_HumanHand_4));
        card4.setOnClickListener(this);

        card5 = (ImageButton) activity.findViewById((R.id.imageButton_HumanHand_5));
        card5.setOnClickListener(this);

        card6 = (ImageButton) activity.findViewById((R.id.imageButton_HumanHand_6));
        card6.setOnClickListener(this);

        card7 = (ImageButton) activity.findViewById((R.id.imageButton_HumanHand_7));
        card7.setOnClickListener(this);

        card8 = (ImageButton) activity.findViewById((R.id.imageButton_HumanHand_8));
        card8.setOnClickListener(this);


        card0.setImageResource(R.drawable.rookcard_rook);
        //correctHandImage(1);




        //Card.initImages(newActivity);

        if (state != null)
        {
            receiveInfo(state);
        }
    }

    public void correctHandImage(int playerIndx)
    {
        if (playerIndx == 1)
        {
            for (int i = 0; i < 9; i++)
            {
                Card cur = state.playerOneHand.get(i);
                card0.setImageResource(R.drawable.rookcard_rook);
            }
        }
    }

    public void onClick(View v)
    {
        if (v == quit)
        {
            activity.finish();
            System.exit(0);
        }
        else if (v == card0){
            game.sendAction(new bZeroAction(this));
        }
        else if (v == card1){
            game.sendAction(new bOneAction(this));
        }
        else if (v == card2){
            game.sendAction(new bTwoAction(this));
        }
        else if (v == card3){
            game.sendAction(new bThreeAction(this));
        }
        else if (v == card4){
            game.sendAction(new bFourAction(this));
        }
        else if (v == card5){
            game.sendAction(new bFiveAction(this));
        }
        else if (v == card6){
            game.sendAction(new bSixAction(this));
        }
        else if (v == card7){
            game.sendAction(new bSevenAction(this));
        }
        else if (v == card8){
            game.sendAction(new bEightAction(this));
        }
    }

    /**
     * @return
     *     tells whether the animation should be terminated
     */
    public boolean doQuit()
    {
        return false;
    }

    /**
     * @return
     *     tells whether the animation should be paused
     */
    public boolean doPause()
    {
        return false;
    }

    /** redraw the screen image:
     *     - the nest, with all five cards face down
     *     - the four player's hands, with only the user's hand visible for them, their opponents
     *     hands will be face down
     *     - a token to indicate whose turn it is
     *
     * @param g
     *     the canvas being drawn on
     */

    public void tick(Canvas g)
    {
        // ignore if there is no game state
        if (state == null)
        {
            return;
        }
        else
        {
            return;
        }
    }

    public void onTouch(MotionEvent event)
    {
        if (event.equals(quit))
        {
            activity.finish();
            System.exit(0);
        }
    }

    /**
     * @return
     *     the animation interval, in milliseconds
     */
    public int interval()
    {
        // 1/20 of a second
        return 50;
    }
}