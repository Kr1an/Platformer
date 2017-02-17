package Entity;

import TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class DoubleJumpPoint extends MapObject {

    private final int RESET_DURATION = 8000;

    private BufferedImage[] sprites;
    private long elapsed;
    private boolean used;

    public DoubleJumpPoint(TileMap tm) {

        super(tm);

        used = false;

        moveSpeed = 0;

        width = 30;
        height = 30;
        cwidth = 4;
        cheight = 4;

        // load sprites
        try {

            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/Other/DoubleJumpPoint.png"
                    )
            );

            sprites = new BufferedImage[3];
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


    public void setUsed(){

        used = true;
        elapsed = System.nanoTime();
    }
    public boolean getUsed(){
        return used;
    }

    public void update() {
        if(used && (System.nanoTime() - elapsed)/1000000 >= RESET_DURATION){
            used = false;
        }
        animation.update();
    }

    public void draw(Graphics2D g) {
            if(!used){
                setMapPosition();
                super.draw(g);
            }else{

            }


    }

}

