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
 * The primary activity for the Rook game
 * Consists of the options included in the Configuration Screen
 * 
 * @author Steven R. Vegdahl
 * @author Sam DeWhitt, Eric Hoser, Mitchell Nguyen, Alexander Nowlin
 * @version December 2016
 */
public class RookMainActivity extends GameMainActivity {

	// used for network-play
	public static final int PORT_NUMBER = 4752;

	/**
	 * A Rook game for 4 players.
	 * The default is human vs. computer
	 */
	@Override
	public GameConfig createDefaultConfig() {

		// Define the allowed player types
		ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();
		
		playerTypes.add(new GamePlayerType("human player (green)") {
			public GamePlayer createPlayer(String name) {
				return new RookHumanPlayer(name, Color.GREEN);
			}});
		playerTypes.add(new GamePlayerType("computer player (dumb)") {
			public GamePlayer createPlayer(String name) {
				return new dRookComputerPlayer(name);
			}});
		playerTypes.add(new GamePlayerType("computer player (dumb)") {
			public GamePlayer createPlayer(String name) {
				return new dRookComputerPlayer(name);
			}});
		playerTypes.add(new GamePlayerType("computer player (smart)") {
			public GamePlayer createPlayer(String name) {
				return new sRookComputerPlayer(name);
			}});

		// Create a game configuration class for SlapJack
		GameConfig defaultConfig = new GameConfig(playerTypes, 4, 4, "Rook", PORT_NUMBER);

		// Add the default players
		defaultConfig.addPlayer("Human", 0);
		defaultConfig.addPlayer("Computer1", 1);
		defaultConfig.addPlayer("Computer2", 2);
		defaultConfig.addPlayer("Computer3", 3);
		
		 //Set the initial information for the remote player
		defaultConfig.setRemoteData("Guest", "", 1);
		
		//done!
		return defaultConfig;
	}//createDefaultConfig

	/**
	 * Create a local-game for Rook
     */
	@Override
	public LocalGame createLocalGame() {
		return new RookLocalGame();
	}
}
