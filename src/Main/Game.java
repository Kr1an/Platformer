package Main;

import GameSaveManager.GameSaveManager;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.io.*;

public class Game implements Serializable {
	public transient int t;
	
	public static void main(String[] args) {

//		Game game = new Game();
//		game.t = 42;
//		GameSaveManager gss = new GameSaveManager();
//		gss.Save("obj1", (Object)game);
//
//		Game g = (Game)gss.Load("obj1");
//		System.out.print(g);




		JFrame window = new JFrame("Poor Tramp");
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
		
	}

	// Serialization staff

	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		stream.writeObject(getByteArrayFromImage());
	}

	private void readObject(ObjectInputStream stream) throws IOException,
			ClassNotFoundException {
		stream.defaultReadObject();
		setImageFromByteArray((int)stream.readObject());
	}

	private int getByteArrayFromImage(){
		return this.t + 3;

	}

	private void setImageFromByteArray(int imageByteData){
		this.t = imageByteData +1;
	}
	
}
