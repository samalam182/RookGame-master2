package edu.up.cs301.game.rook;

import java.util.ArrayList;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.config.GameConfig;
import edu.up.cs301.game.config.GamePlayerType;
//import edu.up.cs301.slapjack.SJComputerPlayer;
//import edu.up.cs301.slapjack.SJHumanPlayer;
//import edu.up.cs301.slapjack.SJLocalGame;

import android.graphics.Color;

/**
 * this is the primary activity for Slapjack game
 * 
 * @author Steven R. Vegdahl
 * @version July 2013
 */
public class RookMainActivity extends GameMainActivity {
	
	public static final int PORT_NUMBER = 4752;

	/** a rook game for four players. The default is human vs. computer */
	@Override
	public GameConfig createDefaultConfig() {

		// Define the allowed player types
		ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();
		
		playerTypes.add(new GamePlayerType("human player (green)") {
			public GamePlayer createPlayer(String name) {
				return new RookHumanPlayer(name, Color.GREEN);
			}});
		playerTypes.add(new GamePlayerType("computer player (dumb1)") {
			public GamePlayer createPlayer(String name) {
				return new dRookComputerPlayer(name);
			}});
		playerTypes.add(new GamePlayerType("computer player (dumb2)") {
			public GamePlayer createPlayer(String name) {
				return new dRookComputerPlayer(name);
			}});
		playerTypes.add(new GamePlayerType("computer player (dumb3)") {
			public GamePlayer createPlayer(String name) {
				return new dRookComputerPlayer(name);
			}});

		// Create a game configuration class for SlapJack
		GameConfig defaultConfig = new GameConfig(playerTypes, 4, 4, "Rook", PORT_NUMBER);

		// Add the default players
		defaultConfig.addPlayer("Human", 0);
		defaultConfig.addPlayer("Computer1", 1);
		defaultConfig.addPlayer("Computer2", 2);
		defaultConfig.addPlayer("Computer3", 3);
		
		// Set the initial information for the remote player
		//defaultConfig.setRemoteData("Guest", "", 1);
		
		//done!
		return defaultConfig;
	}//createDefaultConfig

	@Override
	public LocalGame createLocalGame() {
		return new RookLocalGame();
	}
}
