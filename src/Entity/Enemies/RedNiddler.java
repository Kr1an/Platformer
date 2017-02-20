package Entity.Enemies;

import Entity.*;
import TileMap.TileMap;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.*;

import javax.imageio.ImageIO;

public class RedNiddler extends Enemy implements Serializable {

    private transient BufferedImage[] sprites;

    public RedNiddler(TileMap tm) {

        super(tm);

        moveSpeed = 0.9;
        maxSpeed = 0.9;
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;

        width = 16;
        height = 16;
        cwidth = 14;
        cheight = 14;

        health = maxHealth = 20;
        damage = 1;

        // load sprites
        loadSprites();




        right = true;
        facingRight = true;

    }

    public void loadSprites(){
        try {

            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/Enemies/red-niddler.png"
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

    private void getNextPosition() {

        // movement
        if(left) {
            dx -= moveSpeed;
            if(dx < -maxSpeed) {
                dx = -maxSpeed;
            }
        }
        else if(right) {
            dx += moveSpeed;
            if(dx > maxSpeed) {
                dx = maxSpeed;
            }
        }

        // falling
        if(falling) {
            dy += fallSpeed;
        }

    }

    public void update() {

        // update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        // check flinching
        if(flinching) {
            long elapsed =
                    (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed > 400) {
                flinching = false;
            }
        }

        // if it hits a wall, go other direction
        if(right && dx == 0) {
            right = false;
            left = true;
            facingRight = false;
        }
        else if(left && dx == 0) {
            right = true;
            left = false;
            facingRight = true;
        }

        // update animation
        animation.update();

    }

    public void draw(Graphics2D g) {

        //if(notOnScreen()) return;

        setMapPosition();

        super.draw(g);

    }

    // Serializer staff

    private void readObject(ObjectInputStream input)
            throws IOException, ClassNotFoundException {
        // deserialize the non-transient data members first;
        input.defaultReadObject();

        loadSprites();
    }

}
