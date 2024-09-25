package RemoteClient;

import java.io.Serializable;

public class AbsPlayer implements Serializable {
	private int id;
	private boolean ishuman;
	private byte strenght;

	public AbsPlayer(int id, boolean ishuman, byte strenght) {
		this.id = id;
		this.ishuman = ishuman;
		this.strenght = strenght;
	}

	public int getId() {
		return this.id;
	}

	public int getStrength() {
		return this.strenght;
	}

	public boolean isHuman() {
		return this.ishuman;
	}
}
