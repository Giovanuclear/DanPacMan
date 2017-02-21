import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Texture {

	public static BufferedImage[] player; //Uses an array because there is multiple sprites for animations
	public static BufferedImage[] playerUp;
	public static BufferedImage[] playerDown;
	public static BufferedImage[] redGhost;
	public static BufferedImage[] blueGhost;
	public static BufferedImage[] orangeGhost;
	public static BufferedImage[] pinkGhost;
	public static BufferedImage spritesheet;
	
	public Texture(){
		
		try {
			spritesheet = ImageIO.read(getClass().getResourceAsStream("/sprites/spritesheet.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		player = new BufferedImage[4]; //Initialize player texture array
		playerUp = new BufferedImage[4]; //Initialize player texture array
		playerDown = new BufferedImage[4]; //Initialize player texture array
		redGhost = new BufferedImage[2]; //Initialize player texture array
		blueGhost = new BufferedImage[2]; //Initialize player texture array
		orangeGhost = new BufferedImage[2]; //Initialize player texture array
		pinkGhost = new BufferedImage[2]; //Initialize player texture array
		
		//Player textures
		player[0] = getSprite(0, 0);
		player[1] = getSprite(16, 0);
		
		playerUp[0] = getSprite(0, 0);
		playerUp[1] = getSprite(32, 0);
		
		playerDown[0] = getSprite(0, 0);
		playerDown[1] = getSprite(48, 0);
		
		//Ghost textures
		redGhost[0] = getSprite(0, 16);
		redGhost[1] = getSprite(64, 16);
		blueGhost[0] = getSprite(16, 16);
		blueGhost[1] = getSprite(64, 16);
		orangeGhost[0] = getSprite(32, 16);
		orangeGhost[1] = getSprite(64, 16);
		pinkGhost[0] = getSprite(48, 16);
		pinkGhost[1] = getSprite(64, 16);
	}
	
	public BufferedImage getSprite(int xx, int yy){
		return spritesheet.getSubimage(xx, yy, 16, 16);
	}
	
}
