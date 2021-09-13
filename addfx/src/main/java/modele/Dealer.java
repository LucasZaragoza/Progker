package modele;

public class Dealer {
	private Player player;
	
	public Dealer(Player p) {
		player = p;
	}
	
	public Player getDealer() {
		return player;
	}
	
	public void setDealer(Player p) {
		player = p;
	}
}
