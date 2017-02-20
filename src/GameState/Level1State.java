package GameState;

import Main.GamePanel;
import TileMap.*;
import Entity.*;
import Entity.Enemies.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.lang.reflect.*;

public class Level1State extends GameState implements Serializable {

	//positions
	
	private TileMap tileMap;
	private Background bg;

	private Player player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	private ArrayList<DoubleJumpPoint> doubleJumpPoints;
	
	private HUD hud;


	private long releaseSpaceTimer;

	private String[] enemyTypeList = {
			"Slugger",
			"BlueNiddler",
			"RedNiddler",

	};


	
	public Level1State(GameStateManager gsm) {
		releaseSpaceTimer = 0;
		this.gsm = gsm;
		init();
	}
	
	public void init() {

		externalInit();


		player = new Player(tileMap);
		player.setPosition(50,400);
		player.setSpawnLocation(50, 400);

		enemies = new ArrayList<Enemy>();
		doubleJumpPoints = new ArrayList<DoubleJumpPoint>();

		extraObjectsSpawn();

		
		explosions = new ArrayList<Explosion>();
		
		hud = new HUD(player);
		



		
	}
	private void externalInit(){
		tileMap = new TileMap(20);
		tileMap.loadTiles("/Tilesets/grasstileset2_3.png");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);

		bg = new Background("/Backgrounds/grassbg1.gif", 0.1);

	}

	
	private void createEnemy(Point p, Enemy e) {
		e.setPosition(p.x, p.y);
		enemies.add(e);
	}


	
	public void update() {
		player.update();
		tileMap.setPosition(
			GamePanel.WIDTH / 2 - player.getx(),
			GamePanel.HEIGHT / 2 - player.gety()
		);
		
		// set background
		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		// attack enemies
		player.checkAttack(enemies);

		// update double jump point
		for(int i = 0; i < doubleJumpPoints.size(); i++){
			DoubleJumpPoint p = doubleJumpPoints.get(i);



			if(player.intersects(p)){
				if(p.getUsed() == false){
					player.addExtraJump();
					p.setUsed();
				}

			}
			p.update();

		}

		
		// update all enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead()) {
				enemies.remove(i);
				i--;
				explosions.add(
					new Explosion(e.getx(), e.gety()));
			}
		}
		// update explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}

		//check new spawn if

		// check if dead

		if(player.getGameover()){
			double spawnx = player.getSpawnx(), spawny = player.getSpawny();
			player = new Player(tileMap);
			player.setPosition(spawnx, spawny);
			player.setSpawnLocation(spawnx, spawny);
			player.setHealth(1);




		}
		
	}
	
	public void draw(Graphics2D g) {
		
		// draw bg
		bg.draw(g);
		
		// draw tilemap
		tileMap.draw(g);
		
		// draw player
		player.draw(g);
		
		// draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		
		// draw explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition(
				(int)tileMap.getx(), (int)tileMap.gety());
			explosions.get(i).draw(g);
		}

		//draw double jump points - if interuct,  get extra jump
		for(int i = 0; i < doubleJumpPoints.size(); i++){
			doubleJumpPoints.get(i).draw(g);
		}
		
		// draw hud
//		hud.draw(g);
		
	}
	
	public void keyPressed(KeyEvent ke) {
		int k = ke.getKeyCode();
		if(k == KeyEvent.VK_SPACE) {

			player.setJumping(true);

		}
		if(k == KeyEvent.VK_LEFT) player.setLeft(true);
		if(k == KeyEvent.VK_RIGHT) player.setRight(true);
		if(k == KeyEvent.VK_UP) player.setUp(true);
		if(k == KeyEvent.VK_DOWN) player.setDown(true);
		if(k == KeyEvent.VK_E) player.setGliding(true);
		if(k == KeyEvent.VK_R) player.hit(12);
		if(k == KeyEvent.VK_F) player.setFiring();
		if(k == KeyEvent.VK_ESCAPE) gsm.exitGameModeWithSave();
	}
	
	public void keyReleased(KeyEvent ke) {

		int k = ke.getKeyCode();
		if(releaseSpaceTimer == 0)
		if(k == KeyEvent.VK_SPACE){
			if(player.getJumping()){
				player.setJumpedOnce(true);
			}
			player.setJumping(false);
		}
		if(k == KeyEvent.VK_LEFT) player.setLeft(false);
		if(k == KeyEvent.VK_RIGHT) player.setRight(false);
		if(k == KeyEvent.VK_UP) player.setUp(false);
		if(k == KeyEvent.VK_DOWN) player.setDown(false);
		if(k == KeyEvent.VK_E) player.setGliding(false);
	}

	public void createDoubeJumpPoint(DoubleJumpPoint doubleJumpPoint, Point point){
		doubleJumpPoint.setPosition(point.x, point.y);
		doubleJumpPoints.add(doubleJumpPoint);
	}



	public void extraObjectsSpawn(){
		int [][] map = tileMap.getMap();
		for(int i = 0; i < map.length; i++){
			for(int j = 0; j < map[i].length; j++){
				if( map[i][j] < 0 && map[i][j] > -9){
					try{
						TileMap tileMapParam = tileMap;
						if(-map[i][j] > enemyTypeList.length){
							continue;
						}
						String className = "Entity.Enemies." + enemyTypeList[-map[i][j]-1];
						Class cl = Class.forName(className);
						Constructor con = cl.getConstructor(TileMap.class);
						Enemy e = (Enemy)con.newInstance(tileMapParam);
						int x = i*tileMap.getTileSize();
						int y = j*tileMap.getTileSize();
						Point p = new Point(y,x);
						createEnemy(p, e);

					}catch(Exception e){
						e.printStackTrace();
					}



				}
				else if(map[i][j] == -9){
					DoubleJumpPoint o = new DoubleJumpPoint(tileMap);
					int x = i*tileMap.getTileSize();
					int y = j*tileMap.getTileSize();
					Point p = new Point(y, x);
					createDoubeJumpPoint(o, p);
				}

			}
		}


	}
	// Serializer staff

	private void readObject(ObjectInputStream input)
			throws IOException, ClassNotFoundException {
		// deserialize the non-transient data members first;

		input.defaultReadObject();

	}
	private void writeObject(ObjectOutputStream output)
			throws IOException, ClassNotFoundException {
		// serialize the non-transient data members first;
		output.defaultWriteObject();
	}
	public void setGsm(GameStateManager val){gsm = val;}

	public void afterLoad(){


		tileMap.loadTiles("/Tilesets/grasstileset2_3.png");
		tileMap.loadMap("/Maps/level1-1.map");

		bg = new Background("/Backgrounds/grassbg1.gif", 0.1);

	}
}












