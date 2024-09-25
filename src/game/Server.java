package game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import gui.GameGuiMain;

public class Server {
	public static final int PORT = 9090;
	private GameGuiMain gamegui;
	private CountDownLatchCustom countDownLatch;
	private int nrplayers;
	private Thread[] Threads;

	public Server(int nrPlayers, GameGuiMain gamegui) {
		this.gamegui = gamegui;
		this.countDownLatch = new CountDownLatchCustom(gamegui.getGame().getNUM_FINISHED_PLAYERS_TO_END_GAME());
		this.nrplayers = nrPlayers;
		this.Threads = new Thread[nrPlayers + 10];
	}

	public void startServing() {
		this.gamegui.init();
		generateRandom(this.nrplayers);
		ServerSocket ss;
		try {
			ss = new ServerSocket(PORT);
			Thread server = Thread.currentThread();
			Thread interrupt_thread = new Interrupter(server, Game.INITIAL_WAITING_TIME, ss);
			interrupt_thread.start();

			while (true) {
				Socket socket = ss.accept();
				DealWithPlayer humanThread = new DealWithPlayer(socket, this, createHumanPlayer());
				this.Threads[nrplayers] = humanThread;
				this.nrplayers += 1;
				humanThread.start();
			}
		} catch (IOException e) {
		}
		waitEndGame();
	}

	public void waitEndGame() {
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
		}
		for (int i = 0; i < this.nrplayers; i++) {
			Threads[i].interrupt();
		}

		gamegui.endGame();
	}

	public void generateRandom(int nr) {
		for (int i = 0; i < nr; i++) {
			RandomPlayer bot = new RandomPlayer(i, this.gamegui.getGame(), getRandomStrenght(), this.countDownLatch);
			Threads[i] = new Thread(bot);
			Threads[i].start();
		}
	}

	public PhoneyHumanPlayer createHumanPlayer() {
		return new PhoneyHumanPlayer(this.nrplayers, this.gamegui.getGame(), (byte) 5, this.countDownLatch);
	}

	public static byte getRandomStrenght() {
		Random randomNum = new Random();
		return (byte) (randomNum.nextInt(3) + 1);
	}

	public static void main(String[] args) {
		GameGuiMain gameGui = new GameGuiMain();
		Server server = new Server(gameGui.getGame().getNUM_PLAYERS(), gameGui);
		server.startServing();
	}

}
