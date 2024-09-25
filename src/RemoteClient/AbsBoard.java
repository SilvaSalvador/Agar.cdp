package RemoteClient;

import java.io.Serializable;
import java.util.Observable;

public class AbsBoard extends Observable implements Serializable {
	private AbsPlayer[][] board;

	public AbsBoard(int dimx, int dimy) {
		this.board = new AbsPlayer[dimx][dimy];
	}

	public void createPlayer(int x, int y, int id, boolean ishuman, byte strenght) {
		this.board[x][y] = new AbsPlayer(id, ishuman, strenght);
	}

	public AbsPlayer getPlayer(int x, int y) {
		return this.board[x][y];
	}

public void notifyChange() {
	setChanged();
	notifyObservers();
}

}
