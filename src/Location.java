import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.LinkedList;
import java.util.Random;

public class Location {

	private static final int pSIZE = 200; // population size (number of people in the city)
	private int width, height; // dimensions of the city

	private LinkedList<Person> population; // data structure containing all the people and their data

	private boolean quarantine = false; // with walls or no?
	private int[] xWalls; // all the x boundaries
	private int[] yWalls; // all the y boundaries


	private Hospital hospital = new Hospital();

	// The max size of a person, same as the static variable in the Person class
	private int maxSize = 20;
	private int bedX = maxSize;
	private int bedY = ((height - maxSize) - 50) / ((width/4 - maxSize) / maxSize);
	private int death=0;



	/**
	* Constructor
	*
	* @param w -> width of the city
	* @param h -> height of the city
	* @param hospital -> Hospital of the city
	*/
	public Location(int w, int h, Hospital hospital) {
		setSize(w, h);
		setWalls(w, h);
		this.hospital = hospital;

		population = new LinkedList<Person>();

		for (int i = 0; i < pSIZE; i++)
		population.add(new Person(w, h));

		population.get(0).setSick(); // one person needs to start sick
	}



	/**
	* set the size of the city
	*
	* @param w -> width of the city
	* @param h -> height of the city
	*/
	public void setSize(int w, int h) {
		width = w;
		height = h;
	}

	/**
	* setup the walls of the city
	*
	* @param w -> width of the city
	* @param h -> height of the city
	*/
	private void setWalls(int w, int h) {

		if (quarantine) {
			xWalls = new int[6];
			xWalls[0] = 0;
			xWalls[2] = width / 4;
			xWalls[3] = width / 3;
			xWalls[4] = width / 2;
			xWalls[5] = width;

			yWalls = new int[6];
			yWalls[0] = 0;
			yWalls[1] = height / 5;
			yWalls[2] = height / 4;
			yWalls[3] = height / 3;
			yWalls[4] = height / 2;
			yWalls[5] = height;
		} else {
			xWalls = new int[3];
			xWalls[0] = 0;
			xWalls[1] = width / 4;
			xWalls[2] = width;

			yWalls = new int[2];
			yWalls[0] = 0;
			yWalls[1] = height;
		}

	}

	/**
	* Draw the city
	*
	* @param g -> tool to draw with
	*/
	public void draw(Graphics g) {
		drawWalls(g);

		// draw all the people
		for (int i = 0; i < population.size(); i++)
		population.get(i).draw(g);
		hospital.render(g);
	}

	/**
	* Draw all the walls
	*
	* @param g -> tool used to draw with
	*/
	private void drawWalls(Graphics g) {
		// Dotted lines
		g.setColor(Color.white);
		Graphics2D g2d = (Graphics2D) g; // a more complex Graphics class used to draw Objects

		// How to make a dotted line:
		Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 10 }, 0);
		g2d.setStroke(dashed);

		// draw lines
		for (int i = 0; i < xWalls.length; i++)
		g.drawLine(xWalls[i], 0, xWalls[i], height);

		for (int i = 0; i < yWalls.length; i++)
		g.drawLine(width / 4, yWalls[i], width, yWalls[i]);
	}



	private void setBedLocation() {
		if(bedX > width / 4 - maxSize) {
			bedX = maxSize;
			bedY += (height - maxSize - 50) / ((float)(width / 4 - 2 * maxSize) / maxSize);
		}
		if(bedY>height-maxSize)
		bedY=((height - maxSize) - 50) / ((width/4 - maxSize) / maxSize);

	}

	/**
	* update every single person (movement and collisions)
	*/
	public void update() {
		Random rand = new Random();

		//Update Hostpital Conditions
		hospital.update();

		//Check every person whether they need to get into hospital
		for (int i = 0; i < population.size(); i++) {
			Person tempPerson = population.get(i);
			boolean hospitalize = tempPerson.checkHospitalize();



			if(hospitalize) {
				//Whether they will die today
				if(rand.nextFloat()<=tempPerson.getdeathProb()){
					population.get(i).setDeath();
					population.remove(tempPerson);
					death+=1;
					System.out.println("Current Death:" +death);
				}


				 if(hospital.checkNotFull()) {
					setBedLocation();

					population.remove(tempPerson);
					tempPerson.setX(bedX);
					tempPerson.setY(bedY);
					bedX += maxSize;
					tempPerson.setHospitalTime(System.currentTimeMillis());
					hospital.addPatient(tempPerson);

				}
			}
		}

		//Update every person in the hospital
		for (int i = 0; i < hospital.hospitalMember.size(); i++) {
			Person tempPerson = hospital.hospitalMember.get(i);

			if ((System.currentTimeMillis() - tempPerson.getHospitalTime()) >= (tempPerson.getrecoveryTime()/2.0f))
			{

				population.get(i).setRECOVERY();
				tempPerson.setRECOVERY();
				tempPerson.setX(rand.nextInt(width/4 * 3 - maxSize) + (width / 4));
				tempPerson.setY(rand.nextInt(height - maxSize));
				population.add(tempPerson);
				hospital.removePatient(tempPerson);

			}
		}




		for (int i = 0; i < population.size(); i++) {
			population.get(i).update(xWalls, yWalls, width, height);

			for (int j = 0; j < population.size(); j++)
			if (i != j) {
				Person p1 = population.get(i);
				Person p2 = population.get(j);

				boolean collided = p1.collided(p2);


				if (collided)
				{
					float xv1 = p1.getxVel();
					float yv1 = p1.getyVel();
					float speed1=(float)Math.sqrt(xv1*xv1+yv1*yv1);

					float xv2 = p2.getxVel();
					float yv2 = p2.getyVel();
					float speed2=(float)Math.sqrt(xv2*xv2+yv2*yv2);

					p1.setxVel((float)speed1*xv2/(float)Math.sqrt(xv2*xv2+yv2*yv2));
					p1.setyVel((float)speed1*yv2/(float)Math.sqrt(xv2*xv2+yv2*yv2));

					p2.setxVel((float)speed2*xv1/(float)Math.sqrt(xv1*xv1+yv1*yv1));
					p2.setyVel((float)speed2*yv1/(float)Math.sqrt(xv1*xv1+yv1*yv1));

				}

			}

		}
	}

}
