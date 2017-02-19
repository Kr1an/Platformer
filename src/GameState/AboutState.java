package GameState;

import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.io.Serializable;

public class AboutState extends GameState implements Serializable {

    private Background bg;

    private Color titleColor;
    private Font titleFont;
    private String about;
    private Font font;

    private String getFile(String fileName) {

        StringBuilder result = new StringBuilder("");

        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File file;
        try {
            file = new File(classLoader.getResource(fileName).getFile());
        }catch(Exception e) {
            return null;
        }

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();

    }


    public AboutState(GameStateManager gsm) {

        this.gsm = gsm;

        try {
            bg = new Background("/Backgrounds/menubg.gif", 1);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        titleColor = new Color(128, 0, 0);
        titleFont = new Font("Arial",Font.PLAIN,28);
        font = new Font("Arial", Font.PLAIN, 12);

        about = getFile("about.txt");
        if (about == null)
            about = "Nothing to say about";
    }

    public void init() {}

    public void update() {

    }

    public void draw(Graphics2D g) {
        bg.draw(g);

        g.setColor(Color.RED);
        g.setFont(font);
        if(about != null) {
            String[] aboutLines = about.split("\r\n|\r|\n");
            for (int i = 0; i < aboutLines.length; i++) {
                g.drawString(aboutLines[i], 20, 140 + i * 20);
            }
        }
    }


    public void keyPressed(KeyEvent ke) {
        gsm.setState(gsm.MENUSTATE);
    }
    public void keyReleased(KeyEvent ke) {}
}
