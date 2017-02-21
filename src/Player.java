import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Player extends Rectangle {

	private static final long serialVersionUID = 1L;
	
	public boolean right, left, up, down; //My control keys
	private int speed = 4; //Player's speed of character movement
	
	private int time = 0, targetTime = 10;
	public int imageIndex = 0;
	
	//Variables for sprite flip
	private int lastDir = 1;
	
	//Checks if pac man can eat ghosts uses an FSM
	FSM pacmanState = new FSM();
	NormalPlayerState normalPlayerState = new NormalPlayerState();
	AteGreenAppleState ateGreenAppleState = new AteGreenAppleState();
	public static boolean eatGhost = false;
	
	//power up timer
	private int puTimer = 0;
	private int timeUp = 60*5; //5 seconds

	public Player(int x, int y){
		setBounds(x, y, 32, 32);
		pacmanState.setState(normalPlayerState);
	}
	
	public void tick(){
		//Checks for key press and applies the speed to the playable character
		if(right && canMove(x+speed, y)){
			x += speed;
			lastDir = 1;
		}
		if(left && canMove(x-speed, y)){
			x -= speed;
			lastDir = 2;
		}
		if(up && canMove(x, y-speed)){
			y -= speed;
			lastDir = 3;
		}
		if(down && canMove(x, y+speed)){
			y += speed;
			lastDir = 4;
		}
		
		Level level = Game.level;
		//Removes Green power up apples when collided
		for(int i = 0; i < level.greenApples.size(); i++){
			
			if(this.intersects(level.greenApples.get(i))){
				level.greenApples.remove(i);
				pacmanState.setState(ateGreenAppleState);
				Game.score += 50;
				break;
			}
		}
		
		//Removes apples when player collides
		for(int i = 0; i < level.apples.size(); i++){
			
			if(this.intersects(level.apples.get(i))){
				level.apples.remove(i);
				Game.score += 10;
				break;
			}
		}
		if(level.apples.size() == 0){
			//Win condition met!
			Game.player = new Player(0,0);
			Game.level = new Level("/map/map.png");
			return;
		}
		
		//Detects collision with the enemy, player dies when normal
		if(pacmanState.getState() == normalPlayerState){
			
			eatGhost = false;
			
			for(int i = 0; i < Game.level.redGhost.size(); i++){
				RedGhost rg = Game.level.redGhost.get(i);
				if(rg.intersects(this)) Game.STATE = Game.PAUSE_SCREEN;
			}
			
			for(int i = 0; i < Game.level.blueGhost.size(); i++){
				BlueGhost bg = Game.level.blueGhost.get(i);
				if(bg.intersects(this)) Game.STATE = Game.PAUSE_SCREEN;
			}
			
			for(int i = 0; i < Game.level.orangeGhost.size(); i++){
				OrangeGhost og = Game.level.orangeGhost.get(i);
				if(og.intersects(this)) Game.STATE = Game.PAUSE_SCREEN;
			}
			
			for(int i = 0; i < Game.level.pinkGhost.size(); i++){
				PinkGhost pg = Game.level.pinkGhost.get(i);
				if(pg.intersects(this)) Game.STATE = Game.PAUSE_SCREEN;
			}
		}else if(pacmanState.getState() == ateGreenAppleState){
			
			eatGhost = true;
			
			for(int i = 0; i < Game.level.redGhost.size(); i++){
				if(this.intersects(level.redGhost.get(i))){
					level.redGhost.remove(i);
					Game.score += 200;
					break;
				}
			}
			
			for(int i = 0; i < Game.level.blueGhost.size(); i++){
				if(this.intersects(level.blueGhost.get(i))){
					level.blueGhost.remove(i);
					Game.score += 200;
					break;
				}
			}
			
			for(int i = 0; i < Game.level.orangeGhost.size(); i++){
				if(this.intersects(level.orangeGhost.get(i))){
					level.orangeGhost.remove(i);
					Game.score += 200;
					break;
				}
			}
			
			for(int i = 0; i < Game.level.pinkGhost.size(); i++){
				if(this.intersects(level.pinkGhost.get(i))){
					level.pinkGhost.remove(i);
					Game.score += 200;
					break;
				}
			}
			
			System.out.println("Power up!"); //Debugs powerup cooldown
			
			
			puTimer++;
			
			if(puTimer == timeUp){
				pacmanState.setState(normalPlayerState);
				puTimer = 0;
			}
			
		}
		
		
		time++; //Increment time for animation
		
		if(time == targetTime){
			time = 0;
			imageIndex++;
		}
		
		
	}
	
	public boolean canMove(int nextx, int nexty){
		
		Rectangle bounds = new Rectangle(nextx, nexty, width, height);
		Level level = Game.level;
		
		for(int xx = 0; xx < level.tiles.length; xx++){
			for(int yy = 0; yy < level.tiles[0].length; yy++){
				if(level.tiles[xx][yy] != null){
					if(bounds.intersects(level.tiles[xx][yy])){
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	public void render(Graphics g){
		//Draw the sprite and animation for player
		if(lastDir == 1){
			g.drawImage(Texture.player[imageIndex%2], x, y, width, height, null);
		}
		if(lastDir == 2){
			g.drawImage(Texture.player[imageIndex%2], x+32, y, -width, height, null);
		}
		if(lastDir == 3){
			g.drawImage(Texture.playerUp[imageIndex%2], x, y, width, height, null);
		}
		if(lastDir == 4){
			g.drawImage(Texture.playerDown[imageIndex%2], x, y, width, height, null);
		}
		
	}

}
