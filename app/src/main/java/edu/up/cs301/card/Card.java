package edu.up.cs301.card;

import java.io.Serializable;

//import edu.up.cs301.game.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import edu.up.cs301.game.R;

/**
 * A playing card in the standard 52-card deck. The images, which have been
 * placed in the res/drawable-hdpi folder in the project, are from
 * http://www.pdclipart.org.
 * 
 * In order to display the card-images on the android you need to call the
 *   Card.initImages(currentActivity)
 * method during initialization; the 52 image files need to be placed in the
 * res/drawable-hdpi project area.
 * 
 * @author Steven R. Vegdahl
 * @version July 2013
 */
public class Card implements Serializable {

	// to satisfy the Serializable interface
	private static final long serialVersionUID = 893542931190030342L;
	
	// instance variables: the card's rank and the suit
    public int suit;
	public int numValue;
	public int counterValue;
	public boolean beenPlayed;

    /**
     * Constructor for class card
     */
    public Card(int newSuit, int newNumValue) {
		this.suit = newSuit;
		this.numValue = newNumValue;
		beenPlayed = false;

		if(numValue == 5) {
			this.counterValue = 5;
		}
		else if(numValue == 10 || numValue == 14){
			this.counterValue = 10;
		}
		else if(numValue == 15){
			this.counterValue = 20;
		}
		else{
			this.counterValue = 0;
		}
	}

	public int getSuit() {
		return suit;
	}

	public int getNumValue() {
		return this.numValue;
	}

	public int getCounterValue() {
		return this.counterValue;
	}

	public boolean getPlayed(){ return beenPlayed; }

	public void setNumValue(int chosenNumValue) {
		this.numValue = chosenNumValue;
	}

	public void stCounterValue(int chosenCounterValue) {
		this.counterValue = chosenCounterValue;
	}

	public void setPlayed(){ beenPlayed = true; }


    /**
     * Creates a Card from a String.  (Can be used instead of the
     * constructor.)
     *
     * @param str
     * 		a two-character string representing the card, which is
     *		of the form "4C", with the first character representing the rank,
     *		and the second character representing the suit.  Each suit is
     *		denoted by its first letter.  Each single-digit rank is represented
     *		by its digit.  The letters 'T', 'J', 'Q', 'K' and 'A', represent
     *		the ranks Ten, Jack, Queen, King and Ace, respectively.
     * @return
     * 		A Card object that corresponds to the 'str' string. Returns
     *		null if 'str' has improper format.
     */
//    public static Card fromString(String str) {
//    	// check the string for being null
//        if (str == null) return null;
//
//        // trim the string; return null if length is not 2
//        str = str.trim();
//        if (str.length() !=2) return null;
//
//        // get the rank and suit corresponding to the two characters
//        // in the string
//        Rank r = Rank.fromChar(str.charAt(0));
//        Suit s = Suit.fromChar(str.charAt(1));
//
//        // if both rank and suit are non-null, create the corresponding
//        // card; if either is null, return null
//        return r==null || s == null ? null : new Card(r, s);
//    }
//
//    /**
//     * Produces a textual description of a Card.
//     *
//     * @return
//	 *		A string such as "Jack of Spades", which describes the card.
//     */
//    public String toString() {
//        return rank.longName()+" of "+suit.longName()+"s";
//    }
//
//    /**
//     * Tells whether two Card objects represent the same card.
//     *
//     * @return
//	 *		true if the two card objects represent the same card, false
//     *		otherwise.
//     */
//    public boolean equals(Card other) {
//        return this.rank == other.rank && this.suit == other.suit;
//    }
//
//    /**
//     * Draws the card on a Graphics object.  The card is drawn as a
//     * white card with a black border.  If the card's rank is numerih, the
//     * appropriate number of spots is drawn.  Otherwise the appropriate
//     * picture (e.g., of a queen) is included in the card's drawing.
//     *
//     * @param g  the graphics object on which to draw
//     * @param where  a rectangle that tells where the card should be drawn
//     */
//    public void drawOn(Canvas g, RectF where) {
//    	// create the paint object
//    	Paint p = new Paint();
//    	p.setColor(Color.BLACK);
//
//    	// get the bitmap for the card
//    	Bitmap bitmap = cardImages[this.getSuit().ordinal()][this.getRank().ordinal()];
//
//    	// create the source rectangle
//    	Rect r = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
//
//    	// draw the bitmap into the target rectangle
//    	g.drawBitmap(bitmap, r, where, p);
//    }
//
//
//    /**
//     * Gives a two-character version of the card (e.g., "TS" for ten of
//     * spades).
//     */
//    public String shortName() {
//        return "" + getRank().shortName() + getSuit().shortName();
//    }

//    /**
//     * Tells the card's rank.
//     *
//     * @return
//	 *		a Rank object (actually of a subclass) that tells the card's
//     *		rank (e.g., Jack, three).
//     */
//    public Rank getRank() {
//    	return rank;
//    }
//
//    /**
//     * Tells the card's suit.
//     *
//     * @return
//	 *		a Suit object (actually of a subclass) that tells the card's
//     *		rank (e.g., heart, club).
//     */
//    public Suit getSuit()
//    	return suit;
//    }
 
//    // array that contains the android resource indices for the 52 card
//    // images
    private static int[][] resIdx = {
    	{
    		R.drawable.rookcard_5b, R.drawable.rookcard_6b, R.drawable.rookcard_7b,
			 R.drawable.rookcard_8b, R.drawable.rookcard_9b, R.drawable.rookcard_10b,
			 R.drawable.rookcard_11b, R.drawable.rookcard_12b, R.drawable.rookcard_13b,
			 R.drawable.rookcard_14b
    	},
    	{
			R.drawable.rookcard_5y, R.drawable.rookcard_6y, R.drawable.rookcard_7y,
			R.drawable.rookcard_8y, R.drawable.rookcard_9y, R.drawable.rookcard_10y,
			R.drawable.rookcard_11y, R.drawable.rookcard_12y, R.drawable.rookcard_13y,
			R.drawable.rookcard_14y
    	},
    	{
			R.drawable.rookcard_5g, R.drawable.rookcard_6g, R.drawable.rookcard_7g,
			R.drawable.rookcard_8g, R.drawable.rookcard_9g, R.drawable.rookcard_10g,
			R.drawable.rookcard_11g, R.drawable.rookcard_12g, R.drawable.rookcard_13g,
			R.drawable.rookcard_14g
    	},
    	{
			R.drawable.rookcard_5r, R.drawable.rookcard_6r, R.drawable.rookcard_7r,
			R.drawable.rookcard_8r, R.drawable.rookcard_9r, R.drawable.rookcard_10r,
			R.drawable.rookcard_11r, R.drawable.rookcard_12r, R.drawable.rookcard_13r,
			R.drawable.rookcard_14r
    	},
		{
			R.drawable.rookcard_rook, R.drawable.rookcard_back, R.drawable.rookcard_back,
                R.drawable.rookcard_back, R.drawable.rookcard_back, R.drawable.rookcard_back,
                R.drawable.rookcard_back, R.drawable.rookcard_back, R.drawable.rookcard_back,
                R.drawable.rookcard_back,
		},
    };

    // the array of card images
    public static Bitmap[][] cardImages = null;

    /**
     * initializes the card images
     *
     * @param activity
     * 		the current activity
     */
    public static void initImages(Activity activity) {
    	// if it's already initialized, then ignore
    	if (cardImages != null) return;

    	// create the outer array
    	cardImages = new Bitmap[resIdx.length][];

    	// loop through the resource-index array, creating a
    	// "parallel" array with the images themselves
    	for (int i = 0; i < resIdx.length; i++) {
    		// create an inner array
    		cardImages[i] = new Bitmap[resIdx[i].length];
    		for (int j = 0; j < resIdx[i].length; j++) {
    			// create the bitmap from the corresponding image
    			// resource, and set the corresponding array element
    			cardImages[i][j] =
    					BitmapFactory.decodeResource(
    							activity.getResources(),
    							resIdx[i][j]);
    		}
    	}
    }

}
