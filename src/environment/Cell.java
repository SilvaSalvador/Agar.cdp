package environment;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import game.Game;
import game.Interrupter;
import game.Player;

public class Cell {
	private Coordinate position;
	private Game game;
	private Player player = null;
	private Lock lock = new ReentrantLock();
	private Condition cellfree = lock.newCondition();

	public Cell(Coordinate position, Game g) {
		super();
		this.position = position;
		this.game = g;
	}

	public Coordinate getPosition() {
		return position;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean isOcupied() {
		return player != null;
	}

	public void setPlayer(Player p) {
		this.player = p;
		game.notifyChange();
	}

	public void removePlayer() {
		this.player = null;
	}

	public boolean atackerBlocked(Player atacker) {
		return ((!this.player.IsAlive() || this.player.getCurrentStrength() == 10) && !atacker.isHumanPlayer());
	}

	public synchronized void moveplayer(Cell oldCell) {
		this.lock.lock();
		oldCell.lock.lock();
		Player atacker = oldCell.player;
		if (!isOcupied()) {
			oldCell.removePlayer();
			this.setPlayer(atacker);
			oldCell.cellfree.signalAll();
		} else {
			if (this.player.IsAlive()) {
				fight(atacker, this.player);
				this.game.notifyChange();

				// inactive player
			} else if (atackerBlocked(atacker)) {
				Thread blockedplayer = Thread.currentThread();
				Thread interrupt_thread = new Interrupter(blockedplayer, Game.MAX_WAITING_TIME_FOR_MOVE);
				interrupt_thread.start();
				try {
					wait();// block
				} catch (InterruptedException e) {
				}
			}

		}
		
		this.lock.unlock();
		oldCell.lock.unlock();
	}

	public void fight(Player atacker, Player defender) {
		Byte strenghtSum = (byte) Math.min(atacker.getCurrentStrength() + defender.getCurrentStrength(), 10);

		if (atacker.getCurrentStrength() > defender.getCurrentStrength()) {
			atacker.setCurrentStrenght(strenghtSum);
			defender.setCurrentStrenght((byte) 0);
		} else if (atacker.getCurrentStrength() < defender.getCurrentStrength()) {
			defender.setCurrentStrenght(strenghtSum);
			atacker.setCurrentStrenght((byte) 0);
		} else {
			if (Math.random() > 0.5) {
				atacker.setCurrentStrenght(strenghtSum);
				defender.setCurrentStrenght((byte) 0);
			} else {
				defender.setCurrentStrenght(strenghtSum);
				atacker.setCurrentStrenght((byte) 0);
			}
		}
		if(atacker.getCurrentStrength()==10 || defender.getCurrentStrength()==10) {
			atacker.countDownCountDownLatch();
		}
	
	}

	public void setPlayerinitially(Player p) throws InterruptedException {
		lock.lock();

		if (p.isHumanPlayer() && isOcupied()) {
			return;
		}
		while (isOcupied() && this.player.IsAlive()) {
			System.out.println("The position " + this.getPosition() + "is ocupied by the player " + this.player.getIdentification()
					+ " player " + p.getIdentification() + "was put on hold");
			cellfree.await();
		}

		if (!isOcupied()) {
			this.player = p;
			lock.unlock();

		} else {
			wait();
		}
		this.game.notifyChange();
	}
}
