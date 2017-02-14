package GameState;

import Main.GamePanel;
import TileMap.*;
import Entity.*;
import Entity.Enemies.*;
import Audio.AudioPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Level1State extends GameState {

	//positions
	
	private TileMap tileMap;
	private Background bg;
	
	private Player player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	
	private HUD hud;
	
	private AudioPlayer bgMusic;

	private long releaseSpaceTimer;

	
	public Level1State(GameStateManager gsm) {
		releaseSpaceTimer = 0;
		this.gsm = gsm;
		init();
	}
	
	public void init() {
		tileMap = new TileMap(20);
		tileMap.loadTiles("/Tilesets/grasstileset2_3.png");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Backgrounds/grassbg1.gif", 0.1);
		player = new Player(tileMap);
		player.setPosition(50,400);
		player.setSpawnLocation(50, 400);
		
		populateEnemies();

		
		explosions = new ArrayList<Explosion>();
		
		hud = new HUD(player);
		
		bgMusic = new AudioPlayer("/Music/level1-1.mp3");
//		bgMusic.play();


		
	}

	
	private void populateEnemies() {
		
		enemies = new ArrayList<Enemy>();
		
		Slugger s;
		Point[] points = new Point[] {
			new Point(100, 200),
				new Point(100, 300),
				new Point(200, 300),




		};
		for(int i = 0; i < points.length; i++) {
			s = new Slugger(tileMap);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);
		}
		
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
	
}












