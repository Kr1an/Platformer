package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class HUD implements Serializable {
	
	private Player player;
	
	private transient BufferedImage image;
	private Font font;
	
	public HUD(Player p) {
		player = p;
		loadSprites();

	}
	private void loadSprites(){
		try {
			image = ImageIO.read(
					getClass().getResourceAsStream(
							"/HUD/hud.gif"
					)
			);
			font = new Font("Arial", Font.PLAIN, 14);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void draw(Graphics2D g) {
		
		g.drawImage(image, 0, 10, null);
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString(
			player.getHealth() + "/" + player.getMaxHealth(),
			30,
			25
		);
		g.drawString(
			player.getFire() / 100 + "/" + player.getMaxFire() / 100,
			30,
			45
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













