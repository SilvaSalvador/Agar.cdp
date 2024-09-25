package game;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import gui.BoardJComponent;

/**
 * Class to demonstrate a player being added to the game.
 * @author luismota
 *
 */
public class PhoneyHumanPlayer extends Player {
	
	
	
	public PhoneyHumanPlayer(int id, Game game, byte strength,CountDownLatchCustom countDownLatch) {
		super(id, game, strength,countDownLatch);
	}
	
	
	
    @Override
	public boolean isHumanPlayer() {
		return true;
	}
	
	

}
