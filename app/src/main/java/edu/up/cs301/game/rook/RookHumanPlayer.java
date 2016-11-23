package edu.up.cs301.game.rook;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import edu.up.cs301.animation.AnimationSurface;
import edu.up.cs301.animation.Animator;
import edu.up.cs301.card.Card;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;


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
    public Button start;
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
    public ImageButton nest1;
    public ImageButton nest2;
    public ImageButton nest3;
    public ImageButton nest4;
    public ImageButton nest5;
    public ImageButton trumpYellow;
    public ImageButton trumpBlack;
    public ImageButton trumpGreen;
    public ImageButton trumpRed;
    public Button minusFive;
    public Button addFive;
    public Button confirmNest;
    public Button confirmTrump;
    public Button bidButton;
    public Button passButton;
    public TextView bidAmount;
    public TextView previousBid;

    public TextView trumpTitle;
    public TextView nestTitle;
    public ImageView trick1;
    public ImageView trick2;
    public ImageView trick3;
    public ImageView trick4;
    public TextView lastBidder;



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
        if (state != null)
        {
            receiveInfo(state);
        }

        //  animate the UI
        activity = newActivity;
        newActivity.setContentView(R.layout.rook_human_player);

        surface = (AnimationSurface) activity.findViewById(R.id.animation_surface);
        surface.setAnimator(this);

        // makes buttons

        start = (Button) activity.findViewById(R.id.buttonStartGame);
        start.setOnClickListener(this);
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

        nestTitle = (TextView) activity.findViewById(R.id.textView_NestLabel);
        nestTitle.setOnClickListener(this);

        nest1 = (ImageButton) activity.findViewById((R.id.imageButton_Nest_0));
        nest1.setOnClickListener(this);

        nest2 = (ImageButton) activity.findViewById((R.id.imageButton_Nest_1));
        nest2.setOnClickListener(this);

        nest3 = (ImageButton) activity.findViewById((R.id.imageButton_Nest_2));
        nest3.setOnClickListener(this);

        nest4 = (ImageButton) activity.findViewById((R.id.imageButton_Nest_3));
        nest4.setOnClickListener(this);

        nest5 = (ImageButton) activity.findViewById((R.id.imageButton_Nest_4));
        nest5.setOnClickListener(this);

        trumpTitle = (TextView) activity.findViewById(R.id.textView_TrumpSuitLabel);
        trumpTitle.setOnClickListener(this);

        trumpBlack = (ImageButton) activity.findViewById((R.id.imageButton_ChooseTrump_BlackMoon));
        trumpBlack.setOnClickListener(this);

        trumpYellow = (ImageButton) activity.findViewById((R.id.imageButton_ChooseTrump_YellowSun));
        trumpYellow.setOnClickListener(this);

        trumpGreen = (ImageButton) activity.findViewById((R.id.imageButton_ChooseTrump_GreenLeaf));
        trumpGreen.setOnClickListener(this);

        trumpRed = (ImageButton) activity.findViewById((R.id.imageButton_ChooseTrump_RedHeart));
        trumpRed.setOnClickListener(this);

        addFive = (Button) activity.findViewById(R.id.button_Increment5ToBid);
        addFive.setOnClickListener(this);

        minusFive = (Button) activity.findViewById(R.id.button_Decrement5ToBid);
        minusFive.setOnClickListener(this);

        confirmNest = (Button) activity.findViewById(R.id.button_ConfirmNestTrade);
        confirmNest.setOnClickListener(this);

        confirmTrump = (Button) activity.findViewById(R.id.button_ConfirmTrumpSuit);
        confirmTrump.setOnClickListener(this);

        bidButton = (Button) activity.findViewById(R.id.button_ConfirmBid);
        bidButton.setOnClickListener(this);

        passButton = (Button) activity.findViewById(R.id.button_ConfirmPass);
        passButton.setOnClickListener(this);

        bidAmount = (TextView) activity.findViewById(R.id.textView_YourBid);

        previousBid = (TextView) activity.findViewById(R.id.textView_AmountBid);

        trick1 = (ImageView) activity.findViewById(R.id.imageView_Trick_0);
        trick2 = (ImageView) activity.findViewById(R.id.imageView_Trick_1);
        trick3 = (ImageView) activity.findViewById(R.id.imageView_Trick_2);
        trick4 = (ImageView) activity.findViewById(R.id.imageView_Trick_3);

        lastBidder = (TextView) activity.findViewById(R.id.textView_LastBidder);

        card0.setImageResource(R.drawable.rookcard_back);
        card1.setImageResource(R.drawable.rookcard_back);
        card2.setImageResource(R.drawable.rookcard_back);
        card3.setImageResource(R.drawable.rookcard_back);
        card4.setImageResource(R.drawable.rookcard_back);
        card5.setImageResource(R.drawable.rookcard_back);
        card6.setImageResource(R.drawable.rookcard_back);
        card7.setImageResource(R.drawable.rookcard_back);
        card8.setImageResource(R.drawable.rookcard_back);

    }

    public void correctHandImage(int playerIndx)
    {
        if (playerIndx == 1)
        {
            // gets an array of all card objects
            ImageButton[] card = {card0, card1, card2, card3, card4, card5, card6, card7, card8};

            // the card that we're looking for is being stored
            Card getting;

            for (int i = 0; i < 9; i++)
            {
                // gets the card we're looking for. [0] = only player one.
                // for future human players, needs to be another look per player
                getting = state.playerHands[0].get(i);

                // get numvalue = the number of the card
                // get suit = the value 0 = black , 1 = red, 2 = yellow, 3 = green
                if (getting.getNumValue() == 5)
                {
                    if (getting.getSuit() == 0)
                    {
                        card[i].setImageResource(R.drawable.rookcard_5b);
                    }
                    else if (getting.getSuit() == 1)
                    {
                        card[i].setImageResource(R.drawable.rookcard_5r);
                    }
                    else if (getting.getSuit() == 2)
                    {
                        card[i].setImageResource(R.drawable.rookcard_5y);
                    }
                    else if (getting.getSuit() == 3)
                    {
                        card[i].setImageResource(R.drawable.rookcard_5g);
                    }
                }
                else if (getting.getNumValue() == 6)
                {
                    if (getting.getSuit() == 0)
                    {
                        card[i].setImageResource(R.drawable.rookcard_6b);
                    }
                    else if (getting.getSuit() == 1)
                    {
                        card[i].setImageResource(R.drawable.rookcard_6r);
                    }
                    else if (getting.getSuit() == 2)
                    {
                        card[i].setImageResource(R.drawable.rookcard_6y);
                    }
                    else if (getting.getSuit() == 3)
                    {
                        card[i].setImageResource(R.drawable.rookcard_6g);
                    }
                }
                else if (getting.getNumValue() == 7)
                {
                    if (getting.getSuit() == 0)
                    {
                        card[i].setImageResource(R.drawable.rookcard_7b);
                    }
                    else if (getting.getSuit() == 1)
                    {
                        card[i].setImageResource(R.drawable.rookcard_7r);
                    }
                    else if (getting.getSuit() == 2)
                    {
                        card[i].setImageResource(R.drawable.rookcard_7y);
                    }
                    else if (getting.getSuit() == 3)
                    {
                        card[i].setImageResource(R.drawable.rookcard_7g);
                    }
                }
                else if (getting.getNumValue() == 8)
                {
                    if (getting.getSuit() == 0)
                    {
                        card[i].setImageResource(R.drawable.rookcard_8b);
                    }
                    else if (getting.getSuit() == 1)
                    {
                        card[i].setImageResource(R.drawable.rookcard_8r);
                    }
                    else if (getting.getSuit() == 2)
                    {
                        card[i].setImageResource(R.drawable.rookcard_8y);
                    }
                    else if (getting.getSuit() == 3)
                    {
                        card[i].setImageResource(R.drawable.rookcard_8g);
                    }
                }
                else if (getting.getNumValue() == 9)
                {
                    if (getting.getSuit() == 0)
                    {
                        card[i].setImageResource(R.drawable.rookcard_9b);
                    }
                    else if (getting.getSuit() == 1)
                    {
                        card[i].setImageResource(R.drawable.rookcard_9r);
                    }
                    else if (getting.getSuit() == 2)
                    {
                        card[i].setImageResource(R.drawable.rookcard_9y);
                    }
                    else if (getting.getSuit() == 3)
                    {
                        card[i].setImageResource(R.drawable.rookcard_9g);
                    }
                }
                else if (getting.getNumValue() == 10)
                {
                    if (getting.getSuit() == 0)
                    {
                        card[i].setImageResource(R.drawable.rookcard_10b);
                    }
                    else if (getting.getSuit() == 1)
                    {
                        card[i].setImageResource(R.drawable.rookcard_10r);
                    }
                    else if (getting.getSuit() == 2)
                    {
                        card[i].setImageResource(R.drawable.rookcard_10y);
                    }
                    else if (getting.getSuit() == 3)
                    {
                        card[i].setImageResource(R.drawable.rookcard_10g);
                    }
                }
                else if (getting.getNumValue() == 11)
                {
                    if (getting.getSuit() == 0)
                    {
                        card[i].setImageResource(R.drawable.rookcard_11b);
                    }
                    else if (getting.getSuit() == 1)
                    {
                        card[i].setImageResource(R.drawable.rookcard_11r);
                    }
                    else if (getting.getSuit() == 2)
                    {
                        card[i].setImageResource(R.drawable.rookcard_11y);
                    }
                    else if (getting.getSuit() == 3)
                    {
                        card[i].setImageResource(R.drawable.rookcard_11g);
                    }
                }
                else if (getting.getNumValue() == 12)
                {
                    if (getting.getSuit() == 0)
                    {
                        card[i].setImageResource(R.drawable.rookcard_12b);
                    }
                    else if (getting.getSuit() == 1)
                    {
                        card[i].setImageResource(R.drawable.rookcard_12r);
                    }
                    else if (getting.getSuit() == 2)
                    {
                        card[i].setImageResource(R.drawable.rookcard_12y);
                    }
                    else if (getting.getSuit() == 3)
                    {
                        card[i].setImageResource(R.drawable.rookcard_12g);
                    }
                }
                else if (getting.getNumValue() == 13)
                {
                    if (getting.getSuit() == 0)
                    {
                        card[i].setImageResource(R.drawable.rookcard_13b);
                    }
                    else if (getting.getSuit() == 1)
                    {
                        card[i].setImageResource(R.drawable.rookcard_13r);
                    }
                    else if (getting.getSuit() == 2)
                    {
                        card[i].setImageResource(R.drawable.rookcard_13y);
                    }
                    else if (getting.getSuit() == 3)
                    {
                        card[i].setImageResource(R.drawable.rookcard_13g);
                    }
                }
                else if (getting.getNumValue() == 14)
                {
                    if (getting.getSuit() == 0)
                    {
                        card[i].setImageResource(R.drawable.rookcard_14b);
                    }
                    else if (getting.getSuit() == 1)
                    {
                        card[i].setImageResource(R.drawable.rookcard_14r);
                    }
                    else if (getting.getSuit() == 2)
                    {
                        card[i].setImageResource(R.drawable.rookcard_14y);
                    }
                    else if (getting.getSuit() == 3)
                    {
                        card[i].setImageResource(R.drawable.rookcard_14g);
                    }
                }
                else if(getting.getNumValue() == 15)
                {
                    card[i].setImageResource(R.drawable.rookcard_rook);
                }
                else
                {
                    card[i].setImageResource(R.drawable.rookcard_back);
                }
            }
        }
    }

    public void onClick(View v)
    {
        int myNum = 0;
        int myNum2 = 0;
        if (v == quit)
        {
            activity.finish();
            System.exit(0);
        }
        else if( v == start)
        {
            correctHandImage(1);
            trumpBlack.setVisibility(View.INVISIBLE);
            trumpGreen.setVisibility(View.INVISIBLE);
            trumpYellow.setVisibility(View.INVISIBLE);
            trumpRed.setVisibility(View.INVISIBLE);
            confirmNest.setVisibility(View.INVISIBLE);
            confirmTrump.setVisibility(View.INVISIBLE);
            nest1.setVisibility(View.INVISIBLE);
            nest2.setVisibility(View.INVISIBLE);
            nest3.setVisibility(View.INVISIBLE);
            nest4.setVisibility(View.INVISIBLE);
            nest5.setVisibility(View.INVISIBLE);
            trumpTitle.setVisibility(View.INVISIBLE);
            nestTitle.setVisibility(View.INVISIBLE);
            trick1.setVisibility(View.INVISIBLE);
            trick2.setVisibility(View.INVISIBLE);
            trick3.setVisibility(View.INVISIBLE);
            trick4.setVisibility(View.INVISIBLE);
        }
        else if (v == card0){
            game.sendAction(new RookCardAction(this, 0));
        }
        else if (v == card1){
            game.sendAction(new RookCardAction(this, 1));
        }
        else if (v == card2){
            game.sendAction(new RookCardAction(this, 2));
        }
        else if (v == card3){
            game.sendAction(new RookCardAction(this, 3));
        }
        else if (v == card4){
            game.sendAction(new RookCardAction(this, 4));
        }
        else if (v == card5){
            game.sendAction(new RookCardAction(this, 5));
        }
        else if (v == card6){
            game.sendAction(new RookCardAction(this, 6));
        }
        else if (v == card7){
            game.sendAction(new RookCardAction(this, 7));
        }
        else if (v == card8){
            game.sendAction(new RookCardAction(this, 8));
        }
        else if (v == nest1){
            game.sendAction(new RookCardAction(this, 0));
        }
        else if (v == nest2){
            game.sendAction(new RookCardAction(this, 1));
        }
        else if (v == nest3){
            game.sendAction(new RookCardAction(this, 2));
        }
        else if (v == nest4){
            game.sendAction(new RookCardAction(this, 3));
        }
        else if (v == trumpBlack){
            game.sendAction(new RookCardAction(this, 4));
        }
        else if (v == trumpYellow){
            game.sendAction(new RookCardAction(this, 5));
        }
        else if (v == trumpGreen){
            game.sendAction(new RookCardAction(this, 6));
        }
        else if (v == trumpRed){
            game.sendAction(new RookCardAction(this, 7));
        }
        else if (v == addFive){
            try {
                myNum = Integer.parseInt(bidAmount.getText().toString());
            } catch(NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }

            if(myNum < 120){
                myNum += 5;
            }
            bidAmount.setText("" + myNum);
        }
        else if (v == minusFive){

            try {
                myNum = Integer.parseInt(bidAmount.getText().toString());
                myNum2 = Integer.parseInt(previousBid.getText().toString());
            } catch(NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }

            if(myNum > (myNum2+5)){
                myNum -= 5;
            }

            bidAmount.setText("" + myNum);
        }
        else if (v == confirmNest){
            game.sendAction(new RookCardAction(this, 8));
        }
        else if (v == confirmTrump){
            game.sendAction(new RookCardAction(this, 8));
        }
        else if (v == bidButton){
            try {
                myNum = Integer.parseInt(bidAmount.getText().toString());
            } catch(NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }
            previousBid.setText("" + myNum);
            game.sendAction(new RookBidAction(this, myNum));

            lastBidder.setText("Human Player 1");

        }
        else if (v == passButton){
            game.sendAction(new RookHoldAction(this));
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