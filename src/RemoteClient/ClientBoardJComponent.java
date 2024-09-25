package RemoteClient;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.event.MenuKeyEvent;

import game.Game;

public class ClientBoardJComponent extends JComponent implements KeyListener {
	private Image obstacleImage = new ImageIcon("obstacle.png").getImage();
	private Image humanPlayerImage = new ImageIcon("abstract-user-flat.png").getImage();
	private int lastPressedDirection = -1;
	private JFrame frame = new JFrame("pcd.io");
	private AbsBoard board;
	private RemotePlayer remotePlayer;

	public ClientBoardJComponent(AbsBoard board, RemotePlayer remotePlayer) {
		this.board = board;
		setFocusable(true);
		addKeyListener(this);
		this.remotePlayer = remotePlayer;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		double cellHeight = getHeight() / (double) Game.DIMY;
		double cellWidth = getWidth() / (double) Game.DIMX;

		for (int y = 1; y < Game.DIMY; y++) {
			g.drawLine(0, (int) (y * cellHeight), getWidth(), (int) (y * cellHeight));
		}
		for (int x = 1; x < Game.DIMX; x++) {
			g.drawLine((int) (x * cellWidth), 0, (int) (x * cellWidth), getHeight());
		}
		for (int x = 0; x < Game.DIMX; x++)
			for (int y = 0; y < Game.DIMY; y++) {
				AbsPlayer player = this.board.getPlayer(x, y);
				if (player != null) {
					// Fill yellow if there is a dead player
					if (player.getStrength() == 0) {
						g.setColor(Color.YELLOW);
						g.fillRect((int) (x * cellWidth), (int) (y * cellHeight), (int) (cellWidth),
								(int) (cellHeight));
						g.drawImage(obstacleImage, (int) (x * cellWidth), (int) (y * cellHeight), (int) (cellWidth),
								(int) (cellHeight), null);
						// if player is dead, don'd draw anything else?
						continue;
					}
					// Fill green if it is a human player
					if (player.isHuman()) {
						g.setColor(Color.GREEN);
						g.fillRect((int) (x * cellWidth), (int) (y * cellHeight), (int) (cellWidth),
								(int) (cellHeight));
						// Custom icon?
						g.drawImage(humanPlayerImage, (int) (x * cellWidth), (int) (y * cellHeight), (int) (cellWidth),
								(int) (cellHeight), null);
					}
					g.setColor(new Color(player.getId() * 10000));
					((Graphics2D) g).setStroke(new BasicStroke(5));
					Font font = g.getFont().deriveFont((float) cellHeight);
					g.setFont(font);
					String strengthMarking = (player.getStrength() == 10 ? "X" : "" + player.getStrength());
					g.drawString(strengthMarking, (int) ((x + .2) * cellWidth), (int) ((y + .9) * cellHeight));
				}

			}
	}

	public void changeBoard(AbsBoard board) {
		this.board = board;
	}

	@Override
	// mudar
	public void keyPressed(KeyEvent e) {
		int numberofplayer = 0;
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			lastPressedDirection = environment.Direction.LEFT.ordinal();
			numberofplayer = 1;
			break;
		case KeyEvent.VK_RIGHT:
			lastPressedDirection = environment.Direction.RIGHT.ordinal();
			numberofplayer = 1;
			break;
		case KeyEvent.VK_UP:
			lastPressedDirection = environment.Direction.UP.ordinal();
			numberofplayer = 1;
			break;
		case KeyEvent.VK_DOWN:
			lastPressedDirection = environment.Direction.DOWN.ordinal();
			numberofplayer = 1;
			break;
		case KeyEvent.VK_A:
			lastPressedDirection = environment.Direction.LEFT.ordinal();
			numberofplayer = 2;
			break;
		case KeyEvent.VK_D:
			lastPressedDirection = environment.Direction.RIGHT.ordinal();
			numberofplayer = 2;
			break;
		case KeyEvent.VK_W:
			lastPressedDirection = environment.Direction.UP.ordinal();
			numberofplayer = 2;
			break;
		case KeyEvent.VK_S:
			lastPressedDirection = environment.Direction.DOWN.ordinal();
			numberofplayer = 2;
			break;
		}
		this.remotePlayer.sendDirection(Integer.toString(lastPressedDirection), numberofplayer);
	}

	public int getLastPressedDirection() {
		return lastPressedDirection;
	}

	public void clearLastPressedDirection() {
		lastPressedDirection = -1;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
