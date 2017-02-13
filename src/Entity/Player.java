package Entity;

import TileMap.*;
import Audio.AudioPlayer;


import java.io.Console;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Player extends MapObject {

	private int jumpCount;
	private int MAX_JUMP_TIMES = 2;

	// player stuff
	private int health;
	private int maxHealth;
	private int fire;
	private int maxFire;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;
	private long jumpTimer;
	private boolean jumpedOnce;
	private boolean jumpedTwice;
	
	// fireball
	private boolean firing;
	private int fireCost;
	private int fireBallDamage;
	private ArrayList<FireBall> fireBalls;
	
	// scratch
	private boolean scratching;
	private int scratchDamage;
	private int scratchRange;
	
	// gliding
	private boolean gliding;
	
	// animations
	private ArrayList<BufferedImage[]> sprites;

	//animationsOh
	private ArrayList<BufferedImage[]> spritesOh;

	private final int[] numFramesOh = {
		3, 4, 4, 4
	};
	private final int[] numFrames = {
		2, 8, 1, 2, 4, 2, 5
	};


	//animation actions Oh

	private static final int OH_WALKING = 0;
	private static final int OH_IDLE = 1;
	private static final int OH_SCRATCHING = 2;
	private static final int OH_FALLING = 3;

	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int GLIDING = 4;
	private static final int FIREBALL = 5;
	private static final int SCRATCHING = 6;
	
	private HashMap<String, AudioPlayer> sfx;
	
	public Player(TileMap tm) {
		
		super(tm);

		jumpCount = 0;
		
		width = 14;  	//30
		height = 14; 	//30

		cwidth = 10; 	//20
		cheight = 10;	//20
		
		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;	//-4.8
		stopJumpSpeed = 0.2;
		
		facingRight = true;
		
		health = maxHealth = 5;
		fire = maxFire = 300;
		
		fireCost = 100;
		fireBallDamage = 5;
		fireBalls = new ArrayList<FireBall>();
		
		scratchDamage = 8;
		scratchRange = 20;	//40

		jumpedOnce = false;
		jumpedTwice = false;
		
		// load sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Player/playersprites.gif"
				)
			);
			
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 7; i++) {
				
				BufferedImage[] bi =
					new BufferedImage[numFrames[i]];
				
				for(int j = 0; j < numFrames[i]; j++) {
					
					if(i != SCRATCHING) {
						bi[j] = spritesheet.getSubimage(
								j * width,
								i * height,
								width,
								height
						);
					}
					else {
						bi[j] = spritesheet.getSubimage(
								j * width * 2,
								i * height,
								width * 2,
								height
						);
					}
					
				}
				
				sprites.add(bi);
				
			}

			BufferedImage spritesheetOh = ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/Player/playeroldherosprites.png"
					)
			);

			spritesOh = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 4; i++) {

				BufferedImage[] bi =
						new BufferedImage[numFramesOh[i]];

				for(int j = 0; j < numFramesOh[i]; j++) {
					bi[j] = spritesheetOh.getSubimage(
							j * width,
							i * height,
							width,
							height
					);
				}

				spritesOh.add(bi);

			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		currentAction = OH_IDLE;
		animation.setFrames(spritesOh.get(OH_IDLE));
		animation.setDelay(400);
		
		sfx = new HashMap<String, AudioPlayer>();
		sfx.put("jump", new AudioPlayer("/SFX/jump.mp3"));
		sfx.put("scratch", new AudioPlayer("/SFX/scratch.mp3"));
		
	}
	
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	public int getFire() { return fire; }
	public int getMaxFire() { return maxFire; }

	public boolean getJumpedTwice(){return jumpedTwice;}

	public boolean getJumping(){ return jumping;}
	
	public void setFiring() { 
		firing = true;
	}
	public void setScratching() {
		scratching = true;
	}
	public void setGliding(boolean b) { 
		gliding = b;
	}

	public void setJumpedOnce(boolean b){ jumpedOnce = b;}

	
	public void checkAttack(ArrayList<Enemy> enemies) {
		
		// loop through enemies
		for(int i = 0; i < enemies.size(); i++) {
			
			Enemy e = enemies.get(i);
			
			// scratch attack
			if(scratching) {
				if(facingRight) {
					if(
						e.getx() > x &&
						e.getx() < x + scratchRange && 
						e.gety() > y - height / 2 &&
						e.gety() < y + height / 2
					) {
						e.hit(scratchDamage);
					}
				}
				else {
					if(
						e.getx() < x &&
						e.getx() > x - scratchRange &&
						e.gety() > y - height / 2 &&
						e.gety() < y + height / 2
					) {
						e.hit(scratchDamage);
					}
				}
			}
			
			// fireballs
			for(int j = 0; j < fireBalls.size(); j++) {
				if(fireBalls.get(j).intersects(e)) {
					e.hit(fireBallDamage);
					fireBalls.get(j).setHit();
					break;
				}
			}
			
			// check enemy collision
			if(intersects(e)) {
				hit(e.getDamage());
			}
			
		}
		
	}
	
	public void hit(int damage) {
		if(flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
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
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		if(!falling){
			jumpedOnce = false;
			jumpedTwice =false;
			jumpCount = 0;
		}

		// cannot move while attacking, except in air
//		if(
//		(currentAction == OH_SCRATCHING || currentAction == FIREBALL) &&
//		!(jumping || falling)) {
//			dx = 0;
//		}



		if(jumping && jumpedOnce && !jumpedTwice && jumpCount == 1){
			System.out.print("sedc");

			dy = jumpStart;
			falling = true;
			jumpedTwice = true;
			jumpCount++;
			System.out.print(jumpedOnce);
			System.out.print(jumpedTwice);
		}
		else if(jumping && jumpCount < MAX_JUMP_TIMES && !jumpedTwice && jumpCount == 0 && !jumpedOnce){
			System.out.print("first");


			dy = jumpStart;
			falling = true;
			jumpCount++;
			System.out.print(jumpedOnce);
			System.out.print(jumpedTwice);

		}


		//second jumping

		
		// falling
		if(falling) {
			
			if(dy > 0 && gliding) dy += fallSpeed * 0.1;
			else dy += fallSpeed;
			
			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			
			if(dy > maxFallSpeed) dy = maxFallSpeed;
			
		}
		
	}
	
	public void update() {
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check attack has stopped
		if(currentAction == OH_SCRATCHING) {
			if(animation.hasPlayedOnce()) scratching = false;
		}
		if(currentAction == FIREBALL) {
			if(animation.hasPlayedOnce()) firing = false;
		}
		
		// fireball attack

		fire += 4;
		if(fire > maxFire) fire = maxFire;
		if(firing && currentAction != FIREBALL) {
			if(fire > fireCost) {
				fire -= fireCost;
				FireBall fb = new FireBall(tileMap, facingRight);
				fb.setPosition(x, y-2);
				fireBalls.add(fb);
			}
		}
		
		// update fireballs
		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).update();
			if(fireBalls.get(i).shouldRemove()) {
				fireBalls.remove(i);
				i--;
			}
		}
		
		// check done flinching
		if(flinching) {
			long elapsed =
				(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 1000) {
				flinching = false;
			}
		}
		
		// set animation
		if(scratching) {
			if(currentAction != OH_SCRATCHING) {
				sfx.get("scratch").play();
				currentAction = OH_SCRATCHING;
				animation.setFrames(spritesOh.get(OH_SCRATCHING));
				animation.setDelay(50);
				width = 14;//60
			}
		}
		else if(firing) {
			if(currentAction != FIREBALL) {
				currentAction = FIREBALL;
				animation.setFrames(spritesOh.get(OH_SCRATCHING));
				animation.setDelay(5);
				width = 14;//30
			}
		}
		else if(dy > 0) {
			if(gliding) {
				if(currentAction != GLIDING) {
					currentAction = GLIDING;
					animation.setFrames(spritesOh.get(OH_FALLING));
					animation.setDelay(100);
					width = 14;//30;
				}
			}
			else if(currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(spritesOh.get(OH_FALLING));
				animation.setDelay(100);
				width = 14;//30
			}
		}
		else if(dy < 0) {
			if(currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(spritesOh.get(OH_FALLING));
				animation.setDelay(100);
				width = 14;//30
			}
		}
		else if(left || right) {
			if(currentAction != OH_WALKING) {
				currentAction = OH_WALKING;
				animation.setFrames(spritesOh.get(OH_WALKING));
				animation.setDelay(100);	//40
				width = 14;//30
			}
		}
		else {
			if(currentAction != OH_IDLE) {
				currentAction = OH_IDLE;
				animation.setFrames(spritesOh.get(OH_IDLE));
				animation.setDelay(800);
				width = 14;//30
			}
		}
		
		animation.update();
		
		// set direction
		if(currentAction != OH_SCRATCHING && currentAction != FIREBALL) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		// draw fireballs
		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).draw(g);
		}
		
		// draw player
		if(flinching) {
			long elapsed =
				(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return;
			}
		}
		
		super.draw(g);
		
	}
	
}

















