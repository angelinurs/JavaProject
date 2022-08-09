package wordBreaker;

import java.io.Serializable;

public class UserRank implements Serializable, Comparable<UserRank> {
	String name;
	int score;
	
	public UserRank(String name, int score) {
		this.name = name;
		this.score = score;
	}
	
	public String getName() { return name; }

	public int getScore() { return score; }
	
	public void  setScore( int score ) {  this.score = score; }
	
	public String toString() { 
		return getName() + " : " + getScore(); 
	}

	// Comparable interface compareTo method override
	@Override public int compareTo(UserRank o) {
		
		return ( this.score < o.score )?  1 :
			   ( this.score > o.score )? -1 : 0;
	}
};