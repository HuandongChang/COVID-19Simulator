package virusSpreadSimulatorFinal;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Person {

	/*
	 * Max and min sizes (diameter) for the circle representing a person
	 */

	 private int age;
	/*
	 * Maximum speed and Minimum Speed
	 */
	private static final float MIN_SPEED = 0.5f;
	private static final float MAX_SPEED = 1.5f;
	//private float probHospitalize = 0.01f; // probability of getting HOSPITALIZE after getting sick
	//Death Rate for different age range
	private static final float[] DeathRate=new float[] {0.09000161f,0.09000695f,0.0900309f,0.0844f,0.090161f,0.00595f,0.0193f,0.0428f,0.078f};
	private int size; // diamater of the circle representing the person
	private int x, y; // position
	private float xVel = 0, yVel = 0; // velocity components


//0.4s represents one day
//Recovery takes 14 days to 42 days
//Death rate varies from 0·00161% to 7.8%

	private float probSick; // probability of getting sick after a collision with a sick person
	private float recoveryTime; // time in milliseconds to recover from first sick
	private float deathProb;
	private float SympotomTime = 5600.0f; // time in milliseconds to recover from first sick
	private long sickTime = -1l; // store the time the person has been sick
	private long HospitalTime = -1l; // store the time the person has been sent to Hospital

	/*
	 * possible states of a person
	 */
	 public enum State {
 		HEALTHY, // green
 		RECOVERED, // blue
 		SICK,//No Sympotom(Not HOSPITALIZE) pink
 		DEATH, // white
 		HOSPITALIZE //red
 	}

	private State state = State.HEALTHY; // state of the person, default healthy

	/**
	 * constructor
	 *
	 * @param w -> width of the city
	 * @param h -> height of the city
	 */
	public Person(int w, int h) {
		Random rand = new Random();


		size = 15;

		x = rand.nextInt(w/4 * 3 - size) + (w / 4);
		y = rand.nextInt(h - size);

		int[] arr = {5, 15, 25, 35, 45, 55, 65, 75, 80};
		int randomNumIndex = rand.nextInt(arr.length);
		//int randomIndex = Random.nextInt(arr.length);
		//int randomVal = arr[randomIndex];

		age = arr[randomNumIndex];
		//age=rand.nextInt(80);

		//probSick ranges from 50% to 90%, depending on age
		probSick=0.5f+(float)age*0.4f/(float)80;

		//Recovery takes 14 days to 42 days, depending on your age
		recoveryTime=(14+42*age/(float)(14*80))*400;

		//Daily Death Prob: Death Rate divided by recover time
		deathProb=DeathRate[age/10]/((14+42*age/(float)(14*80))*400);


		while ((int) xVel == 0)
			xVel = MIN_SPEED+2.5f*rand.nextFloat()*(float)Math.sqrt(100-age)/(float)Math.sqrt(80);

		while ((int) yVel == 0)
			yVel = MIN_SPEED+2.5f*rand.nextFloat()*(float)Math.sqrt(100-age)/(float)Math.sqrt(80);
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @return the deathProb
	 */
	public float getdeathProb() {
		return deathProb;
	}


	/**
	 * @return the HospitalTime
	 */
	public long getHospitalTime() {
		return HospitalTime;
	}


		/**
		 * Set the time a patient get into hospital
		 */
	public void setHospitalTime(long ti) {
		HospitalTime = ti;
	}

	/**
	 * @return the recoveryTime
	 */
	public float getrecoveryTime() {
		return recoveryTime;
	}

	/**
	 * set the person to HOSPITALIZE state
	 */
	public void setHospitalize() {
		state = State.HOSPITALIZE;
		HospitalTime = System.currentTimeMillis();
	}

	/**
	 * set the person to Death state
	 */
	public void setDeath() {
		state = State.DEATH;
	}

	/**
	 * @return the state
	 */
	public State getState() {
		return state;
	}

	/**
	 * set the person to sick state, and start timer for recovery
	 */
	public void setSick() {
		state = State.SICK;
		sickTime = System.currentTimeMillis();
	}

	/**
	 * @return the xVel
	 */
	public float getxVel() {
		return xVel;
	}

	/**
	 * @param xVel the xVel to set
	 */
	public void setxVel(float xVel) {
		this.xVel = xVel;
	}

	/**
	 * @return the yVel
	 */
	public float getyVel() {
		return yVel;
	}

	/**
	 * @param yVel the yVel to set
	 */
	public void setyVel(float yVel) {
		this.yVel = yVel;
	}

	/**
	 * @return the x
	 */
	public float getNextX() {
		return x + xVel;
	}

	/**
	 * @return the y
	 */
	public float getNextY() {
		return y + yVel;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @return position(x)
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return position(y)
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return set position(x)
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return set position(y)
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Draw the person
	 *
	 * @param g -> tool to draw with
	 */
	public void draw(Graphics g) {
		Color color;

		switch (state) {

		case HEALTHY:
			color = Color.GREEN;
			break;
		case RECOVERED:
			color = Color.BLUE;
			break;
		case SICK:
			color = Color.PINK;
			break;
		case DEATH:
			color = Color.WHITE;
			break;
		case HOSPITALIZE:
			color = Color.RED;
			break;
		default:
			color = Color.BLACK;
		}

		g.setColor(color);
		g.fillOval(x, y, size, size);
	}

	/**
	 * Update the person position and state
	 *
	 * @param xWalls -> walls in the x-axis
	 * @param xWalls -> walls in the y-axis
	 * @param w -> width of the city
	 * @param h -> height of the city
	 */
	public void update(int[] xWalls, int[] yWalls, int w, int h) {

		x += xVel;
		y += yVel;

		Rectangle nextMe = new Rectangle((int) Math.ceil(getNextX()), (int) Math.ceil(getNextY()), size, size);

		for (int i = 0; i < xWalls.length; i++)
			if (nextMe.intersectsLine(xWalls[i], 0, xWalls[i], h))
				xVel = -xVel;

		for (int i = 0; i < yWalls.length; i++)
			if (nextMe.intersectsLine(0, yWalls[i], w, yWalls[i]))
				yVel = -yVel;

		// Sympotom testing
		if ((System.currentTimeMillis() - sickTime) >= SympotomTime && state == State.SICK){
			state = State.HOSPITALIZE;//Obervable Sympotom
			HospitalTime=System.currentTimeMillis();
		}

		else if ((System.currentTimeMillis() - HospitalTime) >= recoveryTime && state == State.HOSPITALIZE)
			state = State.RECOVERED;//Obervable Sympotom
	}

	/**
	 * test if collided
	 *
	 * @param p the person to test the collision with
	 * @return true if collided with the other person, false if otherwise
	 */
	public boolean collided(Person p) {
		double dist = Math.sqrt(Math.pow(getNextX() - p.getNextX(), 2) + Math.pow(getNextY() - p.getNextY(), 2));

		boolean collided = (dist < size / 2.0 + p.getSize() / 2.0);

		if (collided && state == State.HEALTHY && (p.getState() == State.SICK || p.getState() == State.HOSPITALIZE))
			if (Math.random() < probSick)
				setSick(); // make sick

		return collided;
	}

	/**
	 * check whether a patient is hospitalized
	 *
	 * @return true if a patient is hospitalized, false if otherwise
	 */
	public boolean checkHospitalize() {
		if(state == State.HOSPITALIZE)
			return true;
		else
			return false;
	}

	/**
	 * set a patient to recovery state
	 */
	public void setRECOVERY() {
		state = State.RECOVERED;
	}


}
