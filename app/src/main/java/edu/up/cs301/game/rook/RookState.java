package edu.up.cs301.game.rook;

import android.graphics.Color;

import java.util.ArrayList;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.infoMsg.GameState;

import static android.telephony.PhoneNumberUtils.WAIT;

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

    public final int WAIT = 0;
    public final int BID = 1;
    public final int TRUMP = 2;
    public final int NEST = 3;
    public final int PLAY = 4;
    public final int OVER = 5;

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

        deck = initDeck();

        currTrickWinner = 0;
        trumpSuit = null;
        winningBid = 0;
        bidPass = new boolean[4];
        playerBids = new int[4];
        playerScores = new int[4];
        playerNames = new String[4];
    }

    public int getSubStage() {
        return subStage;
    }

    public void setSubStage(int sub) {
        subStage = sub;
    }

    public int getScore(int playerIdx) {
        return playerScores[playerIdx];
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

    public ArrayList<Card> initDeck(){
        ArrayList<Card> initD = new ArrayList<Card>(41);
        int colors[] = {Color.BLACK, Color.RED, Color.YELLOW, Color.GREEN};
        int numbers[] = {5, 6, 7, 8, 9, 10, 11, 12, 13, 14};

        for(int i = 0; i < colors.length; i++){
            for(int j = 0; j < numbers.length; j++){
                Card tempCard = new Card(colors[i], numbers[j]);
                initD.add(tempCard);
            }
        }
        Card Rook = new Card(Color.BLUE, 15);
        initD.add(Rook);

        initD = shuffle(initD);

        return initD;
    }

    public ArrayList<Card> shuffle(ArrayList<Card> shuf) {

        ArrayList<Card> temp = new ArrayList<Card>();
        while(!shuf.isEmpty()) {
            int loc=(int)(Math.random()*shuf.size());
            temp.add(shuf.get(loc));
            shuf.remove(loc);
        }
        shuf=temp;

        return shuf;
    }

    public void deal(){

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
        int count = 0;
        int maxVal = 0;
        for(int i = 0; i<4; i++){
            if(bidPass[i]){
                count++;
            }
        }

        if(count >= 3){
            for(int j = 0; j<4; j++){
                if(playerBids[j] > maxVal){
                    maxVal = playerBids[j];
                }
            }
            winningBid = maxVal;
        }
    }

    public int countTrick() {
        int trickVal = 0;
        for(int i = 0; i<4; i++){
            trickVal += currTrick.get(i).counterValue;
        }
        return trickVal;
    }

    public void useNest(ArrayList<Card> fromNest, ArrayList<Card> fromHand, ArrayList<Card> playerHand) {
        for(int i = 0; i < fromNest.size(); i++){
            nest.remove(fromNest.get(i));
            playerHand.remove(fromHand.get(i));
        }

        for(int j = 0; j < fromNest.size(); j++){
            nest.add(fromHand.get(j));
            playerHand.add(fromNest.get(j));
        }
    }

    // makes all hidden ifnrmation for a player null
    public void nullHiddenInformation(GamePlayer p)
    {

    }

    public int getActivePlayer()
    {
        return currPlayer;
    }

}
