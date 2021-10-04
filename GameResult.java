
public class GameResult {
	int win;
	int lose;
	int taken;
	public GameResult(int win, int lose, int taken) {
		super();
		this.win = win;
		this.lose = lose;
		this.taken = taken;
	}
	public int getWin() {
		return win;
	}
	public void setWin(int win) {
		this.win = win;
	}
	public int getLose() {
		return lose;
	}
	public void setLose(int lose) {
		this.lose = lose;
	}
	public int getTaken() {
		return taken;
	}
	public void setTaken(int taken) {
		this.taken = taken;
	}
	
	
}
