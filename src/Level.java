import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Level {

	public int width;
	public int height;
	
	public Tile[][] tiles;
	
	public List<Apple> apples; //Stores regular apples
	public List<GreenApple> greenApples; //Stores Green powerup apples
	public List<RedGhost> redGhost;
	public List<BlueGhost> blueGhost;
	public List<OrangeGhost> orangeGhost;
	public List<PinkGhost> pinkGhost;
	
	public Level(String path){
		apples = new ArrayList<>();
		greenApples = new ArrayList<>();
		redGhost = new ArrayList<>();
		blueGhost = new ArrayList<>();
		orangeGhost = new ArrayList<>();
		pinkGhost = new ArrayList<>();
		
		try {
			BufferedImage map = ImageIO.read(getClass().getResourceAsStream(path));
			this.width = map.getWidth();
			this.height = map.getHeight();
			int[] pixels = new int[width*height];
			tiles = new Tile[width][height];
			map.getRGB(0, 0, width, height, pixels, 0, width);
			
			for(int xx = 0; xx < width; xx++){
				for(int yy = 0; yy < height; yy++){
						int val = pixels[xx + (yy*width)];
						//Place special tiles by using color detection
						if(val == 0xff000000){
							//Tiles
							tiles[xx][yy] = new Tile(xx*32, yy*32);
						}else if(val == 0xff0000ff){
							//Player
							Game.player.x = xx*32;
							Game.player.y = yy*32;
						}else if(val == 0xffff0000){
							//Enemy
							redGhost.add(new RedGhost(xx*32, yy*32));
						}else if(val == 0xff00ffff){
							//Enemy
							blueGhost.add(new BlueGhost(xx*32, yy*32));
						}else if(val == 0xffffff00){
							//Enemy
							orangeGhost.add(new OrangeGhost(xx*32, yy*32));
						}else if(val == 0xffFF00FF){
							//Enemy
							pinkGhost.add(new PinkGhost(xx*32, yy*32));
						}else if(val == 0xff00ff00){
							//Super fruit
							greenApples.add(new GreenApple(xx*32, yy*32));
						}else{
							//Fruit
							apples.add(new Apple(xx*32, yy*32));
						}
					}
				}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void tick(){
		for(int i = 0; i < redGhost.size(); i++){
			redGhost.get(i).tick();
		}
		for(int i = 0; i < blueGhost.size(); i++){
			blueGhost.get(i).tick();
		}
		for(int i = 0; i < orangeGhost.size(); i++){
			orangeGhost.get(i).tick();
		}
		for(int i = 0; i < pinkGhost.size(); i++){
			pinkGhost.get(i).tick();
		}
		
	}
	
	public void render(Graphics g){
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				if(tiles[x][y] != null)tiles[x][y].render(g);
			}
		}
		
		for(int i = 0; i < apples.size(); i++){
			apples.get(i).render(g);
		}
		
		for(int i = 0; i < greenApples.size(); i++){
			greenApples.get(i).render(g);
		}
		
		for(int i = 0; i < redGhost.size(); i++){
			redGhost.get(i).render(g);
		}
		
		for(int i = 0; i < blueGhost.size(); i++){
			blueGhost.get(i).render(g);
		}
		
		for(int i = 0; i < orangeGhost.size(); i++){
			orangeGhost.get(i).render(g);
		}
		
		for(int i = 0; i < pinkGhost.size(); i++){
			pinkGhost.get(i).render(g);
		}

	}
	
}
