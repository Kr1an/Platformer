package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class Explosion implements Serializable {
	
	private int x;
	private int y;
	private int xmap;
	private int ymap;
	
	private int width;
	private int height;
	
	private transient Animation animation;
	private transient BufferedImage[] sprites;
	
	private boolean remove;
	
	public Explosion(int x, int y) {
		
		this.x = x;
		this.y = y;
		
		width = 30;
		height = 30;
		
		loadSprites();
		

		
	}
	private void loadSprites(){
		try {

			BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/Enemies/explosion.gif"
					)
			);

			sprites = new BufferedImage[6];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
						i * width,
						0,
						width,
						height
				);
			}

			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(70);

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void update() {
		animation.update();
		if(animation.hasPlayedOnce()) {
			remove = true;
		}
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void setMapPosition(int x, int y) {
		xmap = x;
		ymap = y;
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(
			animation.getImage(),
			x + xmap - width / 2,
			y + ymap - height / 2,
			null
		);
	}

	// Serializer staff

	private void readObject(ObjectInputStream input)
			throws IOException, ClassNotFoundException {
		// deserialize the non-transient data members first;
		input.defaultReadObject();

		loadSprites();
	}

	
}

















