import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	static final int DELAY = 75; // higher the number, the slower the game 
	
	// Holds the coordinates (x,y) of the snake
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyparts = 6;
	
	// Apple
	int applesEaten;
	int appleX;
	int appleY;
	
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	GamePanel() {
		random  = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	public void startGame() {
		newApple();
		running = true; 
		timer = new Timer(DELAY, this);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		
		if (running) {
		
			// This for loop draws lines for us to see the outline of the snake
			for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
			}	
			
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			// Iterates through all of the body parts of the snake
			// i = the head of the snake
			for (int i = 0; i < bodyparts; i++) {
				if (i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(45, 180, 0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			
			// Current score text
			g.setColor(Color.red);
			g.setFont(new Font("Int Free", Font.BOLD, 75));
			
			// Font Metrics - used to line up the text
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
		} else {
			gameOver(g);
		}
	}
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
	}
	
	public void move() {
		for (int i = bodyparts; i > 0; i--) {
			// Shifts our 'body parts' of the snake over by one
			x[i] = x[i-1];		
			y[i] = y[i-1];
		}
		
		switch(direction) {
		
		// Up
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
			
		// Down
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
			
		//Left
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
			
		// Right 
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break; 
		}
	}
	
	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyparts++;
			applesEaten++;
			newApple();
		}
	}
	
	public void checkCollisions() {
		
		// Checks if the head of the snake collides with its body
		for (int i = bodyparts; i > 0; i--) {
			if ((x[0] == x[i]) && y[0] == y[i]) {
				running = false;
			}
		}
		
		// Checks if head touches left border
		if (x[0] < 0) {
			running = false;
		}
		
		// Checks if head touches right border
		if (x[0] > SCREEN_WIDTH) {
			running = false;
		}
		
		// Checks if head touches top border
		if (y[0] < 0) {
			running = false;
		}
				
		// Checks if head touches bottom border
		if (y[0] > SCREEN_HEIGHT) {
			running = false;
		}
	
		// The timer stops when our snake goes out of bounds
		if (!running) 
			timer.stop();
		
	}
	
	public void gameOver(Graphics g) {
		
		// Current score text
		g.setColor(Color.red);
		g.setFont(new Font("Int Free", Font.BOLD, 75));
		
		// Font Metrics - used to line up the text
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
	
		
		// Game Over text
		g.setColor(Color.red);
		g.setFont(new Font("Int Free", Font.BOLD, 75));
		
		// Font Metrics - used to line up the text in the middle of the screen
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
		
	}
	
	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'R')
					direction = 'L';
				break;
			case KeyEvent.VK_RIGHT:
				if (direction != 'L')
					direction = 'R';
				break;
			case KeyEvent.VK_UP:
				if (direction != 'D')
					direction = 'U';
				break;
			case KeyEvent.VK_DOWN:
				if (direction != 'U')
					direction = 'D';
				break;
			}
		}
	}
}



















