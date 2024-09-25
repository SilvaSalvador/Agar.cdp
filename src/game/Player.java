package game;

import javax.sound.sampled.Port;
import javax.swing.text.StyledEditorKit.BoldAction;

import org.w3c.dom.CDATASection;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;

/**
 * Represents a player.
 * 
 * @author luismota
 *
 */

public abstract class Player {

	protected Game game;
	private int id;
	private byte currentStrength;
	protected byte originalStrength;
	private CountDownLatchCustom countDownLatch;

	public Cell getCurrentCell() {
		for (int x = 0; x < Game.DIMX; x++) {
			for (int y = 0; y < Game.DIMY; y++) {
				if (game.getCell(new Coordinate(x, y)).isOcupied()) {
					Player player = game.getCell(new Coordinate(x, y)).getPlayer();
					if (player == this) {
						return game.getCell(new Coordinate(x, y));
					}
				}
			}
		}
		return null;
	}

	public void countDownCountDownLatch() {
		this.countDownLatch.countDown();
	}

	public void placeInMap() throws InterruptedException {

		game.addPlayerToGame(this);

	}

	public Player(int id, Game game, byte strength, CountDownLatchCustom countDownLatch) {
		super();
		this.id = id;
		this.game = game;
		currentStrength = strength;
		originalStrength = strength;
		this.countDownLatch = countDownLatch;
	}

	public abstract boolean isHumanPlayer();

	@Override
	public String toString() {
		return "Player [id=" + id + ", currentStrength=" + currentStrength + ", getCurrentCell()=" + getCurrentCell()
				+ "]";
	}

	public void move(Direction d) {
		Coordinate position = this.getCurrentCell().getPosition();
		Coordinate newPosition = position.translate(d.getVector());
		Cell oldCell = game.getCell(position);
		if (canMoveTo(newPosition)) {
			Cell nextCell = game.getCell(newPosition);
			nextCell.moveplayer(oldCell);
		}
	}

	public boolean canMoveTo(Coordinate c) {
		if(this.currentStrength==10)
			return false;
		if (c.x < 0)
			return false;
		if (c.y < 0)
			return false;
		if (c.x >= game.DIMX)
			return false;
		if (c.y >= game.DIMY)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public byte getCurrentStrength() {
		return currentStrength;
	}

	public void setCurrentStrenght(byte t) {
		this.currentStrength = t;
	}

	public byte getOriginalStrenght() {
		return originalStrength;
	}

	public boolean IsAlive() {
		return this.currentStrength != 0;
	}

	public int getIdentification() {
		return id;
	}
}
