import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Game extends Canvas implements Runnable, KeyListener {

	private boolean isRunning = false;
	public static final int WIDTH = 640, HEIGHT = 480;
	public static String TITLE = "Dan's Pac-Man";
	
	public Thread thread;
	
	public static int score = 0;
	
	public static Player player;
	public static Level level;
	
	//Menu States
	public static final int PAUSE_SCREEN = 0, GAME = 1;
	public static int STATE = -1;
	
	//Reset the game
	public boolean isEnter = false;
	
	public Game(){
		Dimension dimension = new Dimension(Game.WIDTH, Game.HEIGHT);
		setPreferredSize(dimension);
		setMinimumSize(dimension);
		setMaximumSize(dimension);
		
		addKeyListener(this);
		
		STATE = PAUSE_SCREEN;
		
		new Texture();
	}
	
	public synchronized void start(){
		if(isRunning) return;
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop(){
		if(!isRunning) return;
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void tick(){
		if(STATE == GAME){
			player.tick();
			level.tick();
		}else if(STATE == PAUSE_SCREEN){
			if(isEnter){
				//Restarts the game
				isEnter = false;
				player = new Player(Game.WIDTH/2, Game.HEIGHT/2);
				level = new Level("/map/map.png");
				STATE = GAME;
			}
		}
	}
	
	private void render(){
		BufferStrategy bs = getBufferStrategy(); //Render buffer
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		if(STATE == GAME){
			player.render(g);
			level.render(g);
			g.setColor(Color.white);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
			g.drawString("Score: " + score, 10, 22);
		}else if(STATE == PAUSE_SCREEN){
			//This is used to draw the pause/welcome screen
			int boxWidth = 500;
			int boxHeight = 200;
			int xx = Game.WIDTH/2 - boxWidth/2;
			int yy = Game.HEIGHT/2 - boxHeight/2;
			
			g.setColor(new Color(0,0,150));
			g.fillRect(xx, yy, boxWidth, boxHeight);
			g.setColor(Color.white);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 26));
			g.drawString("WELCOME TO PACMAN", xx + 90, yy - 5);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
			g.drawString("USE YOUR ARROW KEYS TO CONTROL PACMAN", xx + 10, yy + 20);
			g.drawString("COLLECT ALL OF THE PILLS TO WIN", xx + 10, yy + 40);
			g.drawString("USE THE GREEN POWER PILLS TO GIVE", xx + 10, yy + 60);
			g.drawString("PACMAN THE ABILITY TO CONSUME GHOSTS", xx + 10, yy + 80);
			g.drawString("FOR A SHORT TIME", xx + 10, yy + 100);
			g.drawString("GOOD LUCK!", xx + 10, yy + 120);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 26));
			g.drawString("PRESS ENTER TO START", xx + 90, yy + 225);
		}
		g.dispose();
		bs.show();
	}
	
	@Override
	public void run(){
		requestFocus(); //Allows you to avoid clicking on the game window when the program is started
		int fps = 0;
		double timer = System.currentTimeMillis();
		long lastTime = System.nanoTime();
		double delta = 0;
		double targetTick = 60.0; //This targets the game to run at 60 fps
		double ns = 1000000000/targetTick; //nano seconds
		
		
		while(isRunning){
			long now = System.nanoTime();
			delta += (now-lastTime)/ns;
			lastTime = now;
			
			while(delta >= 1){
				tick();
				render();
				fps++; //Counts a frame
				delta--;
			}
			if(System.currentTimeMillis() - timer >= 1000){
				//System.out.println(fps);
				fps = 0;
				timer+=1000;
			}
		}
		
		stop();
	}
	
	public static void main(String[] args){
		Game game = new Game();
		JFrame frame = new JFrame();
		frame.setTitle(Game.TITLE);
		frame.add(game);
		frame.setResizable(false);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);
		
		game.start();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(STATE == GAME){
			if(e.getKeyCode() == KeyEvent.VK_RIGHT)player.right = true;
			if(e.getKeyCode() == KeyEvent.VK_LEFT)player.left = true;
			if(e.getKeyCode() == KeyEvent.VK_UP)player.up = true;
			if(e.getKeyCode() == KeyEvent.VK_DOWN)player.down = true;
		}else if(STATE == PAUSE_SCREEN){
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				isEnter = true;
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)player.right = false;
		if(e.getKeyCode() == KeyEvent.VK_LEFT)player.left = false;
		if(e.getKeyCode() == KeyEvent.VK_UP)player.up = false;
		if(e.getKeyCode() == KeyEvent.VK_DOWN)player.down = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		//Probably won't use this in a game
	}

}
