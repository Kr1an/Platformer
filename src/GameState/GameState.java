package GameState;

import Entity.Enemy;

import java.awt.event.KeyEvent;

public abstract class GameState {
	
	protected GameStateManager gsm;
	
	public abstract void init();
	public abstract void update();
	public abstract void draw(java.awt.Graphics2D g);
	public abstract void keyPressed(KeyEvent ke);
	public abstract void keyReleased(KeyEvent ke);
}
