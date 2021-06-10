package window;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

public class Main extends Canvas implements Runnable{
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;
	
	private static JFrame frame;
	private Thread thread;
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	
	public Main() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		frame = new JFrame();
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
		render();
	}
	
	private void render() {
		createBufferStrategy(1);

		BufferStrategy bs = getBufferStrategy();

		for(int i = 0; i < WIDTH * HEIGHT; i++) {
			pixels[i] = 0;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
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
