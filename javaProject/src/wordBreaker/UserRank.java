package wordBreaker;

import java.io.Serializable;

public class UserRank implements Serializable {
	String name;
	String score;
	
	public UserRank(String name, String score) {
		this.name = name.trim();
		this.score = score.trim();
	}
	
	public String getName() { return name; }

	public String getScore() { return score; }
	
	public String toString() { return }
};
