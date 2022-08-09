package wordBreaker;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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
	
	// ex-source path
	final String imageFilename = "src/Source/kakao.jpg";
	final String wordFilename = "src/Source/word_list.txt";
	final String savefile = "src/savedata/savefile.obj";
	
	// user name
	String username;
	ArrayList<UserRank> ranklist;
	
	// game card
	JPanel gameTopPane,
		   gameCenterPane,
		   gameBottomPane;
	
	JLabel scoreLabel;
	int score;
	
	JTextField wordInputTextField;
	JButton startBtn,
			confirmBtn;
	
	// menu
	JMenuBar menuBar;
	JMenu viewMenu;
	JMenuItem viewItem,
	  		  exitItem;
	
	// WordVO data
	ArrayList<WordVO> wordVoList;
	
	ArrayList<String> wordDataList;
	
	// Font data
	Font font;
	
	Thread thread;
	
	boolean isStart = false;
	int dropTime = 1000;
	
	// condition isStartButtonClicked
	boolean isStartButtonClicked = false;

	// constructor
	public WordBreaker() {
		super( "Word breaker version 1.0" );
		
		font = new Font( "Serif", Font.BOLD, 20 );
		
		ranklist = new ArrayList<>();
		wordVoList = new ArrayList<>();
		setWordDataList();
		
		cards = new CardLayout();
		
		setLayout( cards );
		
		setMainCard();
		setMenu();
		setGameCard();

		
		setBounds( 300, 300, 300, 500 );
		setVisible( true );
		
		setEvent();
		
		// temporary set game card for development
//		cards.show( this.getContentPane(), "game" );
	}
	
	/* *****************
	 * set event
	 * - windowClosing ( window, exitItem )
	 * - login ( idTextfield, loginBtn )
	 * - game start ( startBtn )
	 * - typing action ( wordInputTextField )
	 */
	private void setEvent() {
		// window exit
		addWindowListener( new WindowAdapter() {
			
			@Override public void windowClosing(WindowEvent e) {
				saveAndExit();
			}
		});
		
		// exit menu
		exitItem.addActionListener( new ActionListener() {
			
			@Override public void actionPerformed(ActionEvent e) {
				saveAndExit();
			}
		});
		
		// game login -> enter action
		idTextfield.addActionListener( new ActionListener() {
			
			@Override public void actionPerformed(ActionEvent e) {
				username =  idTextfield.getText().trim();
				cards.show( WordBreaker.this.getContentPane(), "game" );
			}
		});
		
		// game login -> click button action
		loginBtn.addActionListener( new ActionListener() {
			
			@Override public void actionPerformed(ActionEvent e) {
				username =  idTextfield.getText().trim();
				cards.show( WordBreaker.this.getContentPane(), "game" );
				
			}
		});
		
		// [ event ] Button of start
		// click 과 같은 명확한 지시 상황 없이 각 WordVO object 를 생성하는 thread 가 필요.
		startBtn.addActionListener( new ActionListener() {
			
			@Override public void actionPerformed(ActionEvent e) {
				// did it check that user playing game 
				isStartButtonClicked = true;
				
				if( thread == null ) {
					thread = new Thread() {
						
						@Override public void run() {
							isStart = true;
							
							while( true ) {
								
								try {
									Thread.sleep( dropTime );
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
								}
								
								int index = (int)( Math.random() * wordDataList.size() - 1 );
								
								WordVO vo = new WordVO( wordDataList.get( index ), WordBreaker.this );
								
								wordVoList.add( vo );
								
								vo.start();
							}
						}
					};
					thread.start();
					
					startBtn.setEnabled( false );
				}
			}
		});
		
		// when play game, enter typing action
		wordInputTextField.addActionListener( new ActionListener() {
			
			@Override public void actionPerformed(ActionEvent e) {
				String keyword = wordInputTextField.getText().trim();
				
				if( keyword.length() > 0 ) {
					// ConcurrentModificationException 을 피하기 위해 
					// 자료형 복사하기
//					ArrayList<WordVO> cloneWords = WordBreak.this.getCloneWords();
					ArrayList<WordVO> cloneWords = getCloneWords();
					
					Iterator<WordVO> it = cloneWords.iterator();
					
					while( it.hasNext() ) {
						WordVO vo = it.next();
						
						if( keyword.equals( vo.text ) ) {
							System.out.println( keyword + ", " + vo.text );
							wordVoList.remove( vo );
							
							scoreLabel.setText( String.valueOf( score += 10 ) );
						}
					}
					
					wordInputTextField.setText( "" );
				}
			}
		});
		
		// view rank menu action 
		viewItem.addActionListener( new ActionListener() {
			
			@Override public void actionPerformed(ActionEvent e) {
				
				String mesg = null;
				
				if( loadRankdDataFile() ) {
					
					Iterator<UserRank> it = ranklist.iterator();
					
					StringBuffer sb = new StringBuffer();
					while( it.hasNext() ) {
						UserRank userScore = it.next();
						sb.append( userScore.toString() );
						sb.append( "\n" );
					}
					
					mesg = sb.toString();
				} else {
					mesg = "You are first user!!"; 
					
				}
				
				JOptionPane.showMessageDialog( null, mesg, "User Rank", JOptionPane.INFORMATION_MESSAGE );
				
			}
		});
	}

	/* *****************
	 * set set menu
	 */
	private void setMenu() {
		menuBar = new JMenuBar();
		
		menuBar.add( viewMenu = new JMenu( "View" ) );
		
		viewMenu.add( viewItem = new JMenuItem( "View_Rank") );
		viewMenu.addSeparator();
		viewMenu.add( exitItem = new JMenuItem( "Exit" ) );
		
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
	 * - gameTopPane
	 * - gameCenterPane
	 * - gameBottomPane
	 */
	private void setGameCard() {
		gameCard = new JPanel( new BorderLayout() );
		
		// set game panels
		gameCard.add( gameTopPane = new JPanel( new GridLayout( 1,  2 ) ), BorderLayout.NORTH );
		
		gameCenterPane = new JPanel() {

			@Override public void paint(Graphics g) {
				Image img = createImage( (int) this.getSize().getWidth(),
										 (int) this.getSize().getHeight() );
				
				Graphics img_g = img.getGraphics();
				
				Iterator<WordVO> it = getCloneWords().iterator();
				
				while( it.hasNext() ) {
					WordVO vo = it.next();
					img_g.setColor( vo.color );
					img_g.drawString( vo.text, vo.x, vo.y );
				}
				
				g.drawImage( img, 0, 0, this );
			}
		};
		
		gameCard.add( gameCenterPane, BorderLayout.CENTER );
		
		gameCard.add( gameBottomPane = new JPanel(), BorderLayout.SOUTH );
		
		// set top pane
		gameTopPane.add( scoreLabel = new JLabel(), BorderLayout.WEST );
		
		JPanel leftPane = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
		leftPane.add( startBtn = new JButton( "Start" ) );
		gameTopPane.add( leftPane, BorderLayout.EAST );
		
		// JLabel font setup
		startBtn.setFont( font );
		scoreLabel.setFont( font );
		scoreLabel.setForeground( Color.BLUE );
		
		scoreLabel.setText( String.valueOf( score ) );
		
		// set bottom pane
		gameBottomPane.add( wordInputTextField = new JTextField( 10 ) );
		gameBottomPane.add( confirmBtn = new JButton( "Confirm" ) );
		
		wordInputTextField.setFont( font );

		// final add gameCard
		add( "game", gameCard );
	}
	
	ArrayList<WordVO> getCloneWords()	{
		return (ArrayList<wordBreaker.WordVO>) wordVoList.clone();
	}
	
	// set word data list from file
	void setWordDataList() {
		wordDataList = new ArrayList<>();
		
		FileInputStream fis = null;
		BufferedReader br = null;
		
		try {
			fis = new FileInputStream( new File( wordFilename ) );
			br = new BufferedReader( new InputStreamReader( fis ) );
			
			String line = null;
			
			while( ( line = br.readLine() ) != null ) {
				wordDataList.add( line.trim() );
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
				try {
					if( fis != null ) fis.close();
					if( br != null ) br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	void saveAndExit() {
		
		if( !isStartButtonClicked ) {
			// not playing game
			return;
		}
		
		loadRankdDataFile();
		
		// check duplicate username
		boolean existUsername = false;
		for( int idx = 0; idx < ranklist.size(); idx++ ) {
			if( ranklist.get( idx ).getName().equals( username ) ) {
				ranklist.get( idx ).setScore( score );
				existUsername = true;
				break;
			}
		}
		
		if( !existUsername ) {
			ranklist.add( new UserRank( username, score ) );
		}

		// save object
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		System.out.println( ranklist.toString() );
		
		try {
			fos = new FileOutputStream( new File( savefile ) );
			oos = new ObjectOutputStream( fos );
			
			oos.writeObject( ranklist );
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
				try {
					if( fos != null ) fos.close();
					if( oos != null ) oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		// exit
		System.exit( 0 );
	}
	
	boolean loadRankdDataFile() {
		
		File file = new File( savefile );
		
		if( !file.exists() ) {
			return false;
		}
		
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		try {
			fis = new FileInputStream( new File( savefile ) );
			ois = new ObjectInputStream( fis );
			
			Object o = ois.readObject();
			
			if( o instanceof ArrayList ) {
				ranklist = (ArrayList<UserRank>) o;
			}
			
			// each score data sort
			Collections.sort( ranklist );
			
		} catch (FileNotFoundException fnfe) {
			// TODO Auto-generated catch block
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			// TODO Auto-generated catch block
			ioe.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			// TODO Auto-generated catch block
			cnfe.printStackTrace();
		} finally {
				try {
					if( fis != null ) fis.close();
					if( ois != null ) ois.close();
				} catch (IOException ioe) {
					// TODO Auto-generated catch block
					ioe.printStackTrace();
				}
		}
		
		return true;
	}

	public static void main(String[] args) {
		new WordBreaker();

	}
}
