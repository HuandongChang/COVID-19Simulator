
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

/**
 * @author Zayed
 *
 */
public class VirusSpreadSimulator extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public final static int WIDTH = 1000; // width of canvas
	public final static int HEIGHT = WIDTH * 9 / 16; // 16:9 aspect ratio canvas

	public boolean running = false; // true if the game is running
	private Thread gameThread; // thread where the game is updated AND drawn (single thread game)
	private Hospital hospital;

	// game objects

	Location city; // the city in which we monitor the population and spread of the virus

	/**
	 * Constructor
	 */
	public VirusSpreadSimulator() {

		canvasSetup();
		initialize();

		newWindow();

	}

	/**
	 * Setup JFrame where the canvas will be in
	 */
	private void newWindow() {
		JFrame frame = new JFrame("Virus Spread Simulator");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.add(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		start();
	}

	/**
	 * initialize all our game objects
	 */
	private void initialize() {
		// Initialize
		city = new Location(getWidth(), getHeight(), new Hospital());
	}

	/**
	 * just to setup the canvas to our desired settings and sizes, setup events
	 */
	private void canvasSetup() {
		this.setSize(new Dimension(WIDTH, HEIGHT));
//		this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
//		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));

		this.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				int code = e.getKeyCode();

				if (code == KeyEvent.VK_R)
					initialize();

			}

		});

		// refresh size of city when this canvas changes size
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				city.setSize(getWidth(), getHeight());
			}
		});

		this.setFocusable(true);
	}

	/**
	 * Game loop
	 */
	@Override
	public void run() {
		// I have a full video explaining this game loop on my YouTube channel Coding Heaven

		this.requestFocus();
		// game timer

		/*final double MAX_FRAMES_PER_SECOND = 60.0;
		final double MAX_UPDATES_PER_SECOND = 60.0;

		long startTime = System.nanoTime();
		final double uOptimalTime = 1000000000 / MAX_UPDATES_PER_SECOND;
		final double fOptimalTime = 1000000000 / MAX_FRAMES_PER_SECOND;
		double uDeltaTime = 0, fDeltaTime = 0;
		int frames = 0, updates = 0;
		long timer = System.currentTimeMillis();

		while (running) {

			long currentTime = System.nanoTime();
			uDeltaTime += (currentTime - startTime) / uOptimalTime;
			fDeltaTime += (currentTime - startTime) / fOptimalTime;
			startTime = currentTime;

			while (uDeltaTime >= 1) {
				update();
				updates++;
				uDeltaTime--;
			}

			while (fDeltaTime >= 1) {
				render();
				frames++;
				fDeltaTime--;
			}

			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("UPS: " + updates + ", FPS: " + frames);
				frames = 0;
				updates = 0;
				timer += 1000;
			}
		}*/
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				update();
				delta--;
			}
			if(running)
				render();
			frames++;

			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();

		stop(); // stop the thread and the game
	}

	/**
	 * start the thread and the game
	 */
	public synchronized void start() {
		gameThread = new Thread(this);
		/*
		 * since "this" is the "Game" Class you are in right now and it implements the
		 * Runnable Interface we can give it to a thread constructor. That thread will
		 * call the "run" method which this class inherited (it's directly above)
		 */
		gameThread.start(); // start thread
		running = true;
	}

	/**
	 * Stop the thread and the game
	 */
	public void stop() {
		try {
			gameThread.join();
			running = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * draw the background and all the objects
	 */
	public void render() {
		// Initialize drawing tools first before drawing

		BufferStrategy buffer = this.getBufferStrategy(); // extract buffer so we can use them
		// a buffer is basically like a blank canvas we can draw on

		if (buffer == null) { // if it does not exist, we can't draw! So create it please
			this.createBufferStrategy(3); // Creating a Triple Buffer
			/*
			 * triple buffering basically means we have 3 different canvases. this is used to
			 * improve performance but the drawbacks are the more buffers, the more memory
			 * needed so if you get like a memory error or something, put 2 instead of 3.
			 *
			 * BufferStrategy:
			 * https://docs.oracle.com/javase/7/docs/api/java/awt/image/BufferStrategy.html
			 */

			return;
		}

		Graphics g = buffer.getDrawGraphics(); // extract drawing tool from the buffers
		/*
		 * Graphics is class used to draw rectangles, ovals and all sorts of shapes and
		 * pictures so it's a tool used to draw on a buffer
		 *
		 * Graphics: https://docs.oracle.com/javase/7/docs/api/java/awt/Graphics.html
		 */

		// draw background
		drawBackground(g);

		// DRAW OBJECTS //
		city.draw(g);

		// actually draw
		g.dispose(); // Disposes of this graphics context and releases any system resources that it
						// is using
		buffer.show(); // actually shows us the 3 beautiful rectangles we drew...LOL

	}

	private void drawBackground(Graphics g) {
		// black background
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	/**
	 * update settings and move all objects
	 */
	public void update() {

		city.update();

	}

	/**
	 *
	 * Main function, creates the canvas
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		new VirusSpreadSimulator();
	}

}
