
public class WatingUser implements Comparable<WatingUser> {
	int id;
	int from;
	int weight;
	
	public WatingUser(int id, int from) {
		super();
		this.id = id;
		this.from = from;
		weight = 0;
	}
	public WatingUser(int id, int from, int weight) {
		super();
		this.id = id;
		this.from = from;
		this.weight = weight;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFrom() {
		return from;
	}
	public void setFrom(int from) {
		this.from = from;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	@Override
	public int compareTo(WatingUser o) {
		if(this.weight>o.weight) return -1;
		else if(this.weight == o.weight) {
//			if(this.from>o.from) return -1;
//			else return 1;
			return 0;
		}else {
			return 1;
		}
	}
	
	
	
	
}
