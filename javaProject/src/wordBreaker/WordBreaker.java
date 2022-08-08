package wordBreaker;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WordBreaker extends JFrame {
	
	CardLayout cards;
	
	JPanel mainCard,
		   gameCard;
	
	// main card
	JPanel mainCenterPane,
		   mainBottomPane;
	
	JPanel userInputPane;
	JPanel loginPane;
	
	JLabel idLabel;
	JTextField idTextfield;
	JButton loginBtn;
	
	ImageIcon imageIcon;
	
	final String imageFilename = "src/Source/kakao.jpg";
	final String wordFilename = "src/Source/word_list.txt";
	
	// game card
	JPanel gameTopPane,
		   gameCenterPane,
		   gameBottomPane;
	
	// menu
	JMenuBar menuBar;
	JMenu fileMenu,
		  viewMenu;
	JMenuItem newItem,
			  openItem,
			  saveItem,
			  exitItem;
	JMenuItem viewItem;
	
	// WordVO data
	ArrayList<WordVO> wordList;
	
	
	public WordBreaker() {
		super( "Word breaker version 1.0" );
		
		cards = new CardLayout();
		
		setLayout( cards );
		
		setMainCard();
		setMenu();
		setGameCard();
		
		
		setBounds( 300, 300, 300, 500 );
		setVisible( true );
		
		addWindowListener( new WindowAdapter() {

			@Override public void windowClosing(WindowEvent e) {
				System.exit( 0 );
			}
		});
		
		// temporary set game card for development
		cards.show( this.getContentPane(), "game" );
	}
	
	/* *****************
	 * set set menu
	 */
	private void setMenu() {
		menuBar = new JMenuBar();
		
		menuBar.add( fileMenu = new JMenu( "File" ) );
		menuBar.add( viewMenu = new JMenu( "View" ) );
		
		fileMenu.add( newItem = new JMenuItem( "New Game" ) );
		fileMenu.add( openItem = new JMenuItem( "Open_File" ) );
		fileMenu.add( saveItem = new JMenuItem( "Save_File" ) );
		fileMenu.addSeparator();
		fileMenu.add( exitItem = new JMenuItem( "Exit" ) );
		
		viewMenu.add( viewItem = new JMenuItem( "View_Rank") );
		
		setJMenuBar( menuBar );
	}

	/* *****************
	 * set main Card
	 */
	private void setMainCard() {
		mainCard = new JPanel( new BorderLayout() );
		
		mainCard.add( mainCenterPane = new JPanel(),
					  BorderLayout.CENTER );
		mainCard.add( mainBottomPane = new JPanel( new GridLayout( 2, 1 ) ),
				  	  BorderLayout.SOUTH );
		
		/* **********
		 * set main center pane
		 */
		imageIcon = new ImageIcon( imageFilename );
		Image img = imageIcon.getImage();
		
		imageIcon.setImage( img.getScaledInstance( 280, 370,
												   Image.SCALE_SMOOTH ) );
		
		mainCenterPane.add( new JLabel( imageIcon ) );
		
		/* **********
		 * set main bottom pane
		 */
		mainBottomPane.add( userInputPane = 
							new JPanel( new FlowLayout( FlowLayout.RIGHT ) ) );
		mainBottomPane.add( loginPane = 
							new JPanel( new FlowLayout( FlowLayout.RIGHT ) ) );
		
		userInputPane.add( idLabel = new JLabel( "ID : " ) );
		userInputPane.add( idTextfield = new JTextField( 10 ) );
		
		loginPane.add( loginBtn = new JButton( "Login" ) );
		
//		mainCard.setBackground( Color.CYAN );
		
		add( "main", mainCard );
	}
	
	/* *****************
	 * set game Card
	 */
	private void setGameCard() {
		gameCard = new JPanel();
		
		add( "game", gameCard );
	}

	public static void main(String[] args) {
		new WordBreaker();

	}
}
