import java.awt.*;
import java.awt.event.*; 
import javax.swing.*;
import java.util.Random;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{
	
	static final int SCREEN_WIDTH = 500; 
	static final int SCREEN_HEIGHT = 500; 
	static final int UNIT_SIZE = 20;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE; 
	static final int DELAY = 100; 

    final int x[] = new int[GAME_UNITS]; //x-coordinates of body parts 
    final int y[] = new int[GAME_UNITS]; //y-coordinates of body parts 

    int bodyParts = 6;
    int applesEaten = 0; 
    int applex; 
    int appley; 
    
    char direction = 'R';
    
    boolean running = false; 

    Timer timer; 

    Random random; 
	
	GamePanel() {
		random = new Random(); 
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
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

        if(running == true) {

            /* //For grid display 
            for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
                g.setColor(Color.gray);
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }
            */

            g.setColor(Color.red);
            g.fillOval(applex, appley, UNIT_SIZE, UNIT_SIZE);

            for(int i = 0; i < bodyParts; i++) {
                if(i==0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }

                else {
                    g.setColor(new Color(0, 102, 0));
                    //g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255))); //for multicolor snake 
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            g.setColor(Color.red);
            g.setFont(new Font("Monaco", Font.BOLD, 20));
            FontMetrics metrics = getFontMetrics(g.getFont()); 
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)), g.getFont().getSize());
        }

        else{
            gameOver(g);
            //restart button appears
        }
	}

    public void newApple() {
        applex = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        appley = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
    }
	
	public void move() {
		for(int i = bodyParts; i > 0; i--) { //shifting the body parts of the snake 
            x[i] = x[i - 1]; //shifting by one spot
            y[i] = y[i - 1]; 
        }

        switch(direction) {
        case 'U':
            y[0] = y[0] - UNIT_SIZE; 
            break; 
        case 'D':
            y[0] = y[0] + UNIT_SIZE; 
            break; 
        case 'L':
            x[0] = x[0] - UNIT_SIZE; 
            break; 
        case 'R':
            x[0] = x[0] + UNIT_SIZE; 
            break; 
        }
	}
	
	public void checkApple() {
		if((x[0] == applex) && (y[0] == appley)) {
            bodyParts++; 
            applesEaten++; 
            newApple();
        }
	}
	
	public void checkCollisions() {
		for(int i = bodyParts; i > 0 ; i--) {
            //head collided with the body
            if((x[0] == x[i]) && (y[0] == y[i])) { 
                running = false; 
            }
        }

        //head touches the left border
        if(x[0] < 0) {
            running = false; 
        }

        //head touches the right border
        if(x[0] > SCREEN_WIDTH) {
            running = false; 
        }

        //head touches the top boarder
        if(y[0] < 0) {
            running = false; 
        }

        //head touches the bottom boarder 
        if(y[0] > SCREEN_HEIGHT) {
            running = false; 
        }

        if(running == false) {
            timer.stop();
        }
    }
	
	public void gameOver(Graphics g) {

        //score
        g.setColor(Color.orange);
        g.setFont(new Font("Monaco", Font.BOLD, 40));
        FontMetrics metrics_1 = getFontMetrics(g.getFont()); 
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics_1.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());

		//Game Over Text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics_2 = getFontMetrics(g.getFont()); 
        g.drawString("Game Over", (SCREEN_WIDTH - metrics_2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if(running == true) {
            move(); 
            checkApple();
            checkCollisions();
        }
		repaint(); 
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if(direction != 'R') {
                    direction = 'L'; 
                }
                break; 
            case KeyEvent.VK_RIGHT: 
                if(direction != 'L') {
                    direction = 'R';
                }
                break; 
            case KeyEvent.VK_UP:
                if(direction != 'D') {
                    direction = 'U'; 
                }
                break; 
            case KeyEvent.VK_DOWN:
                if(direction != 'U') {
                    direction = 'D'; 
                }
                break; 
            }
		}
	}
}