import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class GreenApple extends Rectangle{
	
	public GreenApple(int x, int y){
		setBounds(x+11, y+11, 9, 9);
	}
	
	public void render(Graphics g){
		g.setColor(Color.green);
		g.fillRect(x, y, width, height);
	}
}
