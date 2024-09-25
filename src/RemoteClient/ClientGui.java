package RemoteClient;

import java.io.ObjectInputStream.GetField;
import java.util.Observable;
import java.util.Observer;

import game.Game;
import game.PhoneyHumanPlayer;
import game.Player;

import javax.swing.JFrame;

public class ClientGui implements Observer {
	private JFrame frame = new JFrame("pcd.io");
	private ClientBoardJComponent boardClientGui;
	private AbsBoard board;
	private RemotePlayer remotePlayer;

	public ClientGui(AbsBoard board, RemotePlayer remotePlayer) {
		super();
		this.board = board;
		this.remotePlayer = remotePlayer;
		this.board.addObserver(this);
		buildGui();
	}

	private void buildGui() {
		boardClientGui = new ClientBoardJComponent(this.board, this.remotePlayer);
		frame.add(boardClientGui);
		frame.setSize(800, 800);
		frame.setLocation(0, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init() {
		frame.setVisible(true);
	}

	public void endGame() {
		frame.setVisible(false);
		frame.dispose();
	}
	public void ChangeBoard(AbsBoard board) {
		this.board = board;
		this.boardClientGui.changeBoard(board);
		this.board.addObserver(this);
		this.board.notifyChange();
	}

	@Override
	public void update(Observable o, Object arg) {
		boardClientGui.repaint();
	}

	public ClientBoardJComponent getClientBoardJComponent() {
		return this.boardClientGui;
	}

}
