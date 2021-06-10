package window;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Main extends Canvas implements Runnable{
	private static final long serialVersionUID = 1L;
	private int WIDTH = 1280;
	private int HEIGHT = 720;
	
	private static JFrame frame = new JFrame();
	private Thread thread;
	
	
	public Main() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
	}
	
	public synchronized void start() {
		thread = new Thread(this, "Main");
		thread.start();
	}
	
	public synchronized void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		
	}
	
	public static void main(String[] args) {
		Main main = new Main();
		frame.add(main);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Raytracer");
		frame.setResizable(false);
		frame.setVisible(true);
		main.start();
	}
}
