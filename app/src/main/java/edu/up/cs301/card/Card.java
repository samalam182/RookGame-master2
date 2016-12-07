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
 * A playing card in the standard 41-card deck for the game of Rook. The images, which have been
 * placed in the res/drawable folder in the project, are originally made by the Rook team.
 * 
 * In order to display the card-images on the Android you need to call the
 *   Card.initImages(currentActivity)
 * method during initialization; the particular image files need to be placed in the
 * res/drawable-hdpi project area through a Bitmap array.
 * 
 * @author Steven R. Vegdahl
 * @author Sam DeWhitt, Eric Hoser, Mitchell Nguyen, Alexander Nowlin
 * @version December 2016
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

		// for all cards that have a number value of 5,
		// set their Counter-point value to 5
		if(numValue == 5) {
			this.counterValue = 5;
		}
		// for all cards that either have a number value
		// of 10 or 14, set their Counter-point value to 10
		else if(numValue == 10 || numValue == 14){
			this.counterValue = 10;
		}
		// for the Rook Card (which has a number value of 15),
		// set its Counter-point value to 20
		else if(numValue == 15){
			this.counterValue = 20;
		}
		else {
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
			R.drawable.rookcard_rook, R.drawable.rookcard_back, R.drawable.rookcard_blank,
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
