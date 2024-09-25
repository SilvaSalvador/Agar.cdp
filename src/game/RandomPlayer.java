package game;

import java.util.concurrent.CountDownLatch;

import environment.Direction;

public class RandomPlayer extends Player implements Runnable {

	public RandomPlayer(int id, Game game, byte strength, CountDownLatchCustom countDownLatch) {
		super(id, game, strength, countDownLatch);
	}

	@Override
	public void run() {

		try {
			super.placeInMap();
			Thread.sleep(game.INITIAL_WAITING_TIME);

			while (this.IsAlive() && this.getCurrentStrength() != 10 && !Thread.currentThread().isInterrupted()) {
				this.move(Direction.random());
				Thread.sleep(Game.MAX_WAITING_TIME_FOR_MOVE * this.originalStrength);
			}
		} catch (InterruptedException e) {
		}
	}

	@Override
	public boolean isHumanPlayer() {
		return false;
	}

}
