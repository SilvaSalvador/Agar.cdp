package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketException;

import RemoteClient.AbsBoard;
import environment.Coordinate;
import environment.Direction;

public class DealWithPlayer extends Thread {
	private BufferedReader in;
	private ObjectOutputStream out;
	private Socket socket;
	private PhoneyHumanPlayer humanPlayer;
	private Server server;


	public DealWithPlayer(Socket socket, Server server, PhoneyHumanPlayer player) {
		this.socket = socket;
		this.server = server;
		this.humanPlayer = player;
	}

	void doConnections(Socket socket) throws IOException {
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	@Override
	public void run() {
		try {
			doConnections(socket);
			serve();
		} catch (IOException e) {
		}
	}

	public AbsBoard transformGame() {
		Game game = this.humanPlayer.game;
		AbsBoard AbsBoard = new AbsBoard(Game.DIMX, Game.DIMY);
		for (int x = 0; x < Game.DIMX; x++)
			for (int y = 0; y < Game.DIMY; y++)
				if (game.getCell(new Coordinate(x, y)).isOcupied()) {
					Player player = game.getCell(new Coordinate(x, y)).getPlayer();
					AbsBoard.createPlayer(x, y, player.getIdentification(), player.isHumanPlayer(),
							player.getCurrentStrength());
				}
		return AbsBoard;
	}

	private void serve() throws IOException {


		try {
			this.humanPlayer.game.addPlayerToGame(this.humanPlayer);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						out.writeObject(transformGame());
						sleep(Game.REFRESH_INTERVAL);

					}
				} catch (IOException | InterruptedException e) {
					System.out.println("Game Over!");

					try {
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}
			}
		};
		Thread sendGame = new Thread(runnable);
		sendGame.start();
		try {
			sleep(Game.INITIAL_WAITING_TIME);
			while (this.humanPlayer.IsAlive() && this.humanPlayer.getCurrentStrength() != 10
					&& !Thread.currentThread().isInterrupted()) {
				String d = in.readLine();
				Direction direction = Direction.values()[Integer.parseInt(d)];
				sleep(Game.REFRESH_INTERVAL);
				this.humanPlayer.move(direction);
			}
			if (this.humanPlayer.getCurrentStrength() == 10) {
				System.out.println("Player " + this.humanPlayer.getIdentification() + " won the game!");
			}
			if (!this.humanPlayer.IsAlive()) {
				System.out.println("Player " + this.humanPlayer.getIdentification() + " died!");
			}
			if (Thread.currentThread().isInterrupted()) {
				System.out.println("Player " + this.humanPlayer.getIdentification() + " lost!");
			}
		} catch (InterruptedException e) {
			System.out.println("Server lost connection to client!");
		}
	}
}