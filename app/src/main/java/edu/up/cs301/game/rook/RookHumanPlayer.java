package edu.up.cs301.game.rook;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.Log;
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

import static edu.up.cs301.card.Card.cardImages;
import static edu.up.cs301.card.Card.initImages;


/**
 * Created by hoser18 on 11/8/2016.
 */
public class RookHumanPlayer extends GameHumanPlayer implements Animator, View.OnClickListener {
    //game state object
    protected RookState state;

    // activity object
    protected Activity activity;

    // animation surface object
    private AnimationSurface surface;

    // background color
    private int backgroundColor;

    public final int WAIT = 0;
    public final int BID = 1;
    public final int TRUMP = 2;
    public final int NEST = 3;
    public final int PLAY = 4;
    public final int OVER = 5;
    private final int BLACK = 0;
    private final int YELLOW = 1;
    private final int GREEN = 2;
    private final int RED = 3;

    private final int BLANK = 5;
    Card blankCard = new Card(20, BLANK);

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
    public TextView winningBidder;
    public TextView winningBid;

    public TextView trumpTitle;
    public TextView nestTitle;
    public ImageView trick1;
    public ImageView trick2;
    public ImageView trick3;
    public ImageView trick4;
    public TextView lastBidder;
    public TextView trumpAccounce;
    public TextView bidTitle;
    public TextView amountTitle;
    public TextView bidShow;
    public TextView bidMainTitle;
    public TextView yourBid;
    public ArrayList<Card> fromH = new ArrayList<Card>(5);
    public ArrayList<Card> fromN = new ArrayList<Card>(5);

    public TextView passOne;
    public TextView passTwo;
    public TextView passThree;
    public TextView passFour;
    public TextView passTitle;


    public int[] handSwitch = new int[5];
    public int trackH = 0;
    public int[] nestSwitch = new int[5];
    public int trackS = 0;


    public int delay = 0;
    public int trumpColor = -1;

    public ImageView[] work = new ImageView[9];

    public TextView humanPoints;

    public ImageView humanOrangeStar;
    public ImageView opponentOneOrangeStar;
    public ImageView opponentTwoOrangeStar;
    public ImageView opponentThreeOrangeStar;

    public ImageView opponentONECard0;
    public ImageView opponentONECard1;
    public ImageView opponentONECard2;
    public ImageView opponentONECard3;
    public ImageView opponentONECard4;
    public ImageView opponentONECard5;
    public ImageView opponentONECard6;
    public ImageView opponentONECard7;
    public ImageView opponentONECard8;

    public ImageView opponentTWOCard0;
    public ImageView opponentTWOCard1;
    public ImageView opponentTWOCard2;
    public ImageView opponentTWOCard3;
    public ImageView opponentTWOCard4;
    public ImageView opponentTWOCard5;
    public ImageView opponentTWOCard6;
    public ImageView opponentTWOCard7;
    public ImageView opponentTWOCard8;

    public ImageView opponentTHREECard0;
    public ImageView opponentTHREECard1;
    public ImageView opponentTHREECard2;
    public ImageView opponentTHREECard3;
    public ImageView opponentTHREECard4;
    public ImageView opponentTHREECard5;
    public ImageView opponentTHREECard6;
    public ImageView opponentTHREECard7;
    public ImageView opponentTHREECard8;

    public TextView oneScore;
    public TextView twoScore;
    public TextView threeScore;
    public TextView fourScore;



    /**
     * constructor
     *
     * @param name    the player's name
     * @param bkColor the background color
     */
    public RookHumanPlayer(String name, int bkColor) {
        super(name);
        backgroundColor = bkColor;
    }

    /**
     * @param info the message we have received from the game
     */
    public void receiveInfo(GameInfo info) {
        if (info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo) {
            // if the user attempts to play an illegal move, or not on their turn, flash the screen
            surface.flash(Color.RED, 50);
        } else if (!(info instanceof RookState)) {
            // there is no game state, so ignore it
            return;
        }
        else
        {
            // there is a game state object, so update the state
            this.state = (RookState) info;
            updateGUI(state);

            if (state.pass[0] && state.getSubStage() == BID)
            {
                game.sendAction(new RookHoldAction(this));
            }
        }
    }

    /**
     * @return the top view GUI
     */
    @Override
    public View getTopView() {
        return activity.findViewById(R.id.top_gui_layout);

    }

    /**
     * @return the backgroud color
     */
    public int backgroundColor() {
        return backgroundColor;
    }

    /**
     * @param newActivity the current activity
     */
    public void setAsGui(GameMainActivity newActivity) {
        if (state != null) {
            receiveInfo(state);
        }

        //  animate the UI
        activity = newActivity;
        newActivity.setContentView(R.layout.rook_human_player);

        initImages(activity);

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

        winningBid = (TextView) activity.findViewById(R.id.textView_ROUNDWinningBid);

        winningBidder = (TextView) activity.findViewById(R.id.textView_ROUNDWinningBidder);

        bidAmount = (TextView) activity.findViewById(R.id.textView_YourBid);

        previousBid = (TextView) activity.findViewById(R.id.textView_AmountBid);

        trick1 = (ImageView) activity.findViewById(R.id.imageView_Trick_0);
        trick2 = (ImageView) activity.findViewById(R.id.imageView_Trick_1);
        trick3 = (ImageView) activity.findViewById(R.id.imageView_Trick_2);
        trick4 = (ImageView) activity.findViewById(R.id.imageView_Trick_3);
        trick1.setImageResource(R.drawable.rookcard_back);
        trick2.setImageResource(R.drawable.rookcard_back);
        trick3.setImageResource(R.drawable.rookcard_back);
        trick4.setImageResource(R.drawable.rookcard_back);

        passOne = (TextView) activity.findViewById(R.id.textView_PassedPlayersZERO);
        passTwo = (TextView) activity.findViewById(R.id.textView_PassedPlayersONE);
        passThree = (TextView) activity.findViewById(R.id.textView_PassedPlayersTWO);
        passFour = (TextView) activity.findViewById(R.id.textView_PassedPlayersTHREE);
        passTitle = (TextView) activity.findViewById(R.id.textView_PassedPlayersLabel);

        lastBidder = (TextView) activity.findViewById(R.id.textView_LastBidder);

        trumpAccounce = (TextView) activity.findViewById(R.id.textView_ROUNDTrumpSuit);

        bidTitle = (TextView) activity.findViewById(R.id.textView_LastBidderLabel);
        amountTitle = (TextView) activity.findViewById(R.id.textView_AmountBidLabel);
        bidShow = (TextView) activity.findViewById(R.id.textView_AmountBid);
        bidMainTitle = (TextView) activity.findViewById(R.id.textView_BidOrPassLabel);
        yourBid = (TextView) activity.findViewById(R.id.textView_YourBidLabel);

        card0.setImageResource(R.drawable.rookcard_back);
        card1.setImageResource(R.drawable.rookcard_back);
        card2.setImageResource(R.drawable.rookcard_back);
        card3.setImageResource(R.drawable.rookcard_back);
        card4.setImageResource(R.drawable.rookcard_back);
        card5.setImageResource(R.drawable.rookcard_back);
        card6.setImageResource(R.drawable.rookcard_back);
        card7.setImageResource(R.drawable.rookcard_back);
        card8.setImageResource(R.drawable.rookcard_back);

        // trump views
        confirmTrump.setVisibility(View.INVISIBLE);
        trumpTitle.setVisibility(View.INVISIBLE);
        trumpBlack.setVisibility(View.INVISIBLE);
        trumpGreen.setVisibility(View.INVISIBLE);
        trumpYellow.setVisibility(View.INVISIBLE);
        trumpRed.setVisibility(View.INVISIBLE);

        // nest views
        nestTitle.setVisibility(View.INVISIBLE);
        confirmNest.setVisibility(View.INVISIBLE);
        nest1.setVisibility(View.INVISIBLE);
        nest2.setVisibility(View.INVISIBLE);
        nest3.setVisibility(View.INVISIBLE);
        nest4.setVisibility(View.INVISIBLE);
        nest5.setVisibility(View.INVISIBLE);

        // trick views
        trick1.setVisibility(View.INVISIBLE);
        trick2.setVisibility(View.INVISIBLE);
        trick3.setVisibility(View.INVISIBLE);
        trick4.setVisibility(View.INVISIBLE);

        //bid views
        bidButton.setVisibility(View.INVISIBLE);
        passButton.setVisibility(View.INVISIBLE);
        minusFive.setVisibility(View.INVISIBLE);
        addFive.setVisibility(View.INVISIBLE);
        bidTitle.setVisibility(View.INVISIBLE);
        lastBidder.setVisibility(View.INVISIBLE);
        amountTitle.setVisibility(View.INVISIBLE);
        bidAmount.setVisibility(View.INVISIBLE);
        bidShow.setVisibility(View.INVISIBLE);
        bidMainTitle.setVisibility(View.INVISIBLE);
        yourBid.setVisibility(View.INVISIBLE);
        passOne.setVisibility(View.INVISIBLE);
        passTwo.setVisibility(View.INVISIBLE);
        passThree.setVisibility(View.INVISIBLE);
        passFour.setVisibility(View.INVISIBLE);
        passTitle.setVisibility(View.INVISIBLE);
        //updateGUI(state);

        humanPoints = (TextView) activity.findViewById(R.id.textView_HumanTotalPoints);

        //orange-star views
        humanOrangeStar = (ImageView) activity.findViewById(R.id.imageView_HumanOrangeStar);
        humanOrangeStar.setVisibility(View.INVISIBLE);
        opponentOneOrangeStar = (ImageView) activity.findViewById(R.id.imageView_OpponentONEOrangeStar);
        opponentOneOrangeStar.setVisibility(View.INVISIBLE);
        opponentTwoOrangeStar = (ImageView) activity.findViewById(R.id.imageView_OpponentTWOOrangeStar);
        opponentTwoOrangeStar.setVisibility(View.INVISIBLE);
        opponentThreeOrangeStar = (ImageView) activity.findViewById(R.id.imageView_OpponentTHREEOrangeStar);
        opponentThreeOrangeStar.setVisibility(View.INVISIBLE);

        //cards of opponents' hands views
        opponentONECard0 = (ImageView) activity.findViewById(R.id.imageView_OpponentONEHand_0);
        opponentONECard0.setImageResource(R.drawable.rookcard_back);
        opponentONECard1 = (ImageView) activity.findViewById(R.id.imageView_OpponentONEHand_1);
        opponentONECard1.setImageResource(R.drawable.rookcard_back);
        opponentONECard2 = (ImageView) activity.findViewById(R.id.imageView_OpponentONEHand_2);
        opponentONECard2.setImageResource(R.drawable.rookcard_back);
        opponentONECard3 = (ImageView) activity.findViewById(R.id.imageView_OpponentONEHand_3);
        opponentONECard3.setImageResource(R.drawable.rookcard_back);
        opponentONECard4 = (ImageView) activity.findViewById(R.id.imageView_OpponentONEHand_4);
        opponentONECard4.setImageResource(R.drawable.rookcard_back);
        opponentONECard5 = (ImageView) activity.findViewById(R.id.imageView_OpponentONEHand_5);
        opponentONECard5.setImageResource(R.drawable.rookcard_back);
        opponentONECard6 = (ImageView) activity.findViewById(R.id.imageView_OpponentONEHand_6);
        opponentONECard6.setImageResource(R.drawable.rookcard_back);
        opponentONECard7 = (ImageView) activity.findViewById(R.id.imageView_OpponentONEHand_7);
        opponentONECard7.setImageResource(R.drawable.rookcard_back);
        opponentONECard8 = (ImageView) activity.findViewById(R.id.imageView_OpponentONEHand_8);
        opponentONECard8.setImageResource(R.drawable.rookcard_back);

        opponentTWOCard0 = (ImageView) activity.findViewById(R.id.imageView_OpponentTWOHand_0);
        opponentTWOCard0.setImageResource(R.drawable.rookcard_back);
        opponentTWOCard1 = (ImageView) activity.findViewById(R.id.imageView_OpponentTWOHand_1);
        opponentTWOCard1.setImageResource(R.drawable.rookcard_back);
        opponentTWOCard2 = (ImageView) activity.findViewById(R.id.imageView_OpponentTWOHand_2);
        opponentTWOCard2.setImageResource(R.drawable.rookcard_back);
        opponentTWOCard3 = (ImageView) activity.findViewById(R.id.imageView_OpponentTWOHand_3);
        opponentTWOCard3.setImageResource(R.drawable.rookcard_back);
        opponentTWOCard4 = (ImageView) activity.findViewById(R.id.imageView_OpponentTWOHand_4);
        opponentTWOCard4.setImageResource(R.drawable.rookcard_back);
        opponentTWOCard5 = (ImageView) activity.findViewById(R.id.imageView_OpponentTWOHand_5);
        opponentTWOCard5.setImageResource(R.drawable.rookcard_back);
        opponentTWOCard6 = (ImageView) activity.findViewById(R.id.imageView_OpponentTWOHand_6);
        opponentTWOCard6.setImageResource(R.drawable.rookcard_back);
        opponentTWOCard7 = (ImageView) activity.findViewById(R.id.imageView_OpponentTWOHand_7);
        opponentTWOCard7.setImageResource(R.drawable.rookcard_back);
        opponentTWOCard8 = (ImageView) activity.findViewById(R.id.imageView_OpponentTWOHand_8);
        opponentTWOCard8.setImageResource(R.drawable.rookcard_back);

        opponentTHREECard0 = (ImageView) activity.findViewById(R.id.imageView_OpponentTWOHand_0);
        opponentTHREECard0.setImageResource(R.drawable.rookcard_back);
        opponentTHREECard1 = (ImageView) activity.findViewById(R.id.imageView_OpponentTWOHand_1);
        opponentTHREECard1.setImageResource(R.drawable.rookcard_back);
        opponentTHREECard2 = (ImageView) activity.findViewById(R.id.imageView_OpponentTWOHand_2);
        opponentTHREECard2.setImageResource(R.drawable.rookcard_back);
        opponentTHREECard3 = (ImageView) activity.findViewById(R.id.imageView_OpponentTWOHand_3);
        opponentTHREECard3.setImageResource(R.drawable.rookcard_back);
        opponentTHREECard4 = (ImageView) activity.findViewById(R.id.imageView_OpponentTWOHand_4);
        opponentTHREECard4.setImageResource(R.drawable.rookcard_back);
        opponentTHREECard5 = (ImageView) activity.findViewById(R.id.imageView_OpponentTWOHand_5);
        opponentTHREECard5.setImageResource(R.drawable.rookcard_back);
        opponentTHREECard6 = (ImageView) activity.findViewById(R.id.imageView_OpponentTWOHand_6);
        opponentTHREECard6.setImageResource(R.drawable.rookcard_back);
        opponentTHREECard7 = (ImageView) activity.findViewById(R.id.imageView_OpponentTWOHand_7);
        opponentTHREECard7.setImageResource(R.drawable.rookcard_back);
        opponentTHREECard8 = (ImageView) activity.findViewById(R.id.imageView_OpponentTWOHand_8);
        opponentTHREECard8.setImageResource(R.drawable.rookcard_back);

        oneScore = (TextView) activity.findViewById(R.id.textView_HumanTotalPoints);
        twoScore = (TextView) activity.findViewById(R.id.textView_OpponentONETotalPoints);
        threeScore = (TextView) activity.findViewById(R.id.textView_OpponentTWOTotalPoints);
        fourScore = (TextView) activity.findViewById(R.id.textView_OpponentTHREETotalPoints);
    }

    public void updateGUI(RookState s) {
        if (s.getSubStage() == BID) {
            nest1.setVisibility(View.INVISIBLE);
            nest2.setVisibility(View.INVISIBLE);
            nest3.setVisibility(View.INVISIBLE);
            nest4.setVisibility(View.INVISIBLE);
            nest5.setVisibility(View.INVISIBLE);
            confirmNest.setVisibility(View.INVISIBLE);
            trumpBlack.setVisibility(View.INVISIBLE);
            trumpGreen.setVisibility(View.INVISIBLE);
            trumpRed.setVisibility(View.INVISIBLE);
            trumpYellow.setVisibility(View.INVISIBLE);
            confirmTrump.setVisibility(View.INVISIBLE);
            previousBid.setText("" + state.getHighestBid());
            int possBid = state.getHighestBid() + 5;
            bidAmount.setText("" + possBid);
            trumpAccounce.setVisibility(View.VISIBLE);

            addFive.setVisibility(View.VISIBLE);
            minusFive.setVisibility(View.VISIBLE);
            bidButton.setVisibility(View.VISIBLE);
            passButton.setVisibility(View.VISIBLE);
            previousBid.setVisibility(View.VISIBLE);
            bidAmount.setVisibility(View.VISIBLE);
            lastBidder.setVisibility(View.VISIBLE);
            bidTitle.setVisibility(View.VISIBLE);
            amountTitle.setVisibility(View.VISIBLE);
            bidShow.setVisibility(View.VISIBLE);
            bidMainTitle.setVisibility(View.VISIBLE);
            yourBid.setVisibility(View.VISIBLE);
            passTitle.setVisibility(View.VISIBLE);

            passOne.setVisibility(View.INVISIBLE);
            passTwo.setVisibility(View.INVISIBLE);
            passThree.setVisibility(View.INVISIBLE);
            passFour.setVisibility(View.INVISIBLE);

            Log.i("UpdateGUI", "Trying to update lastBidder");
            if (state.lastBidder == 0) {
                lastBidder.setText("Player 1");
            } else if (state.lastBidder == 1) {
                lastBidder.setText("Player 2");
            } else if (state.lastBidder == 2) {
                lastBidder.setText("Player 3");
            } else if (state.lastBidder == 3) {
                lastBidder.setText("Player 4");
            }

            if(state.pass[0])
            {
                passOne.setVisibility(View.VISIBLE);
            }

            if(state.pass[1])
            {
                passTwo.setVisibility(View.VISIBLE);
            }

            if(state.pass[2])
            {
                passThree.setVisibility(View.VISIBLE);
            }

            if(state.pass[3])
            {
                passFour.setVisibility(View.VISIBLE);
            }

            setOrangeStarIndicator();

        } else if (s.getSubStage() == WAIT) {
            bidButton.setVisibility(View.INVISIBLE);
            passButton.setVisibility(View.INVISIBLE);
            minusFive.setVisibility(View.INVISIBLE);
            addFive.setVisibility(View.INVISIBLE);
            bidTitle.setVisibility(View.INVISIBLE);
            lastBidder.setVisibility(View.INVISIBLE);
            amountTitle.setVisibility(View.INVISIBLE);
            bidAmount.setVisibility(View.INVISIBLE);
            bidShow.setVisibility(View.INVISIBLE);
            bidMainTitle.setVisibility(View.INVISIBLE);
            yourBid.setVisibility(View.INVISIBLE);
            passOne.setVisibility(View.INVISIBLE);
            passTwo.setVisibility(View.INVISIBLE);
            passThree.setVisibility(View.INVISIBLE);
            passFour.setVisibility(View.INVISIBLE);

            setOrangeStarIndicator();

        } else if (s.getSubStage() == NEST) {
            if(state.getActivePlayer() == this.playerNum) {
                nest1.setVisibility(View.VISIBLE);
                nest2.setVisibility(View.VISIBLE);
                nest3.setVisibility(View.VISIBLE);
                nest4.setVisibility(View.VISIBLE);
                nest5.setVisibility(View.VISIBLE);
                confirmNest.setVisibility(View.VISIBLE);
            }
            trumpBlack.setVisibility(View.INVISIBLE);
            trumpGreen.setVisibility(View.INVISIBLE);
            trumpRed.setVisibility(View.INVISIBLE);
            trumpYellow.setVisibility(View.INVISIBLE);
            confirmTrump.setVisibility(View.INVISIBLE);
            addFive.setVisibility(View.INVISIBLE);
            minusFive.setVisibility(View.INVISIBLE);
            bidButton.setVisibility(View.INVISIBLE);
            passButton.setVisibility(View.INVISIBLE);
            bidButton.setVisibility(View.INVISIBLE);
            passButton.setVisibility(View.INVISIBLE);
            minusFive.setVisibility(View.INVISIBLE);
            addFive.setVisibility(View.INVISIBLE);
            bidTitle.setVisibility(View.INVISIBLE);
            lastBidder.setVisibility(View.INVISIBLE);
            amountTitle.setVisibility(View.INVISIBLE);
            bidAmount.setVisibility(View.INVISIBLE);
            bidShow.setVisibility(View.INVISIBLE);
            bidMainTitle.setVisibility(View.INVISIBLE);
            yourBid.setVisibility(View.INVISIBLE);

            passTitle.setVisibility(View.INVISIBLE);
            passOne.setVisibility(View.INVISIBLE);
            passTwo.setVisibility(View.INVISIBLE);
            passThree.setVisibility(View.INVISIBLE);
            passFour.setVisibility(View.INVISIBLE);

            //makes sure tahat cards in HumanPlayer's hand doesn't disappear during
            //Nest-phase of game
            card0.setVisibility(View.VISIBLE);
            card1.setVisibility(View.VISIBLE);
            card2.setVisibility(View.VISIBLE);
            card2.setVisibility(View.VISIBLE);
            card3.setVisibility(View.VISIBLE);
            card4.setVisibility(View.VISIBLE);
            card5.setVisibility(View.VISIBLE);
            card6.setVisibility(View.VISIBLE);
            card7.setVisibility(View.VISIBLE);
            card8.setVisibility(View.VISIBLE);

            correctNestImage();
            setOrangeStarIndicator();

        } else if (s.getSubStage() == TRUMP) {
            nest1.setVisibility(View.INVISIBLE);
            nest2.setVisibility(View.INVISIBLE);
            nest3.setVisibility(View.INVISIBLE);
            nest4.setVisibility(View.INVISIBLE);
            nest5.setVisibility(View.INVISIBLE);
            confirmNest.setVisibility(View.INVISIBLE);
            if(state.getActivePlayer() == this.playerNum) {
                trumpBlack.setVisibility(View.VISIBLE);
                trumpGreen.setVisibility(View.VISIBLE);
                trumpRed.setVisibility(View.VISIBLE);
                trumpYellow.setVisibility(View.VISIBLE);
                confirmTrump.setVisibility(View.VISIBLE);
            }
            addFive.setVisibility(View.INVISIBLE);
            minusFive.setVisibility(View.INVISIBLE);
            bidButton.setVisibility(View.INVISIBLE);
            passButton.setVisibility(View.INVISIBLE);

            setOrangeStarIndicator();
        } else
        {

            if (state.getTrump() == 0)
            {
                trumpAccounce.setText("Black");
            }
            else if (state.getTrump() == 1)
            {
                trumpAccounce.setText("Yellow");
            }
            else if (state.getTrump() == 2)
            {
                trumpAccounce.setText("Green");
            }
            else if (state.getTrump() == 3)
            {
                trumpAccounce.setText("Red");
            }

            nest1.setVisibility(View.INVISIBLE);
            nest2.setVisibility(View.INVISIBLE);
            nest3.setVisibility(View.INVISIBLE);
            nest4.setVisibility(View.INVISIBLE);
            nest5.setVisibility(View.INVISIBLE);
            confirmNest.setVisibility(View.INVISIBLE);
            trumpBlack.setVisibility(View.INVISIBLE);
            trumpGreen.setVisibility(View.INVISIBLE);
            trumpRed.setVisibility(View.INVISIBLE);
            trumpYellow.setVisibility(View.INVISIBLE);
            confirmTrump.setVisibility(View.INVISIBLE);
            addFive.setVisibility(View.INVISIBLE);
            minusFive.setVisibility(View.INVISIBLE);
            bidButton.setVisibility(View.INVISIBLE);
            passButton.setVisibility(View.INVISIBLE);

            winningBidder.setText("Player: " + state.winningPlayer);
            winningBid.setText("" + state.winningBid);
            if(state.currTrick.size() > 0)
            {
                correctTrickImage();
            }
            trick1.setVisibility(View.VISIBLE);
            trick2.setVisibility(View.VISIBLE);
            trick3.setVisibility(View.VISIBLE);
            trick4.setVisibility(View.VISIBLE);

            oneScore.setText("" + state.getScore(0));
            twoScore.setText("" + state.getScore(1));
            threeScore.setText("" + state.getScore(2));
            fourScore.setText("" + state.getScore(3));

            if (state.currTrick.size() == 4)
            {
                correctTrickImage();

//                trick1.setImageResource(R.drawable.rookcard_back);
//                trick2.setImageResource(R.drawable.rookcard_back);
//                trick3.setImageResource(R.drawable.rookcard_back);
//                trick4.setImageResource(R.drawable.rookcard_back);
            }

            setOrangeStarIndicator();

        }
    }

    /**
     * check to see which player's turn it is during bid-phase, nest-phase,
     * trump-phase, or trick-phase to place an orange star next to the current
     * active player's name to indicate to the human player whose turn it is
     */
    public void setOrangeStarIndicator() {
//        int playerNumInConfig;
//        if (state.)

        if (state.getActivePlayer() == 0 && !state.pass[0]) {
            humanOrangeStar.setVisibility(View.VISIBLE);
            opponentOneOrangeStar.setVisibility(View.INVISIBLE);
            opponentTwoOrangeStar.setVisibility(View.INVISIBLE);
            opponentThreeOrangeStar.setVisibility(View.INVISIBLE);
        }
        else if (state.getActivePlayer() == 1 && !state.pass[1]) {
            humanOrangeStar.setVisibility(View.INVISIBLE);
            opponentOneOrangeStar.setVisibility(View.VISIBLE);
            opponentTwoOrangeStar.setVisibility(View.INVISIBLE);
            opponentThreeOrangeStar.setVisibility(View.INVISIBLE);
        }
        else if (state.getActivePlayer() == 2 && !state.pass[2]) {
            humanOrangeStar.setVisibility(View.INVISIBLE);
            opponentOneOrangeStar.setVisibility(View.INVISIBLE);
            opponentTwoOrangeStar.setVisibility(View.VISIBLE);
            opponentThreeOrangeStar.setVisibility(View.INVISIBLE);
        }
        else if (state.getActivePlayer() == 3 && !state.pass[3]) {
            humanOrangeStar.setVisibility(View.INVISIBLE);
            opponentOneOrangeStar.setVisibility(View.INVISIBLE);
            opponentTwoOrangeStar.setVisibility(View.INVISIBLE);
            opponentThreeOrangeStar.setVisibility(View.VISIBLE);
        }
    }

    public void checkCurrentSizeOfOpponentHand(int opponentIdx) {
        if(opponentIdx == 1) {
            if(state.playerOneHand.size() < 9) {
                // PUT STUFF IN HEREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
                // FOR CHECKING SIZE OF ARRAY - let display of opponent hands reflect size of array
            }
        }
    }

    public void correctHandImage() {
        // gets an array of all card objects
        ImageButton[] card = {card0, card1, card2, card3, card4, card5, card6, card7, card8};

        // the card that we're looking for is being stored
        Card getting;
        if(card.length != 0) {
            for (int i = 0; i < state.playerHands[state.getActivePlayer()].size(); i++) {
                // gets the card we're looking for. [0] = only player one.
                // for future human players, needs to be another look per player
                if (state.playerHands[0].get(i) != null) {
                    getting = state.playerHands[0].get(i);
                    int currSuit = getting.getSuit();
                    int currVal = getting.getNumValue();

                    Bitmap tempBitmap;

                    if (currVal == 15) {
                        tempBitmap = cardImages[4][0];
                    } else {
                        tempBitmap = cardImages[currSuit][currVal - 5];
                    }
                    card[i].setImageBitmap(tempBitmap);
                }
            }
        }

    }

    public void correctTrickImage() {
        // gets an array of all card objects
        ImageView[] trick = {trick1, trick2, trick3, trick4};

        // the card that we're looking for is being stored
        Card getting;
        int currSuit = 5;
        int currVal = 10;
        if(state.currTrick.size() != 0){
        for (int i = 0; i < state.currTrick.size(); i++) {
            // gets the card we're looking for. [0] = only player one.
            // for future human players, needs to be another look per player
            if (state.currTrick.get(i) != null) {
                getting = state.currTrick.get(i);
                currSuit = getting.getSuit();
                currVal = getting.getNumValue();
                Bitmap tempBitmap;

                if (currVal == 15) {
                    tempBitmap = cardImages[4][0];
                } else {
                    tempBitmap = cardImages[currSuit][currVal - 5];
                }
                trick[i].setImageBitmap(tempBitmap);
                trick[i].invalidate();
            }

        }
    }
    }

    public void correctNestImage()
    {
        if (state.currTrickWinner != 0)
        {
            return;
        }
        else {
        // gets an array of all card objects
        ImageButton[] nesty = {nest1, nest2, nest3, nest4, nest5};

        // the card that we're looking for is being stored
        Card getting;
        if(nesty.length != 0) {
            for (int i = 0; i < nesty.length; i++) {
                // gets the card we're looking for. [0] = only player one.
                // for future human players, needs to be another look per player
                if (state.nest.get(i) != null) {
                    getting = state.nest.get(i);
                    int currSuit = getting.getSuit();
                    int currVal = getting.getNumValue();

                    Bitmap tempBitmap;

                    if (currVal == 15) {
                        tempBitmap = cardImages[4][0];
                    } else {
                        tempBitmap = cardImages[currSuit][currVal - 5];
                    }
                    nesty[i].setImageBitmap(tempBitmap);
                }
            }
        }

    }}

    public void onClick(View v) {
        int myNum = 0;
        int myNum2 = 0;
        if (v == quit) {
            activity.finish();
            System.exit(0);
        } else if (v == start) {
            SystemClock.sleep(2000);
            //updateGUI(state);
            correctHandImage();
            start.setVisibility(View.INVISIBLE);

            nest1.setVisibility(View.INVISIBLE);
            nest2.setVisibility(View.INVISIBLE);
            nest3.setVisibility(View.INVISIBLE);
            nest4.setVisibility(View.INVISIBLE);
            nest5.setVisibility(View.INVISIBLE);
            confirmNest.setVisibility(View.INVISIBLE);
            trumpBlack.setVisibility(View.INVISIBLE);
            trumpGreen.setVisibility(View.INVISIBLE);
            trumpRed.setVisibility(View.INVISIBLE);
            trumpYellow.setVisibility(View.INVISIBLE);
            confirmTrump.setVisibility(View.INVISIBLE);
            previousBid.setText("" + state.getHighestBid());
            int possBid = state.getHighestBid() + 5;
            bidAmount.setText("" + possBid);
            trumpAccounce.setVisibility(View.VISIBLE);

            addFive.setVisibility(View.VISIBLE);
            minusFive.setVisibility(View.VISIBLE);
            bidButton.setVisibility(View.VISIBLE);
            passButton.setVisibility(View.VISIBLE);
            previousBid.setVisibility(View.VISIBLE);
            bidAmount.setVisibility(View.VISIBLE);
            lastBidder.setVisibility(View.VISIBLE);
            bidTitle.setVisibility(View.VISIBLE);
            amountTitle.setVisibility(View.VISIBLE);
            bidShow.setVisibility(View.VISIBLE);
            bidMainTitle.setVisibility(View.VISIBLE);
            yourBid.setVisibility(View.VISIBLE);
            passTitle.setVisibility(View.VISIBLE);

        } else if (v == card0) {
            if (state.getSubStage() == NEST && !(fromH.contains(state.playerHands[state.getActivePlayer()].get(0))) && fromH.size() < 5) {
                fromH.add(state.playerHands[state.getActivePlayer()].get(0));
                card0.setAlpha(100);
            } else if (state.getSubStage() == WAIT || state.getSubStage() == BID || state.getSubStage() == TRUMP) {
            } else if(state.getSubStage() == PLAY){
                game.sendAction(new RookCardAction(this, 0));
                card0.setVisibility(View.INVISIBLE);

            }
        } else if (v == card1) {
            if (state.getSubStage() == NEST && !(fromH.contains(state.playerHands[state.getActivePlayer()].get(1))) && fromH.size() < 5) {
                fromH.add(state.playerHands[state.getActivePlayer()].get(1));
                card1.setAlpha(100);
            } else if (state.getSubStage() == WAIT || state.getSubStage() == BID || state.getSubStage() == TRUMP) {
            } else if(state.getSubStage() == PLAY){

                game.sendAction(new RookCardAction(this, 1));
                card1.setVisibility(View.INVISIBLE);

            }
        } else if (v == card2) {
            if (state.getSubStage() == NEST && !(fromH.contains(state.playerHands[state.getActivePlayer()].get(2))) && fromH.size() < 5) {
                fromH.add(state.playerHands[state.getActivePlayer()].get(2));
                card2.setAlpha(100);
            } else if (state.getSubStage() == WAIT || state.getSubStage() == BID || state.getSubStage() == TRUMP) {
            } else if(state.getSubStage() == PLAY){

                game.sendAction(new RookCardAction(this, 2));
                card2.setVisibility(View.INVISIBLE);

            }
        } else if (v == card3) {
            if (state.getSubStage() == NEST && !(fromH.contains(state.playerHands[state.getActivePlayer()].get(3))) && fromH.size() < 5) {
                fromH.add(state.playerHands[state.getActivePlayer()].get(3));
                card3.setAlpha(100);
            } else if (state.getSubStage() == WAIT || state.getSubStage() == BID || state.getSubStage() == TRUMP) {
            } else if(state.getSubStage() == PLAY){

                game.sendAction(new RookCardAction(this, 3));
                card3.setVisibility(View.INVISIBLE);

            }
        } else if (v == card4) {
            if (state.getSubStage() == NEST && !(fromH.contains(state.playerHands[state.getActivePlayer()].get(4))) && fromH.size() < 5) {
                fromH.add(state.playerHands[state.getActivePlayer()].get(4));
                card4.setAlpha(100);
            } else if (state.getSubStage() == WAIT || state.getSubStage() == BID || state.getSubStage() == TRUMP) {
            } else if(state.getSubStage() == PLAY){

                game.sendAction(new RookCardAction(this, 4));
                card4.setVisibility(View.INVISIBLE);

            }
        } else if (v == card5) {
            if (state.getSubStage() == NEST && !(fromH.contains(state.playerHands[state.getActivePlayer()].get(5))) && fromH.size() < 5) {
                fromH.add(state.playerHands[state.getActivePlayer()].get(5));
                card5.setAlpha(100);
            } else if (state.getSubStage() == WAIT || state.getSubStage() == BID || state.getSubStage() == TRUMP) {
            } else if(state.getSubStage() == PLAY){

                game.sendAction(new RookCardAction(this, 5));
                card5.setVisibility(View.INVISIBLE);

            }
        } else if (v == card6) {
            if (state.getSubStage() == NEST && !(fromH.contains(state.playerHands[state.getActivePlayer()].get(6))) && fromH.size() < 5) {
                fromH.add(state.playerHands[state.getActivePlayer()].get(6));
                card6.setAlpha(100);
            } else if (state.getSubStage() == WAIT || state.getSubStage() == BID || state.getSubStage() == TRUMP) {
            } else if(state.getSubStage() == PLAY){

                game.sendAction(new RookCardAction(this, 6));
                card6.setVisibility(View.INVISIBLE);

            }
        } else if (v == card7) {
            if (state.getSubStage() == NEST && !(fromH.contains(state.playerHands[state.getActivePlayer()].get(7))) && fromH.size() < 5) {
                fromH.add(state.playerHands[state.getActivePlayer()].get(7));
                card7.setAlpha(100);
            } else if (state.getSubStage() == WAIT || state.getSubStage() == BID || state.getSubStage() == TRUMP) {
            } else if(state.getSubStage() == PLAY){

                game.sendAction(new RookCardAction(this, 7));
                card7.setVisibility(View.INVISIBLE);

            }
        } else if (v == card8) {
            if (state.getSubStage() == NEST && !(fromH.contains(state.playerHands[state.getActivePlayer()].get(8))) && fromH.size() < 5) {
                fromH.add(state.playerHands[state.getActivePlayer()].get(8));
                card8.setAlpha(100);
            } else if (state.getSubStage() == WAIT || state.getSubStage() == BID || state.getSubStage() == TRUMP) {
            } else if(state.getSubStage() == PLAY){

                game.sendAction(new RookCardAction(this, 8));
                card8.setVisibility(View.INVISIBLE);

            }
        } else if (v == nest1) {
            nest1.setAlpha(100);
            fromN.add(state.nest.get(0));
            game.sendAction(new RookCardAction(this, 0));
        } else if (v == nest2) {
            nest2.setAlpha(100);
            fromN.add(state.nest.get(1));
            game.sendAction(new RookCardAction(this, 1));
        } else if (v == nest3) {
            nest3.setAlpha(100);
            fromN.add(state.nest.get(2));
            game.sendAction(new RookCardAction(this, 2));
        } else if (v == nest4) {
            nest4.setAlpha(100);
            fromN.add(state.nest.get(3));
            game.sendAction(new RookCardAction(this, 3));
        } else if (v == nest5) {
            nest5.setAlpha(100);
            fromN.add(state.nest.get(4));
            game.sendAction(new RookCardAction(this, 3));
        } else if (v == trumpBlack) {
            trumpColor = BLACK;
            trumpAccounce.setText("Black");
        } else if (v == trumpYellow) {
            trumpColor = YELLOW;
            trumpAccounce.setText("Yellow");
        } else if (v == trumpGreen) {
            trumpColor = GREEN;
            trumpAccounce.setText("Green");
        } else if (v == trumpRed) {
            trumpColor = RED;
            trumpAccounce.setText("Red");
        } else if (v == addFive) {
            try {
                myNum = Integer.parseInt(bidAmount.getText().toString());
            } catch (NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }

            if (myNum < 120) {
                myNum += 5;
            }
            bidAmount.setText("" + myNum);
        } else if (v == minusFive) {

            try {
                myNum = Integer.parseInt(bidAmount.getText().toString());
                myNum2 = Integer.parseInt(previousBid.getText().toString());
            } catch (NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }

            if (myNum > (myNum2 + 5)) {
                myNum -= 5;
            }

            bidAmount.setText("" + myNum);
        } else if (v == confirmNest) {
            if (fromH.size() == fromN.size()) {
                game.sendAction(new RookNestAction(this, fromN, fromH));
                correctHandImage();

                card0.setAlpha(255);
                card1.setAlpha(255);
                card2.setAlpha(255);
                card3.setAlpha(255);
                card4.setAlpha(255);
                card5.setAlpha(255);
                card6.setAlpha(255);
                card7.setAlpha(255);
                card8.setAlpha(255);
            }

        } else if (v == confirmTrump && trumpColor > -1) {
            game.sendAction(new RookTrumpAction(this, trumpColor));

//            trumpBlack.setVisibility(View.INVISIBLE);
//            trumpGreen.setVisibility(View.INVISIBLE);
//            trumpYellow.setVisibility(View.INVISIBLE);
//            trumpRed.setVisibility(View.INVISIBLE);
//            trumpTitle.setVisibility(View.INVISIBLE);
//            confirmTrump.setVisibility(View.INVISIBLE);

        } else if (v == bidButton) {
            try {
                myNum = Integer.parseInt(bidAmount.getText().toString());
            } catch (NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }
            game.sendAction(new RookBidAction(this, myNum));
            previousBid.setText("" + myNum);

        } else if (v == passButton) {
            game.sendAction(new RookHoldAction(this));
        }
    }

    /**
     * @return tells whether the animation should be terminated
     */
    public boolean doQuit() {
        return false;
    }

    /**
     * @return tells whether the animation should be paused
     */
    public boolean doPause() {
        return false;
    }

    /**
     * redraw the screen image:
     * - the nest, with all five cards face down
     * - the four player's hands, with only the user's hand visible for them, their opponents
     * hands will be face down
     * - a token to indicate whose turn it is
     *
     * @param g the canvas being drawn on
     */

    public void tick(Canvas g) {
        // ignore if there is no game state
        if (state == null) {
            return;
        } else {
            return;
        }
    }

    public void onTouch(MotionEvent event) {
        if (event.equals(quit)) {
            activity.finish();
            System.exit(0);
        }
    }

    /**
     * @return the animation interval, in milliseconds
     */
    public int interval() {
        // 1/20 of a second
        return 50;
    }


    public void score() {
        int points = state.countTrick();
        Card first = state.currTrick.get(0);
        int firstCardVal = first.getNumValue();
        int firstCardSuit = first.getSuit();
        int currTrump = state.getTrump();
        Card domCard;

        int trickWinner = 0;
        int ranking = 1000;
        int counter = 0;
        for (Card cards : state.currTrick) {
            if (cards.getNumValue() == 15) {
                trickWinner = counter;
                ranking = 1;
            } else if (cards.getSuit() == currTrump && cards.getNumValue() == 14) {
                if (ranking > 1) {
                    trickWinner = counter;
                    counter++;
                    ranking = 2;
                }
            } else if (cards.getSuit() == currTrump && cards.getNumValue() == 13) {
                if (ranking > 2) {
                    trickWinner = counter;
                    counter++;
                    ranking = 3;
                }
            } else if (cards.getSuit() == currTrump && cards.getNumValue() == 12) {
                if (ranking > 3) {
                    trickWinner = counter;
                    counter++;
                    ranking = 4;
                }
            } else if (cards.getSuit() == currTrump && cards.getNumValue() == 11) {
                if (ranking > 4) {
                    trickWinner = counter;
                    counter++;
                    ranking = 5;
                }
            } else if (cards.getSuit() == currTrump && cards.getNumValue() == 10) {
                if (ranking > 5) {
                    trickWinner = counter;
                    counter++;
                    ranking = 6;
                }
            } else if (cards.getSuit() == currTrump && cards.getNumValue() == 9) {
                if (ranking > 6) {
                    trickWinner = counter;
                    counter++;
                    ranking = 7;
                }
            } else if (cards.getSuit() == currTrump && cards.getNumValue() == 8) {
                if (ranking > 7) {
                    trickWinner = counter;
                    counter++;
                    ranking = 8;
                }
            } else if (cards.getSuit() == currTrump && cards.getNumValue() == 7) {
                if (ranking > 8) {
                    trickWinner = counter;
                    counter++;
                    ranking = 9;
                }
            } else if (cards.getSuit() == currTrump && cards.getNumValue() == 6) {
                if (ranking > 9) {
                    trickWinner = counter;
                    counter++;
                    ranking = 10;
                }
            } else if (cards.getSuit() == currTrump && cards.getNumValue() == 5) {
                if (ranking > 10) {
                    trickWinner = counter;
                    counter++;
                    ranking = 11;
                }
            } else if (cards.getSuit() == currTrump && cards.getNumValue() == 4) {
                if (ranking > 11) {
                    trickWinner = counter;
                    counter++;
                    ranking = 12;
                }
            } else if (cards.getSuit() == firstCardSuit && cards.getNumValue() == 14) {
                if (ranking > 12) {
                    trickWinner = counter;
                    counter++;
                    ranking = 13;
                }
            } else if (cards.getSuit() == firstCardSuit && cards.getNumValue() == 13) {
                if (ranking > 13) {
                    trickWinner = counter;
                    counter++;
                    ranking = 14;
                }
            } else if (cards.getSuit() == firstCardSuit && cards.getNumValue() == 12) {
                if (ranking > 14) {
                    trickWinner = counter;
                    counter++;
                    ranking = 15;
                }
            } else if (cards.getSuit() == firstCardSuit && cards.getNumValue() == 11) {
                if (ranking > 15) {
                    trickWinner = counter;
                    counter++;
                    ranking = 16;
                }
            } else if (cards.getSuit() == firstCardSuit && cards.getNumValue() == 10) {
                if (ranking > 16) {
                    trickWinner = counter;
                    counter++;
                    ranking = 17;
                }
            } else if (cards.getSuit() == firstCardSuit && cards.getNumValue() == 9) {
                if (ranking > 17) {
                    trickWinner = counter;
                    counter++;
                    ranking = 18;
                }
            } else if (cards.getSuit() == firstCardSuit && cards.getNumValue() == 8) {
                if (ranking > 18) {
                    trickWinner = counter;
                    counter++;
                    ranking = 19;
                }
            } else if (cards.getSuit() == firstCardSuit && cards.getNumValue() == 7) {
                if (ranking > 19) {
                    trickWinner = counter;
                    counter++;
                    ranking = 20;
                }
            } else if (cards.getSuit() == firstCardSuit && cards.getNumValue() == 6) {
                if (ranking > 20) {
                    trickWinner = counter;
                    counter++;
                    ranking = 21;
                }
            } else if (cards.getSuit() == firstCardSuit && cards.getNumValue() == 5) {
                if (ranking > 21) {
                    trickWinner = counter;
                    counter++;
                    ranking = 22;
                }
            } else if (cards.getSuit() == firstCardSuit && cards.getNumValue() == 4) {
                if (ranking > 22) {
                    trickWinner = counter;
                    counter++;
                    ranking = 23;
                }
            }
        }

        humanPoints.setText(points);
    }
}