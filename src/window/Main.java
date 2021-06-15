package window;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

public class Main extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 1920;
	private static final int HEIGHT = 1080;

	private static JFrame frame;
	private Thread thread;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	// scene contains all the objects
	private Scene scene;

	public Main() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		frame = new JFrame("Fullscreen");
		scene = new Scene(WIDTH, HEIGHT);
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
		System.out.println("rendering...");
		long prev = System.currentTimeMillis();
		render();
		long curr = System.currentTimeMillis();
		float delta = (curr - prev) / 1000.0f;
		System.out.println("rendering finished in: " + delta + " seconds");
	}

	private void render() {
		createBufferStrategy(1);

		BufferStrategy bs = getBufferStrategy();

		// rendering all the objects
		scene.render(pixels);

		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		Main main = new Main();
		frame.add(main);
		frame.setUndecorated(true);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Raytracer");
		frame.setResizable(false);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setCursor(frame.getToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "null"));
		frame.setVisible(true);
		main.start();
	}
}
