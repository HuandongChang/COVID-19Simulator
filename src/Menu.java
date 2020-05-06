package virusSpreadSimulatorFinal;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

// The state of the current simulator
import virusSpreadSimulatorFinal.VirusSpreadSimulator.STATE;

// User interactive menu panel for basic setup and operations
public class Menu extends MouseAdapter{

	private VirusSpreadSimulator vss;
	private Location location;
	private int pSize = 0;

	/**
	 * Constructor
	 * 
	 * @param vss, a VirusSpreadSimulator object
	 * @param location, a location object
	 */
	public Menu(VirusSpreadSimulator vss, Location location) {
		this.vss = vss;
		this.location = location;
		this.pSize = vss.city.getPsize();
	}

	/**
	 * MousePressed method to determine users' mouse operations
	 */
	public void mousePressed(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();

		// Menu State
		if(vss.runState == STATE.Menu) {
			// Start button
			if(mouseOver(mx, my, 405, 150, 200, 64)) {
				vss.runState = STATE.Run;
				location.setCurrTime(System.currentTimeMillis());
				return;
			}

			// Setup button
			if(mouseOver(mx, my, 405, 250, 200, 64)) {
				vss.runState = STATE.Set;
				return;
			}

			// Quit button
			if(mouseOver(mx, my, 405, 350, 200, 64)) {
				System.exit(1);
			}
		}
		
		// Setup State
		if(vss.runState == STATE.Set) {
			// Quarantine setup
			// True button
			if(mouseOver(mx, my, 240, 100, 70, 70)) {
				vss.city.setState(true);	
			}

			// False button
			if(mouseOver(mx, my, 310, 100, 70, 70)) {
				vss.city.setState(false);
			}
			
			// Select qLevel
			if(vss.city.getState()) {
				if(mouseOver(mx, my, 540, 115, 50, 50))
					vss.city.setQLevel("low");
				if(mouseOver(mx, my, 590, 115, 50, 50))
					vss.city.setQLevel("medium");
				if(mouseOver(mx, my, 640, 115, 50, 50))
					vss.city.setQLevel("high");
			}



			//Population setup
			if(mouseOver(mx, my, 240, 200, 70, 70))
				pSize = 50; //vss.city.setPsize(50);
			if(mouseOver(mx, my, 310, 200, 70, 70))
				pSize = 100; //vss.city.setPsize(100);
			if(mouseOver(mx, my, 380, 200, 70, 70))
				pSize = 150; //vss.city.setPsize(150);
			if(mouseOver(mx, my, 450, 200, 70, 70))
				pSize = 200; //vss.city.setPsize(200);
			if(mouseOver(mx, my, 520, 200, 70, 70))
				pSize = 250; //vss.city.setPsize(250);

			/*
			if(mouseOver(mx, my, 240, 200, 350, 70)) {
				vss.city = new Location(vss.getWidth(), vss.getHeight(), new Hospital(), vss.city.getState(), vss.city.getPsize());
				System.out.println("Size: " + vss.city.getPsize());
			}
			*/


			// Hospital setup
			if(mouseOver(mx, my, 340, 300, 70, 70))
				vss.city.getHospital().setcapacity(20);
			if(mouseOver(mx, my, 410, 300, 70, 70))
				vss.city.getHospital().setcapacity(40);
			if(mouseOver(mx, my, 480, 300, 70, 70))
				vss.city.getHospital().setcapacity(50);
			if(mouseOver(mx, my, 550, 300, 70, 70))
				vss.city.getHospital().setcapacity(60);
			if(mouseOver(mx, my, 620, 300, 70, 70))
				vss.city.getHospital().setcapacity(80);



			// Apply button
			if(mouseOver(mx, my, 435, 465, 140, 50)) {
				if(pSize != vss.city.getPsize()) {
					int capacity = vss.city.getHospital().getcapacity();
					vss.city = new Location(vss.getWidth(), vss.getHeight(), new Hospital(), vss.city.getState(), pSize);
					vss.city.getHospital().setcapacity(capacity);
					location.setCurrTime(System.currentTimeMillis());
				}
				vss.runState = STATE.Run;
				return;
			}
		}
	}

	/**
	 * Do nothing when mouse released
	 * 
	 */
	public void mouseReleased(MouseEvent e) {



	}

	/**
	 * Check if the mouse click is over a specific area
	 * 
	 * @param mx, users' mouse x position
	 * @param my, users' mouse y position
	 * @param x, initial x location of a specified area
	 * @param y, initial y location of a specified area
	 * @param width, the width of the rectangle area
	 * @param height, the height of the rectangle area
	 * @return a boolean
	 */
	private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
		if(mx > x && mx < x + width) {
			if(my > y && my < y + height) {
				return true;
			}else return false;
		}else return false;
	}

	/**
	 * The menu does not have any action updates
	 */
	public void tick() {

	}

	/**
	 * Updating the drawings of the menu
	 * 
	 * @param g, a graphics
	 */
	public void render(Graphics g) {
		// Menu state
		if(vss.runState == STATE.Menu) {

			Font fnt = new Font("arial", 1, 50);
			Font fnt2 = new Font("arial", 1, 30);

			g.setFont(fnt);
			g.setColor(Color.RED);
			g.drawString("Simulator", 400, 70);

			g.setFont(fnt2);
			g.drawRect(405, 150, 200, 64);
			g.drawString("Start", 465, 190);

			g.drawRect(405, 250, 200, 64);
			g.drawString("Setup", 465, 290);

			g.drawRect(405, 350, 200, 64);
			g.drawString("Quit", 475, 390);

		}
		// Setup state
		else if(vss.runState == STATE.Set){
			Font fnt = new Font("arial", 1, 50);
			Font fnt2 = new Font("arial", 1, 30);
			Font fnt3 = new Font("arial", 1, 20);
			Font fnt4 = new Font("arial", 1, 15);

			// Quarantine button
			g.setFont(fnt);
			g.setColor(Color.RED);
			g.drawString("Setup", 425, 70);

			g.setFont(fnt2);
			g.drawString("Quarantine: ", 50, 150);

			g.drawRect(240, 100, 70, 70);
			g.drawRect(310, 100, 70, 70);

			g.setColor(Color.RED);
			if(vss.city.getState()) {
				g.fillRect(240, 100, 70, 70);
				g.setFont(fnt3);
				g.drawString("Set Level: ", 430, 150);
				
				g.setColor(Color.BLACK);
				g.fillRect(540, 115, 50, 50);
				g.fillRect(590, 115, 50, 50);
				g.fillRect(640, 115, 50, 50);

				g.setColor(Color.RED);
				g.drawRect(540, 115, 50, 50);
				g.drawRect(590, 115, 50, 50);
				g.drawRect(640, 115, 50, 50);
				
				String level = vss.city.getQLevel();
				if(level.equals("low"))
					g.fillRect(540, 115, 50, 50);
				if(level.equals("medium"))
					g.fillRect(590, 115, 50, 50);
				if(level.equals("high"))
					g.fillRect(640, 115, 50, 50);
				
				g.setFont(fnt4);
				g.setColor(Color.WHITE);

				g.drawString("Low", 553, 148);
				g.drawString("Mid", 603, 148);
				g.drawString("High", 650, 148);

			}
			else
				g.fillRect(310, 100, 70, 70);

			
			
			g.setFont(fnt3);
			g.setColor(Color.WHITE);
			g.drawString("True", 255, 145);
			g.drawString("False", 320, 145);


			// Population button
			g.setColor(Color.RED);
			g.setFont(fnt2);
			g.drawString("Population: ", 50, 250);

			g.setColor(Color.BLACK);
			g.fillRect(240, 200, 70, 70);
			g.fillRect(310, 200, 70, 70);
			g.fillRect(380, 200, 70, 70);
			g.fillRect(450, 200, 70, 70);
			g.fillRect(520, 200, 70, 70);

			g.setColor(Color.RED);
			g.drawRect(240, 200, 70, 70);
			g.drawRect(310, 200, 70, 70);
			g.drawRect(380, 200, 70, 70);
			g.drawRect(450, 200, 70, 70);
			g.drawRect(520, 200, 70, 70);



			if(pSize == 50)
				g.fillRect(240, 200, 70, 70);
			if(pSize == 100)
				g.fillRect(310, 200, 70, 70);
			if(pSize == 150)
				g.fillRect(380, 200, 70, 70);
			if(pSize == 200)
				g.fillRect(450, 200, 70, 70);
			if(pSize == 250)
				g.fillRect(520, 200, 70, 70);

			g.setFont(fnt3);
			g.setColor(Color.WHITE);

			g.drawString("50", 265, 245);
			g.drawString("100", 330, 245);
			g.drawString("150", 400, 245);
			g.drawString("200", 470, 245);
			g.drawString("250", 540, 245);




			// Hospital button
			g.setColor(Color.RED);
			g.setFont(fnt2);
			g.drawString("Hospital capacity: ", 50, 350);

			g.setColor(Color.BLACK);
			g.fillRect(340, 300, 70, 70);
			g.fillRect(410, 300, 70, 70);
			g.fillRect(480, 300, 70, 70);
			g.fillRect(550, 300, 70, 70);
			g.fillRect(620, 300, 70, 70);

			g.setColor(Color.RED);
			g.drawRect(340, 300, 70, 70);
			g.drawRect(410, 300, 70, 70);
			g.drawRect(480, 300, 70, 70);
			g.drawRect(550, 300, 70, 70);
			g.drawRect(620, 300, 70, 70);


			int key2 = vss.city.getHospital().getcapacity();

			if(key2 == 20)
				g.fillRect(340, 300, 70, 70);
			if(key2 == 40)
				g.fillRect(410, 300, 70, 70);
			if(key2 == 50)
				g.fillRect(480, 300, 70, 70);
			if(key2 == 60)
				g.fillRect(550, 300, 70, 70);
			if(key2 == 80)
				g.fillRect(620, 300, 70, 70);


			g.setFont(fnt3);
			g.setColor(Color.WHITE);

			g.drawString("20", 365, 345);
			g.drawString("40", 435, 345);
			g.drawString("50", 505, 345);
			g.drawString("60", 575, 345);
			g.drawString("80", 645, 345);


			// Apply button
			g.setColor(Color.WHITE);
			g.setFont(fnt2);
			g.drawRect(435, 465, 140, 50);
			g.drawString("Apply", 465, 500);
		}
	}
}

