package edu.up.cs301.game.rook;

import android.graphics.Color;

import java.util.ArrayList;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.GameState;

import static android.telephony.PhoneNumberUtils.WAIT;

/**
 * Created by dewhitt17 on 11/9/2016.
 */
public class RookState extends GameState {

    private int subStage;
    private final int numPlayers = 4;
    public final int WAIT = 0;
    public final int BID = 1;
    public final int TRUMP = 2;
    public final int NEST = 3;
    public final int PLAY = 4;
    public final int OVER = 5;
    private int currPlayer;
    public ArrayList<Card>[] playerHands = (ArrayList<Card>[]) new ArrayList[4];
    public ArrayList<Card> playerZeroHand;
    public ArrayList<Card> playerOneHand;
    public ArrayList<Card> playerTwoHand;
    public ArrayList<Card> playerThreeHand;
    public ArrayList<Card> nest;
    public ArrayList<Card> currTrick;
    public ArrayList<Card> deck;

    private int currTrickWinner;
    private int trumpSuit;
    private final int BLACK = 0;
    private final int YELLOW = 1;
    private final int GREEN = 2;
    private final int RED = 3;
    private final int ROOK = 4;
    public int winningBid;
    public int winningPlayer;
    public boolean[] bidPass;
    private int[] playerBids;
    private int[] playerScores;
    private String[] playerNames;

    public RookState() {
        subStage = WAIT;
        currPlayer = 0;
        playerZeroHand = new ArrayList<Card>(9);
        playerOneHand = new ArrayList<Card>(9);
        playerTwoHand = new ArrayList<Card>(9);
        playerThreeHand = new ArrayList<Card>(9);
        playerHands[0] = playerZeroHand;
        playerHands[1] = playerOneHand;
        playerHands[2] = playerTwoHand;
        playerHands[3] = playerThreeHand;
        nest = new ArrayList<Card>(5);
        currTrick = new ArrayList<Card>(4);

        deck = initDeck();
        deal();

        currTrickWinner = 0;
        trumpSuit = 0;
        winningBid = 0;
        bidPass = new boolean[numPlayers];
        playerBids = new int[numPlayers];
        playerScores = new int[numPlayers];
        playerNames = new String[numPlayers];
        winningPlayer = 0;
    }

    public RookState(GameInfo info) {
        RookState temp = (RookState) info;

        subStage = temp.subStage;
        currPlayer = temp.currPlayer;
        playerZeroHand = temp.playerZeroHand;
        playerOneHand = temp.playerOneHand;
        playerTwoHand = temp.playerTwoHand;
        playerThreeHand = temp.playerThreeHand;
        playerHands[0] = playerZeroHand;
        playerHands[1] = playerOneHand;
        playerHands[2] = playerTwoHand;
        playerHands[3] = playerThreeHand;
        nest = temp.nest;
        currTrick = temp.currTrick;

        deck = temp.deck;

        currTrickWinner = temp.currTrickWinner;
        trumpSuit = temp.trumpSuit;
        winningBid = temp.winningBid;
        bidPass = temp.bidPass;
        playerBids = temp.playerBids;
        playerScores = temp.playerScores;
        playerNames = temp.playerNames;
        winningPlayer = temp.winningPlayer;

    }

    public int getSubStage() {
        return subStage;
    }

    public void setSubStage(int sub) {
        subStage = sub;
    }

    public void setTrump(int trumpColor) {
        trumpSuit = trumpColor;
    }

    public int getScore(int playerIdx) {
        return playerScores[playerIdx];
    }

    public void setScore(int score, int index) {
        playerScores[index] = score;
    }

    public void setBid(int bid, int player) {
        playerBids[player] = bid;
    }

    public int[] getBids() {
        return playerBids;
    }

    public int getHighestBid(){
        int highTemp= 50;
        for(int i = 0; i<4; i++){
            if(playerBids[i] > highTemp){
                highTemp = playerBids[i];
            }
        }
        return highTemp;
    }

    public void addCard(Card c, ArrayList<Card> cardPile) {
        cardPile.add(c);
    }

    public void removeCard(Card c, ArrayList<Card> cardPile) {
        cardPile.remove(c);
    }

    public ArrayList<Card> initDeck() {
        ArrayList<Card> initD = new ArrayList<Card>(41);
        int colors[] = {BLACK, RED, YELLOW, GREEN};
        int numbers[] = {5, 6, 7, 8, 9, 10, 11, 12, 13, 14};

        for (int i = 0; i < colors.length; i++) {
            for (int j = 0; j < numbers.length; j++) {
                Card tempCard = new Card(colors[i], numbers[j]);
                initD.add(tempCard);
            }
        }
        Card Rook = new Card(ROOK, 15);
        initD.add(Rook);

        initD = shuffle(initD);

        return initD;
    }

    public ArrayList<Card> shuffle(ArrayList<Card> shuf) {

        ArrayList<Card> temp = new ArrayList<Card>();
        while (!shuf.isEmpty()) {
            int loc = (int) (Math.random() * shuf.size());
            temp.add(shuf.get(loc));
            shuf.remove(loc);
        }
        shuf = temp;

        return shuf;
    }

    public void deal() {

        for (int j = 0; j < numPlayers; j++) {
            for (int i = 0; i < 9; i++) {
                Card temp = deck.get(0);
                playerHands[j].add(temp);
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

    public boolean finalizeBids() {
        int count = 0;
        int maxVal = 0;
        for (int i = 0; i < numPlayers; i++) {
            if (bidPass[i]) {
                count++;
            }
        }

        if (count >= 3) {
            for (int j = 0; j < numPlayers; j++) {
                if (playerBids[j] > maxVal) {
                    maxVal = playerBids[j];
                    winningPlayer = j;
                }
            }
            winningBid = maxVal;
            setPlayer(winningPlayer);
            return true;
        } else {
            return false;
        }
    }

    public int countTrick() {
        int trickVal = 0;
        for (int i = 0; i < numPlayers; i++) {
            trickVal += currTrick.get(i).counterValue;
        }
        return trickVal;
    }

    public void useNest(ArrayList<Card> fromNest, ArrayList<Card> fromHand, ArrayList<Card> playerHand) {
        for (int i = 0; i < fromNest.size(); i++) {
            nest.remove(fromNest.get(i));
            playerHand.remove(fromHand.get(i));
        }

        for (int j = 0; j < fromNest.size(); j++) {
            nest.add(fromHand.get(j));
            playerHand.add(fromNest.get(j));
        }
    }

    public void setHold(int playerIndex) {
        bidPass[playerIndex] = true;
    }

    // makes all hidden information for a player null
    public void nullHiddenInformation(int playerIdx) {
        for (int i = 0; i < numPlayers; i++) {
            if (i != playerIdx) {
                playerHands[i].clear();
                for (int j = 0; j < 9; j++) {
                    playerHands[i].add(null);
                }
            }
        }
    }

    // returns the active player
    public int getActivePlayer() {
        return currPlayer;
    }

    public void setPlayer(int playIdx) {
        currPlayer = playIdx;
    }

    public void setPlayer() {
        if (currPlayer == 3) {
            currPlayer = 0;
        } else {
            currPlayer++;
        }
    }

}
