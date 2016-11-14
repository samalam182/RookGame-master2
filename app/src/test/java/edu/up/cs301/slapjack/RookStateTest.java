package edu.up.cs301.slapjack;

import java.util.ArrayList;

import edu.up.cs301.card.Card;

import static org.junit.Assert.*;

/**
 * Created by dewhitt17 on 11/9/2016.
 */
public class RookStateTest {

    @org.junit.Test
    public void testAddCard() throws Exception {
        RookState rookState = new RookState();
        Card c = rookState.deck.get(0);
        ArrayList cardPile = rookState.playerZeroHand;
        rookState.addCard(c, cardPile);
        assertEquals(rookState.playerZeroHand.get(0), c);
    }

    @org.junit.Test
    public void testRemoveCard() throws Exception {
        RookState rookState = new RookState();
        Card c = rookState.deck.get(0);
        ArrayList cardPile = rookState.playerZeroHand;
        rookState.removeCard(c, cardPile);
        boolean b = cardPile.remove(c);  // is true if it finds Card c and successfully removes it
        assertTrue(!b);
    }

    @org.junit.Test
    public void testShuffle() throws Exception {
        RookState rookState = new RookState();
        rookState.shuffle();

        for(int j = 0; j<4; j++) {
            for (int i = 0; i < 9; i++) {
                if (j == 0) {
                    assertTrue(rookState.playerZeroHand.get(i) instanceof Card);
                }
                if (j == 1) {
                    assertTrue(rookState.playerOneHand.get(i) instanceof Card);
                }
                if (j == 2) {
                    assertTrue(rookState.playerTwoHand.get(i) instanceof Card);
                }
                if (j == 3) {
                    assertTrue(rookState.playerThreeHand.get(i) instanceof Card);
                }
            }
        }
        for(int k = 0; k<5; k++){
            assertTrue(rookState.nest.get(k) instanceof Card);
        }

        assertTrue(rookState.deck.isEmpty());
    }

    @org.junit.Test
    public void testFinalizeBids() throws Exception {
        RookState rookState = new RookState();
        assertTrue(rookState.winningBid > 0);
        int count;
        count = 0;
        for(int i = 0 ; i<rookState.bidPass.length; i++){
            if(rookState.bidPass[i] == true){
                count++;
            }
        }

        assertEquals(3, count);

    }

//    @org.junit.Test
//    public void testCountTrick() throws Exception {
//        RookState rookState = new RookState();
//        Card c1;
//        Card c2;
//        Card c3;
//        Card c4;
//        rookState.currTrick.add(c1);
//        rookState.currTrick.add(c2);
//        rookState.currTrick.add(c3);
//        rookState.currTrick.add(c4);
//
//        for(int k = 0; k<4; k++){
//
//        }
//
//        assertEquals(rookState.currTrick.get(k) instanceof Card);
//
//    }

    @org.junit.Test
    public void testUseNest() throws Exception {
        RookState rookState = new RookState();
        for(int k = 0; k<5; k++){
            assertTrue(rookState.nest.get(k) instanceof Card);
        }
    }
}