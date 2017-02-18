package TileMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Tile implements Serializable {
	
	private transient BufferedImage image;
	private int type;
	
	// tile types
	public static final int NORMAL = 0;
	public static final int BLOCKED = 1;
	public static final int KILLING = 3;
	public static final int SAVING = 4;
	public static final int OTHER = 5;
	
	public Tile(BufferedImage image, int type) {
		this.image = image;
		this.type = type;
	}
	
	public BufferedImage getImage() { return image; }
	public int getType() { return type; }

	
}
