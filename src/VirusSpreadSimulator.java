package virusSpreadSimulatorFinal;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.colors.*;

import virusSpreadSimulatorFinal.VirusSpreadSimulator.MySwingWorker;



public class VirusSpreadSimulator extends Canvas implements Runnable{

	private static final long serialVersionUID = -4584388369897487885L;

	public static final int WIDTH = 1000, HEIGHT = WIDTH / 16 * 9;

	public boolean running = false; // true if the game is running
	private Thread gameThread; // thread where the game is updated AND drawn (single thread game)
	private Hospital hospital;

	Location city;
	private Menu menu;

	/*
	 * The possible states of a person.
	 */
	public enum STATE{
		Menu,
		Help,
		Run,
		Set,
		End
	}

	public static STATE runState = STATE.Menu;


	/*
	 * Constructor to run the simulation. 
	 */
	public VirusSpreadSimulator() {
		canvasSetup();
		initialize();
		newWindow();
	}

	/* 
	 * The pop window for viewing the simulation.
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

	/*
	 * Initialize all our game objects
	 */
	private void initialize() {
		// Initialize
		this.removeMouseListener(menu);
		city = new Location(getWidth(), getHeight(), new Hospital());
		menu = new Menu(this, city);
		this.addMouseListener(menu);
	}



	/**
	 * It is used to setup the canvas to our desired settings and sizes, setup events
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
					city = new Location(getWidth(), getHeight(), new Hospital(), false, city.getPsize());

				if (code == KeyEvent.VK_M)
					runState = STATE.Set;

				if(code == KeyEvent.VK_Q) {
					if(city.getState()) 
						city.setState(false);
					else city.setState(true);;

				}

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


	/*
	 * Sets the technical elements of the simulation.
	 */
	public void run() {
		this.requestFocus();
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
	}



	/*
	 * Updates the information in the program when the user changes the setting.
	 */
	private void update() {

		if(runState == STATE.Run) {
			city.update();
		}
		else if(runState == STATE.Menu || runState == STATE.End || runState == STATE.Help || runState == STATE.Set) {
			menu.tick();
		}

	}


	public synchronized void start() {
		gameThread = new Thread(this);
		/*
		 * Since "this" is the "Game" Class you are in right now and it implements the
		 * Runnable Interface we can give it to a thread constructor. That thread will
		 * call the "run" method which this class inherited (it's directly above)
		 */
		gameThread.start(); // start thread
		running = true;
	}

	/*
	 * Stops the thread and the game.
	 */
	public void stop() {
		try {
			gameThread.join();
			running = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * While update() changes the information in the program, render() makes the changes on the objects in the window.
	 */
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}


		Graphics g = bs.getDrawGraphics();

		if(runState == STATE.Run) {
			drawBackground(g);
			city.setWalls(getWidth(), getHeight());
			city.draw(g);
		}
		else if(runState == STATE.Menu || runState == STATE.End || runState == STATE.Help || runState == STATE.Set) {
			drawBackground(g);
			menu.render(g);
		}


		g.dispose();
		bs.show();
	}

	/*
	 * Sets the color of the background.
	 */
	private void drawBackground(Graphics g) {
		// black background
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
	}












	// Code for drawing the live charts.
	MySwingWorker mySwingWorker;
	SwingWrapper<XYChart> sw;
	SwingWrapper<XYChart> sw1;
	SwingWrapper<XYChart> sw2;
	XYChart chart;
	XYChart chart1;
	XYChart chart2;


	double [] days = new double[100];
	double [] infections = new double[100];
	double [] deaths = new double[100];
	double [] patients = new double[100];
	
	/*
	 * Displays charts in real time.
	 */
	public void go() {
		// Create Chart for number of active cases.
		chart = QuickChart.getChart("Live Chart of Infections", "Day", "Number of Infected People", "Active Cases", days, infections);
		chart.getStyler().setLegendPosition(LegendPosition.InsideNE);
		chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Area);
		Color [] colors = new Color[1];
		colors[0] = Color.RED;
		chart.getStyler().setSeriesColors(colors);
		
		
		// Create Chart for number of patients.
		chart1 = QuickChart.getChart("Live Chart of Patients", "Day", "Number of Patients", "Patients", days, patients);
		chart1.getStyler().setLegendPosition(LegendPosition.InsideNE);
		chart1.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Area);
		Color [] colors1 = new Color[1];
		colors1[0] = Color.PINK;
		chart1.getStyler().setSeriesColors(colors1);

		
		// Create Chart for number of deaths.
		chart2 = QuickChart.getChart("Live Chart of Deaths", "Day", "Number of Deaths", "Deaths", days, deaths);
		chart2.getStyler().setLegendPosition(LegendPosition.InsideNE);
		chart2.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Area);
		Color [] colors2 = new Color[1];
		colors2[0] = Color.BLACK;
		chart2.getStyler().setSeriesColors(colors2);
		
		
		// Display the graph.
		sw = new SwingWrapper<XYChart>(chart);
		sw.displayChart();
		sw1 = new SwingWrapper<XYChart>(chart1);
		sw1.displayChart();
		sw2 = new SwingWrapper<XYChart>(chart2);
		sw2.displayChart();

		mySwingWorker = new MySwingWorker();
		mySwingWorker.execute();
	}
	
	// Variable to keep track of day.
	public String newday = String.valueOf(1);

	/*
	 * This class allows the data to be recorded and updated in the graph. 
	 */
	public class MySwingWorker extends SwingWorker<Boolean, double[]> {
		LinkedList<Double> fifo = new LinkedList<Double>();
		LinkedList<Double> fifo1 = new LinkedList<Double>();
		LinkedList<Double> fifo2 = new LinkedList<Double>();
		
		/*
		 * Constructor for the MySwingWorker class.
		 */
		public MySwingWorker() {
			fifo.add(0.0);
			fifo1.add(0.0);
			fifo2.add(0.0);
		}

		/*
		 * Worker thread executes our own version of the doInBackground() method continuously in the background.
		 */
		@Override
		protected Boolean doInBackground() throws Exception {

			// Write data to a csv file.
			FileWriter csvWriter = new FileWriter("Report.csv");
			csvWriter.append("Day");
			csvWriter.append(",");
			csvWriter.append("Population");
			csvWriter.append(",");
			csvWriter.append("Hospital Capacity");
			csvWriter.append(",");
			csvWriter.append("Infections");
			csvWriter.append(",");
			csvWriter.append("Patients");
			csvWriter.append(",");
			csvWriter.append("Deaths");
			csvWriter.append(",");
			csvWriter.append("Under Quarantine");
			csvWriter.append(",");
			csvWriter.append("Quarantine Level");
			csvWriter.append("\n");
			csvWriter.flush();
			csvWriter.close();
		
			while (!isCancelled()) {
				String day = String.valueOf(city.day);
				if (day.equals(newday))
				{
					csvWriter = new FileWriter("Report.csv", true);
					day = String.valueOf(city.day);
					days[city.day] = city.day; // Keep track of day
					infections[city.day] = city.getInfection(); // Keep track of number of infections
					patients[city.day] = city.getHospital().getPatientNum(); // Keep track of number of patients
					deaths[city.day] = city.getDeathNum(); // Keep track of number of deaths
					String infection = String.valueOf(city.getInfection());
					String death = String.valueOf(city.getDeathNum());
					String population = String.valueOf(city.getPsize());
					String patients = String.valueOf(city.getHospital().getPatientNum());
					String hospitalCapacity = String.valueOf(city.getHospital().getcapacity());
					String quarantineOrNot = "";
					String quarantineLevel = "";
					if (city.getState())
						quarantineOrNot = "True";
					else
						quarantineOrNot = "False";
					if (quarantineOrNot.equals("True"))
						quarantineLevel = String.valueOf(city.getQLevel());
					else
						quarantineLevel = "N/A";
						
					csvWriter.append(String.join(",", Arrays.asList(day, population, hospitalCapacity, infection, patients, death, quarantineOrNot, quarantineLevel)));
					csvWriter.append("\n");
					csvWriter.flush();
					csvWriter.close();
					newday = String.valueOf(Integer.parseInt(newday) + 1);
				}


				fifo.add((double)city.getInfection());
				if (fifo.size() > 3000) {
					fifo.removeFirst();
				}

				double[] array = new double[fifo.size()];
				for (int i = 0; i < fifo.size(); i++) {
					array[i] = fifo.get(i);
				}

				
				fifo1.add((double)city.getHospital().getPatientNum());
				if (fifo1.size() > 3000) {
					fifo1.removeFirst();
				}

				double[] array1 = new double[fifo1.size()];
				for (int i = 0; i < fifo1.size(); i++) {
					array1[i] = fifo1.get(i);
				}
				
				fifo2.add((double)city.getDeathNum());
				if (fifo2.size() > 3000) {
					fifo2.removeFirst();
				}

				double[] array2 = new double[fifo2.size()];
				for (int i = 0; i < fifo2.size(); i++) {
					array2[i] = fifo2.get(i);
				}

				publish(array, array1, array2);

				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					// Caught when interrupt is called
					System.out.println("Live Chart shut down.");
				}

			}

			return true;
		}

		
		/*
		 * Event Dispatch Thread: All swing related activities occurs in this thread. 
		 * Swingworker invokes the process() method.
		 * Updates the datapoints in the chart and repaints the chart.
		 */
		@Override
		protected void process(List<double[]> chunks) {
			
			// Update data points in the chart
			chart.updateXYSeries("Active Cases", null, infections, null);
			chart1.updateXYSeries("Patients", null, patients, null);	
			chart2.updateXYSeries("Deaths", null, deaths, null);

			// Repaint the charts
			sw.repaintChart();
			sw1.repaintChart();
			sw2.repaintChart();

			long start = System.currentTimeMillis();
			long duration = System.currentTimeMillis() - start;
			try {
				Thread.sleep(40 - duration); // 40 ms ==> 25fps
				// Thread.sleep(400 - duration); // 40 ms ==> 2.5fps
			} catch (InterruptedException e) {
			}

		}
	}




	public static void main (String args[]) throws Exception {
		VirusSpreadSimulator vss = new VirusSpreadSimulator();	
		vss.go();
	}
}


