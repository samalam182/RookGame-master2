package edu.up.cs301.slapjack;

import android.graphics.Color;

import java.util.ArrayList;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.infoMsg.GameState;

/**
 * Created by dewhitt17 on 11/9/2016.
 */
public class RookState extends GameState{

    private int subStage;
    private int currPlayer;
    public ArrayList<Card> playerZeroHand;
    public ArrayList<Card> playerOneHand;
    public ArrayList<Card> playerTwoHand;
    public ArrayList<Card> playerThreeHand;
    public ArrayList<Card> nest;
    public ArrayList<Card> currTrick;

    public ArrayList<Card> deck;

    private int currTrickWinner;
    private Color trumpSuit;
    public int winningBid;
    public boolean[] bidPass;
    private int[] playerBids;
    private int[] playerScores;
    private String[] playerNames;

    public RookState() {
        subStage = 0;
        currPlayer = 0;
        playerZeroHand = new ArrayList<Card>(9);
        playerOneHand = new ArrayList<Card>(9);
        playerTwoHand = new ArrayList<Card>(9);
        playerThreeHand = new ArrayList<Card>(9);
        nest = new ArrayList<Card>(5);
        currTrick = new ArrayList<Card>(4);

        deck = new ArrayList<Card>(41);

        currTrickWinner = 0;
        trumpSuit = null;
        winningBid = 0;
        bidPass = new boolean[4];
        playerBids = new int[4];
        playerScores = new int[4];
        playerNames = new String[4];
    }

    public int getSubStage() {
        return 0;
    }

    public void setSubStage(int sub) {
        subStage = sub;
    }

    public int getScore() {
        return 0;
    }

    public void setScore(int score, int index) {
        playerScores[index] = score;
    }

    ///////////////////////

    public void addCard(Card c, ArrayList<Card> cardPile) {
        cardPile.add(c);
    }
    public void removeCard(Card c, ArrayList<Card> cardPile) {
        cardPile.remove(c);
    }
    public void shuffle() {
        for(int j = 0; j<4; j++) {
            for (int i = 0; i < 9; i++) {
                Card temp = deck.get(0);
                if (j == 0) {
                    playerZeroHand.add(temp);
                }
                if (j == 1) {
                    playerOneHand.add(temp);
                }
                if (j == 2) {
                    playerTwoHand.add(temp);
                }
                if (j == 3) {
                    playerThreeHand.add(temp);
                }
                deck.remove(0);
            }
        }

        for (int k = 0; k < 5; k++) {
            Card temp = deck.get(k);

            if (k == 0) {
                nest.add(temp);
            }
        }
    }
    public void finalizeBids() {

    }
    public void countTrick() {

    }
    public void useNest() {

    }
}
