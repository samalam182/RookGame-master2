package edu.up.cs301.game.rook;

import android.graphics.Color;
import android.widget.ImageButton;

import java.io.Serializable;
import java.util.ArrayList;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.R;
import edu.up.cs301.game.config.GamePlayerType;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.GameState;

import static android.telephony.PhoneNumberUtils.WAIT;

/**
 * A GameState class for the game of Rook, which keeps track of all 4 player's
 * choices to place a card, interact with the nest, choose the trump suit, current bid,
 * and total points throughout the course of the game.
 *
 * Also incorporates a special ArrayList called "deck", which is used at the start
 * of the first round of the game to initialize a randomized combination of all 41 cards
 * that will be dealt out accordingly to each player's hand and the nest
 *
 * @author Sam DeWhitt, Eric Hoser, Mitchell Nguyen, Alexander Nowlin
 * @version December 2016
 */
public class RookState extends GameState implements Serializable{

    // used for network-play
    private static final long serialVersionUID = -8269749892027578792L;
    private static final boolean SHALLOW_COPY = false;

    // tracks the current stage throughout the game
    private int subStage;

    // defines the specific number of players of the game
    private final int numPlayers = 4;

    // defines which stage of the game that a particular
    // player is currently in
    public final int WAIT = 0;
    public final int BID = 1;
    public final int TRUMP = 2;
    public final int NEST = 3;
    public final int PLAY = 4;
    public final int OVER = 5;

    // defines the index of the current player who during
    // any stage of the game
    private int currPlayer;

    // defines the index of the current player who won
    // the bid of the current round
    public int currTrickWinner;

    // ArrayLists will hold all cards that are part of
    // each player's hands, the nest, and the current trick
    public ArrayList<Card>[] playerHands = (ArrayList<Card>[]) new ArrayList[4];
    public ArrayList<Card> playerZeroHand;
    public ArrayList<Card> playerOneHand;
    public ArrayList<Card> playerTwoHand;
    public ArrayList<Card> playerThreeHand;
    public ArrayList<Card> nest;
    public ArrayList<Card> currTrick;

    // will hold all the cards, which will be randomly dealt out
    // to all player hands and nest in constructor-method
    public ArrayList<Card> deck;

    // defines the chosen trump-suit by the current winner of the bid
    private int trumpSuit;

    // defines the different suits of each card
    private final int BLACK = 0;
    private final int RED = 1;
    private final int YELLOW = 2;
    private final int GREEN = 3;
    private final int ROOK = 4;

    // defines the winning bid amount and the player who won the bid
    public int winningBid;
    public int winningPlayer;

    // defines the player who previously bid
    public int lastBidder = 0;

    // defines which players either still have the opportunity to bid
    // or have already passed during each round
    public boolean[] pass;

    // defines all the players' bid amounts
    private int[] playerBids = new int[numPlayers];

    // keeps track of all the total scores of each player throughout game
    private int[] playerScores;

    // defines the inputted names for each player according
    // to the configuration screen
    private String[] playerNames;

    // informs all players that a new round has started by creating a "new-game-state"
    public boolean newState;

    // used to set all points that were earned back to zero when a new round starts
    public int[] pointsThisRound = new int[numPlayers];

    /**
     *
     * Main constructor-method for RookState
     *
     */
    public RookState() {
        // at the beginning of the game, all player's are waiting
        // until the game has been initialized
        subStage = WAIT;

        // set the first player to bid as the player who was first
        // at the top of the list on the configuration screen
        currPlayer = 0;

        // set the maximum value of cards that can be placed into
        // all player hands, nest, and trick
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

        // all players at the beginning of the game have not passed the bidding phase
        pass = new boolean[numPlayers];
        pass[0] = false;
        pass[1] = false;
        pass[2] = false;
        pass[3] = false;

        newState = false;

        // set the deck to a randomly ordered, shuffled combination of all the playable cards,
        // and then deal out all the 41 cards to the 4 players' hands and the nest
        deck = initDeck();
        deal();

        // set trick winner, trump suit, and winning bid to default values
        // at the beginning of the game when nothing has been played out yet
        currTrickWinner = 0;
        trumpSuit = 0;
        winningBid = 0;

        // set the first player's bid of the first round as the minimum value of the bid
        playerBids[0] = 50;
        playerBids[1] = 0;
        playerBids[2] = 0;
        playerBids[3] = 0;

        // set all points that were earned at the beginning of a round to zero
        pointsThisRound[0] = 0;
        pointsThisRound[1] = 0;
        pointsThisRound[2] = 0;
        pointsThisRound[3] = 0;

        // initialize all the players' current total scores and names from the configuration screen
        playerScores = new int[numPlayers];
        playerNames = new String[numPlayers];

        // set the first player as the default winner at the beginning of the game
        winningPlayer = 0;

    }

    public RookState(boolean newRound, RookState roundState) {
        // at the beginning of the new round all players rebid
        subStage = BID;

        // set the first player to bid as the player who was first
        // at the top of the list on the configuration screen
        currPlayer = roundState.getActivePlayer();

        // set the maximum value of cards that can be placed into
        // all player hands, nest, and trick
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


        // all players at the beginning of the game have not passed the bidding phase
        pass = new boolean[numPlayers];
        pass[0] = false;
        pass[1] = false;
        pass[2] = false;
        pass[3] = false;

        // set the deck to a randomly ordered, shuffled combination of all the playable cards,
        // and then deal out all the 41 cards to the 4 players' hands and the nest
        deck = initDeck();
        deal();

        // set trick winner, trump suit, and winning bid to default values
        // at the beginning of the game when nothing has been played out yet
        currTrickWinner = currPlayer;
        trumpSuit = 0;
        winningBid = 0;

        // set the first player's bid of the first round as the minimum value of the bid
        playerBids[0] = 0;
        playerBids[1] = 0;
        playerBids[2] = 0;
        playerBids[3] = 0;
        playerBids[currPlayer] = 50;

        // set all points that were earned at the beginning of a new round to zero
        pointsThisRound[0] = 0;
        pointsThisRound[1] = 0;
        pointsThisRound[2] = 0;
        pointsThisRound[3] = 0;



        // initialize all the players' current total scores and names from the configuration screen
        playerScores = new int[numPlayers];
        playerScores[0] = roundState.playerScores[0];
        playerScores[1] = roundState.playerScores[1];
        playerScores[2] = roundState.playerScores[2];
        playerScores[3] = roundState.playerScores[3];
        playerNames = new String[numPlayers];

        // set the first player as the default winner at the beginning of the game
        winningPlayer = currPlayer;

        // allows Human Player to know the state is new
        newState = true;
    }

    /**
     *
     * Complementary constructor-method for any HumanPlayer playing the game
     * @param info
     *
     */
    public RookState(RookState info)
    {
        // set the current RookState as the given, updated info as
        // manipulated by the RookHumanPlayer and LocalGame classes
        RookState temp = info;

        // set all the current data of RookState to the given, updated
        // info from the "temp"-RookState
        subStage = temp.subStage;
        currPlayer = temp.currPlayer;
        playerZeroHand = new ArrayList<Card>(9);
        playerOneHand = new ArrayList<Card>(9);
        playerTwoHand = new ArrayList<Card>(9);
        playerThreeHand = new ArrayList<Card>(9);

        for(Card c : temp.playerZeroHand)
        {
            playerZeroHand.add(new Card(c));
        }
        for(Card c : temp.playerOneHand)
        {
            playerOneHand.add(new Card(c));
        }
        for(Card c : temp.playerTwoHand)
        {
            playerTwoHand.add(new Card(c));
        }
        for(Card c : temp.playerThreeHand)
        {
            playerThreeHand.add(new Card(c));
        }

        playerHands[0] = playerZeroHand;
        playerHands[1] = playerOneHand;
        playerHands[2] = playerTwoHand;
        playerHands[3] = playerThreeHand;

        nest = new ArrayList<Card>(5);
        for(Card c : temp.nest)
        {
            nest.add(new Card(c.getSuit(), c.getNumValue()));
        }

        currTrick = new ArrayList<Card>(4);
        for(Card c : temp.currTrick)
        {
            currTrick.add(new Card(c.getSuit(), c.getNumValue()));
        }

        pass = temp.pass;
        deck = temp.deck;

        trumpSuit = temp.trumpSuit;
        winningBid = temp.winningBid;

        playerBids[0] = temp.playerBids[0];
        playerBids[1] = temp.playerBids[1];
        playerBids[2] = temp.playerBids[2];
        playerBids[3] = temp.playerBids[3];
        winningPlayer = temp.winningPlayer;

        lastBidder = temp.lastBidder;
        currTrickWinner = temp.currTrickWinner;

        playerScores = temp.playerScores;
        playerNames = new String[4];
        playerNames[0] = temp.playerNames[0];
        playerNames[1] = temp.playerNames[1];
        playerNames[2] = temp.playerNames[2];
        playerNames[3] = temp.playerNames[3];

        //allows the human player to verify that they have
        //the newest state each turn. For debugging purposes
        newState = temp.newState;
    }

    /**
     * Get current substage of the game
     */
    public int getSubStage() {
        return subStage;
    }

    /**
     * Set a new substage for the game
     */
    public void setSubStage(int sub) {
        subStage = sub;
    }

    /**
     * Get current trump suit that was chosen by the winning bidder of the current round
     */
    public int getTrump(){ return trumpSuit; }

    /**
     * Set the trump suit that as the suit chosen by the winning bidder of the current round
     */
    public void setTrump(int trumpColor) {
        trumpSuit = trumpColor;
    }

    /**
     * Get the total score for each player throughout the game
     */
    public int getScore(int playerIdx) {
        return playerScores[playerIdx];
    }

    /**
     * Set the total score for each player throughout the game
     */
    public void setScore(int score, int index) {
        playerScores[index] += score;
    }

    /**
     * Set the current bid according to the value that was inputted by a certain player
     */
    public void setBid(int bid, int player)
    {
        playerBids[player] = bid;
    }

    public void setFalse()
    {
        newState = false;
    }

    /**
     * Get the current highest bid made by the latest player who made a bid
     */
    public int getHighestBid(){
        int highTemp = 50;  //the minimum value for the first bid is 50
        for(int i = 0; i < 4; i++){
            if(playerBids[i] > highTemp){
                highTemp = playerBids[i];
            }
        }
        return highTemp;
    }

    /**
     * Add a card to a certain, given card-pile (either a player's hand, nest, or trick)
     */
    public void addCard(Card c, ArrayList<Card> cardPile) {
        cardPile.add(c);
    }

    /**
     * Remove a card from a certain, given card-pile (either a player's hand, nest, or trick)
     */
    public void removeCard(Card c, ArrayList<Card> cardPile) {
        cardPile.remove(c);
    }

    /**
     * Initialize a randomized combination of all 41 cards into the deck, which will
     * be dealt out accordingly to each players' hand and nest at the beginning of each round
     */
    public ArrayList<Card> initDeck() {
        ArrayList<Card> initD = new ArrayList<Card>(41);

        // gather all information about a card's possible suit and number value
        int colors[] = {BLACK, RED, YELLOW, GREEN};
        int numbers[] = {5, 6, 7, 8, 9, 10, 11, 12, 13, 14};

        // initialize all the 41 cards into the deck
        for (int i = 0; i < colors.length; i++) {
            for (int j = 0; j < numbers.length; j++) {
                Card tempCard = new Card(colors[i], numbers[j]);
                initD.add(tempCard);
            }
        }

        // the Rook card is a special case, so add this last into the deck
        Card Rook = new Card(ROOK, 15);
        initD.add(Rook);

        //  call the shuffle-method to randomize the order of all the cards in the deck
        initD = shuffle(initD);

        return initD;
    }

    /**
     * Randomize the order of all the cards of the initial deck of each round
     */
    public ArrayList<Card> shuffle(ArrayList<Card> shuf) {

        ArrayList<Card> temp = new ArrayList<Card>();

        // run through the shuffling process of the deck 41 times for each card
        // that will randomly placed elsewhere in a newly "shuffled" deck
        while (!shuf.isEmpty()) {
            int loc = (int) (Math.random() * shuf.size());
            temp.add(shuf.get(loc));
            shuf.remove(loc);
        }
        shuf = temp;

        return shuf;
    }

    /**
     * Deal out all the cards of the randomized deck of 41 cards
     * to each player's hands and the nest
     */
    public void deal() {
        // deal out 9 cards to the 4 player's hands
        for (int j = 0; j < numPlayers; j++) {
            for (int i = 0; i < 9; i++) {
                Card temp = deck.get(0);
                playerHands[j].add(temp);
                deck.remove(0);
            }
        }

        // deal out the remaining 5 cards to the nest
        for (int k = 0; k < 5; k++) {
            Card temp = deck.get(k);
            if (k < 5) {
                nest.add(temp);
            }
        }
    }

    /**
     * Return true if the bidding phase is completed by:
     * determining the winner of the bidding phase, who will be able to interact
     * with the nest as well as choose the trump suit of the current round
     */
    public boolean finalizeBids() {
        // keeps track of how many players have passed throughout the bidding phase
        int countNumPlayersPassed = 0;

        // keeps track of the latest maximum value of a bid that a latest bidding player has made
        int maxVal = 0;

        // skip over the turns of players who have already passed
        for (int i = 0; i < numPlayers; i++) {
            if (pass[i]) {
                countNumPlayersPassed++;
            }
        }

        // if a player has made the highest bid of 120 points, then automatically set that player
        // as the winning bidder by commanding that all other players have "passed"
        for (int i = 0; i < numPlayers; i++) {
            if (playerBids[i] == 120) {
                countNumPlayersPassed = 3;
            }
        }

        // if at least 3 player have made a pass during the bidding phase, then set the
        // maximum value of the bid to the winning bidder's bid amount
        if (countNumPlayersPassed >= 3) {
            for (int j = 0; j < numPlayers; j++) {
                if (playerBids[j] > maxVal) {
                    maxVal = playerBids[j];
                    winningPlayer = j;
                }
            }
            winningBid = maxVal;
            setPlayer(winningPlayer);

            // the bids have been finalized for the current round
            return true;
        } else {
            // the bidding phase has not been completed yet
            return false;
        }
    }

    /**
     * Add up all the values of Counter-cards that have been placed into the current trick,
     * which will be eventually be used to increase the total points of the player who
     * won that current trick
     */
    public int countTrick() {

        int trickVal = 0;
        for (int i = 0; i < numPlayers; i++) {
            // add the counter-values of all 4 cards of the trick together
            trickVal += currTrick.get(i).counterValue;
        }
        return trickVal;
    }

    public int countNest() {

        int nestVal = 0;
        for (int i = 0; i < nest.size(); i++) {
            // add the counter-values of all 5 cards of the trick together
            nestVal += nest.get(i).counterValue;
        }
        return nestVal;
    }

    /**
     * Allow the user to interact with nest if they are the winner of the bidding phase
     * for the current round
     */
    public void useNest(ArrayList<Card> fromNest, ArrayList<Card> fromHand, ArrayList<Card> playerHand) {
        // remove a chosen amount of cards from the nest according to the player's choice,
        // and add those removed cards into the player's hand
        for (int i = 0; i < fromNest.size(); i++) {
            nest.remove(fromNest.get(i));
            playerHand.remove(fromHand.get(i));
        }

        // remove a chosen amount of cards from the player's hand according to the player's choice,
        // and add those removed cards into the nest
        for (int j = 0; j < fromNest.size(); j++) {
            nest.add(fromHand.get(j));
            playerHand.add(fromNest.get(j));
        }
    }

    /**
     * Automatically set a particular player's status during the bidding phase as a "pass"
     */
    public void setHold(int playerIndex) {
        pass[playerIndex] = true;
    }

    /**
     * Get the currently active player during any phase of the game
     */
    public int getActivePlayer() {
        return currPlayer;
    }

    /**
     * Set the currently active player during any phase of the game
     */
    public void setPlayer(int playIdx) {
        currPlayer = playIdx;
    }

    /**
     * Automatically set the currently active player during any phase of the game
     * NOTE: this method is used especially to alternate whose "turn" it is in
     *       a clockwise direction
     */
    public void setPlayer() {
        if (currPlayer == 3)
        {
            // if the current player reaches its maximum value, then
            // rotate back to the first player of the game (to continue
            // the clockwise rotation of players' turns)
            currPlayer = 0;
        }
        else
        {
            currPlayer++;
        }
    }
}
