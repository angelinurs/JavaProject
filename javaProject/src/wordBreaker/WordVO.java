package wordBreaker;

import java.awt.Color;

/* ***********************
 * - WordVO class
 */
public class WordVO extends Thread {
	int x, y = -30;
	int width, height = 20;
	int speed;
	
	String text;
	
	Color color;

	WordBreaker frame;
	
	public WordVO( String text, WordBreaker frame ) {
		this.text = text;
		this.frame = frame;
		
		this.x = (int)( Math.random() * 
				( frame.gameCard.getSize().getWidth() - 100 ) );
		this.speed = (int)( Math.random() * 2 + 1 );
		
		this.color = new Color(
						(int)( Math.random() * 256 ),	// red
						(int)( Math.random() * 256 ),	// green
						(int)( Math.random() * 256 )	// blue
					);
	}

	@Override public void run() {
		while( true ) {
			y += speed;
			
			if( y > frame.gameCard.getSize().getHeight() - 16 ) {
				break;
			}
			
			frame.gameCard.repaint();
			
			try {
				Thread.sleep( 100 );
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		frame.wordList.remove( this );
	}
}