package GameState;

import GameSaveManager.GameSaveManager;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.Serializable;

import static java.awt.event.KeyEvent.VK_ESCAPE;

public class StartGameOptionsState extends GameState implements Serializable {

    private Background bg;

    private int currentChoice = 0;
    private String[] options = {
            "New",
            "Load",
    };

    private Color titleColor;
    private Font titleFont;

    private Font font;

    public StartGameOptionsState(GameStateManager gsm) {

        this.gsm = gsm;

        try {

            bg = new Background("/Backgrounds/menubg.gif", 1);
//			bg.setVector(-0.1, 0);

            titleColor = new Color(128, 0, 0);
            titleFont = new Font(
                    "Game Options",
                    Font.PLAIN,
                    28);

            font = new Font("Arial", Font.PLAIN, 20);

        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void init() {}

    public void update() {

    }

    public void draw(Graphics2D g) {

        // draw bg
        bg.draw(g);

        // draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("Game:", 0, 25);

        // draw menu options
        g.setFont(font);
        for(int i = 0; i < options.length; i++) {
            if(i == currentChoice) {
                g.setColor(Color.BLACK);
                g.drawString("-" + options[i], 130-7, 120 + i * 22);
            }
            else {
                g.setColor(Color.RED);
                g.drawString(options[i], 130, 120 + i * 22);
            }
        }
    }

    private void deleteSaveState(){
        GameSaveManager gsm = new GameSaveManager();
        gsm.Save("lvl1gamestate", null);
    }
    private void select() {
        if(currentChoice == 0) {
            deleteSaveState();
            gsm.setState(GameStateManager.LEVEL1STATE);
        }
        if(currentChoice == 1) {
            gsm.setState(GameStateManager.LEVEL1STATE);
        }
    }

    public void keyPressed(KeyEvent ke) {
        int k = ke.getKeyCode();
        if(k == KeyEvent.VK_ENTER){
            select();
        }
        if(k == KeyEvent.VK_UP) {
            currentChoice--;
            if(currentChoice == -1) {
                currentChoice = options.length - 1;
            }
        }
        if(k == KeyEvent.VK_DOWN) {
            currentChoice++;
            if(currentChoice == options.length) {
                currentChoice = 0;
            }
        }
    }
    public void keyReleased(KeyEvent ke) {
        int k = ke.getKeyCode();
        if(k == VK_ESCAPE)
            gsm.setState(GameStateManager.MENUSTATE);
    }

}
