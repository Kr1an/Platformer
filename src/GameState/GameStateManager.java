package GameState;

import GameSaveManager.GameSaveManager;

import java.awt.event.KeyEvent;
import java.io.Serializable;

public class GameStateManager implements Serializable {
	
	private GameState[] gameStates;
	private int currentState;
	public transient int test;


	public static final int NUMGAMESTATES = 3;

	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	public static final int STARTGAMEOPTIONS = 2;

	public GameStateManager() {


		test = 1;
		gameStates = new GameState[NUMGAMESTATES];
		
		currentState = MENUSTATE;
		loadState(currentState);
		
	}
	
	private void loadState(int state) {
		if(state == MENUSTATE){
			gameStates[state] = new MenuState(this);
		}
		else if(state == LEVEL1STATE){
			GameSaveManager gameSaveManager = new GameSaveManager();
			Level1State loadedState = (Level1State)gameSaveManager.Load("lvl1gamestate");
			if(loadedState != null){
				loadedState.afterLoad();
				loadedState.setGsm(this);
				test = 2;
				gameStates[state] = loadedState;
			}

			else
				gameStates[state] = new Level1State(this);
		}
		else if(state == STARTGAMEOPTIONS){
			gameStates[state] = new StartGameOptionsState(this);
		}

	}
	
	private void unloadState(int state) {
		gameStates[state] = null;
	}
	
	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
		//gameStates[currentState].init();
	}
	
	public void update() {
		try {
			gameStates[currentState].update();
		} catch(Exception e) {}
	}
	
	public void draw(java.awt.Graphics2D g) {
		try {
			gameStates[currentState].draw(g);
		} catch(Exception e) {}
	}

	public void exitGameModeWithSave(){
		GameSaveManager gameSaveManager = new GameSaveManager();
		gameSaveManager.Save("lvl1gamestate", gameStates[currentState]);
		setState(MENUSTATE);
	}
	
	public void keyPressed(KeyEvent ke) {
		gameStates[currentState].keyPressed(ke);
	}
	
	public void keyReleased(KeyEvent ke) {
		gameStates[currentState].keyReleased(ke);
	}


	
}









