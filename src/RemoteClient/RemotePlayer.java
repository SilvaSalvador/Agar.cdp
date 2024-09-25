package RemoteClient;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import game.Server;

//IMPLEMENTS KEY LISTENER E YA QUANDO CLICA NUMA TECLA ENVIA AS COISAS
public class RemotePlayer {
    private InetAddress adress;
    private int port;
    private boolean multiplayer;
    private ObjectInputStream in1;
    private ObjectInputStream in2;
    private PrintWriter out1;
    private PrintWriter out2;
    private Socket socket1;

    public RemotePlayer(InetAddress adress, int port, boolean multiplayer) {
        this.adress = adress;
        this.port = port;
        this.multiplayer = multiplayer;
    }

    public void runClient() throws IOException {
        connectToServer();
        play();
    }

    void connectToServer() throws IOException {

        this.socket1 = new Socket(this.adress, this.port);
        this.in1 = new ObjectInputStream(this.socket1.getInputStream());
        this.out1 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket1.getOutputStream())), true);
        if (multiplayer) {
            Socket socket2 = new Socket(this.adress, this.port);
            this.in2 = new ObjectInputStream(socket2.getInputStream());
            this.out2 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket2.getOutputStream())), true);
        }
    }

    public void play() {
        ClientGui gui = null;
        try {
            AbsBoard board = null;
            board = (AbsBoard) this.in1.readObject();
            gui = new ClientGui(board, this);
            gui.init();
            board.notifyChange();

            while (!this.socket1.isClosed()) {

                AbsBoard seilAbsBoard = (AbsBoard) this.in1.readObject();
                gui.ChangeBoard(seilAbsBoard);
            }


        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Server down game over!");
            gui.endGame();

        }
    }

    public void sendDirection(String dirString, int nrPlayer) {
        if (nrPlayer == 1) {
            this.out1.println(dirString);
        }
        if (nrPlayer == 2) {
            this.out2.println(dirString);
        }
    }

    public static void main(String[] args) {
        String[] options = {"SinglePlayer-arrowkeys", "Multiplayer-arrowkeys-wasd"};
        int selection = JOptionPane.showOptionDialog(null, "Select one", "Welcome to Agar.io", 0, 2, null, options,
                options[0]);
        RemotePlayer remotePlayer = null;
        try {
            if (selection == 0) {
                remotePlayer = new RemotePlayer(InetAddress.getByName(null), Server.PORT, false);
            }
            if (selection == 1) {
                remotePlayer = new RemotePlayer(InetAddress.getByName(null), Server.PORT, true);
            }

        } catch (UnknownHostException e) {

        }
        try {
            remotePlayer.runClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
