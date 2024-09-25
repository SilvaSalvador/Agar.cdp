package game;

import java.io.IOException;
import java.net.ServerSocket;

import javax.print.attribute.standard.MultipleDocumentHandling;

public class Interrupter extends Thread {
	private Thread thread;
	private long time;
	private ServerSocket socket;

	public Interrupter(Thread t, long time) {
		this.thread = t;
		this.time = time;
	}
   public Interrupter(Thread t, long time, ServerSocket socket) {
	   this.thread = t;
		this.time = time;
		this.socket=socket;
   }
	@Override
	public void run() {

		try {
			sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(this.socket==null) {
		this.thread.interrupt();
		}
		else {
			try {
				this.socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}