import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class RedGhost extends Rectangle{
	private static final long serialVersionUID = 1L;
	private int right = 0, left = 1, up = 2, down = 3;
	private int dir = -1;
	public Random randomGen;
	private int time = 0;
	private int targetTime = 60*5;
	private int spd = 2;
	private int lastDir = -1;
	
	private boolean ghostEaten = false;
	
	//FSM
	FSM redGhostState = new FSM();
	ChasingState chasingState = new ChasingState();
	RetreatState retreatState = new RetreatState();
	RandomState randomState = new RandomState();
	PathfindingState pathfindingState = new PathfindingState();
	

	public RedGhost(int x, int y){
		randomGen = new Random();
		setBounds(x, y, 32, 32);
		dir = randomGen.nextInt(4);
		redGhostState.setState(chasingState);
	}
	
	public void tick(){
		
		ghostEaten = Game.player.eatGhost;
		
		if(ghostEaten){
			redGhostState.setState(retreatState);
		}
		
		if(redGhostState.getState() == randomState){
			
			if(dir == right){
				if(canMove(x+spd, y)){
					if(randomGen.nextInt(100) < 50) x += spd;
				}else{
					dir = randomGen.nextInt(4);
				}
			}else if(dir == left){
				if(canMove(x-spd, y)){
					if(randomGen.nextInt(100) < 50) x -= spd;
				}else{
					dir = randomGen.nextInt(4);
				}
			}else if(dir == up){
				if(canMove(x, y-spd)){
					if(randomGen.nextInt(100) < 50) y -= spd;
				}else{
					dir = randomGen.nextInt(4);
				}
			}else if(dir == down){
				if(canMove(x, y+spd)){
					if(randomGen.nextInt(100) < 50) y += spd;
				}else{
					dir = randomGen.nextInt(4);
				}
			}
			
			time++;
			
			if(time == targetTime){
				redGhostState.setState(chasingState);
				//state = smart;
				time = 0;
			}
			
		}else if(redGhostState.getState() == chasingState){
			//Follow the player
			boolean move = false;
			if(x < Game.player.x){
				if(canMove(x+spd, y)){
					x += spd;
					move = true;
					lastDir = right;
				}
			}
			if(x > Game.player.x){
				if(canMove(x-spd, y)){
					x -= spd;
					move = true;
					lastDir = left;
				}
			}
			if(y < Game.player.y){
				if(canMove(x, y+spd)){
					y += spd;
					move = true;
					lastDir = down;
				}
			}
			if(y > Game.player.y){
				if(canMove(x, y-spd)){
					y -= spd;
					move = true;
					lastDir = up;
				}
			}
			
			if(x == Game.player.x && y == Game.player.y) move = true;
			
			if(!move){
				redGhostState.setState(randomState);
				//state = random;
			}	
			
			time++;
			
			if(time == targetTime){
				redGhostState.setState(chasingState);
				//state = smart;
				time = 0;
			}
			
		}else if(redGhostState.getState() == pathfindingState){

			if(lastDir == right){
				if(y < Game.player.y){
					if(canMove(x, y+spd)){
						y+=spd;
						redGhostState.setState(chasingState);
						//state = smart;
					}
				}else{
					if(canMove(x, y-spd)){
						y-=spd;
						redGhostState.setState(chasingState);
						//state = smart;
					}
				}
				
				if(canMove(x+spd, y)){
					x+=spd;
				}
			}else if(lastDir == left){
				if(y < Game.player.y){
					if(canMove(x, y+spd)){
						y+=spd;
						redGhostState.setState(chasingState);
						//state = smart;
					}
				}else{
					if(canMove(x, y-spd)){
						y-=spd;
						redGhostState.setState(chasingState);
						//state = smart;
					}
				}
				
				if(canMove(x-spd, y)){
					x-=spd;
				}
			}else if(lastDir == up){
				if(x < Game.player.x){
					if(canMove(x+spd, y)){
						x+=spd;
						redGhostState.setState(chasingState);
						//state = smart;
					}
				}else{
					if(canMove(x, y-spd)){
						x-=spd;
						redGhostState.setState(chasingState);
						//state = smart;
					}
				}
				
				if(canMove(x-spd, y)){
					y-=spd;
				}
			}else if(lastDir == down){
				if(x < Game.player.x){
					if(canMove(x+spd, y)){
						x+=spd;
						redGhostState.setState(chasingState);
						//state = smart;
					}
				}else{
					if(canMove(x, y-spd)){
						x-=spd;
						redGhostState.setState(chasingState);
						//state = smart;
					}
				}
				
				if(canMove(x+spd, y)){
					y+=spd;
				}
			}
			
			time++;
			
			if(time == targetTime){
				redGhostState.setState(chasingState);
				//state = smart;
				time = 0;
			}
			
		}else if(redGhostState.getState() == retreatState){

			boolean move = false;
			if(x < Game.player.x){
				if(canMove(x-spd, y)){
					x -= spd;
					move = true;
					lastDir = right;
				}
			}
			if(x > Game.player.x){
				if(canMove(x+spd, y)){
					x += spd;
					move = true;
					lastDir = left;
				}
			}
			if(y < Game.player.y){
				if(canMove(x, y-spd)){
					y -= spd;
					move = true;
					lastDir = down;
				}
			}
			if(y > Game.player.y){
				if(canMove(x, y+spd)){
					y += spd;
					move = true;
					lastDir = up;
				}
			}
			
			if(x == Game.player.x && y == Game.player.y) move = true;
			
			
			time++;
			
			if(time == targetTime){
				ghostEaten = false;
				redGhostState.setState(chasingState);
				//state = smart;
				time = 0;
			}
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
	
	public void render(Graphics g) {
		
		if(redGhostState.getState() == retreatState){
			g.drawImage(Texture.redGhost[1], x, y, width, height, null);
		}else{
			g.drawImage(Texture.redGhost[0], x, y, width, height, null);
		}
		
	}

}
