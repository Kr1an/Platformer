package Main;

import GameSaveManager.GameSaveManager;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.io.*;

public class Game {
	public static void main(String[] args) {
		JFrame window = new JFrame("Poor Tramp");
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
		
	}
}
