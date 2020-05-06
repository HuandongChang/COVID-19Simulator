package virusSpreadSimulatorFinal;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

public class Hospital {

	public int capacity;
	public int patient;

	public int greenValue = 255;

	// Linked list to store all the hospital members
	public LinkedList<Person> hospitalMember;

	/**
	 * Constructor to initialize the hospital
	 * The default with capacity 50
	 * 
	 */
	public Hospital() {
		this.capacity = 50;
		this.patient = 0;
		this.hospitalMember = new LinkedList<Person>();
	}


	/**
	 * Update the green value for the capacity bar while the "game" running
	 * The color would change according to the number of patients relative to the capisity.
	 */
	public void update() {
			greenValue = (int)(255 -  ((float)patient / capacity) * 255);
			greenValue = clamp(greenValue, 0, 255);

		}

	/**
	 * Keep drawing the hospital including the capacity bar and the patients
	 * 
	 * @param g, a graphics
	 */
	public void render(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(15, 15, capacity * 10, 32);
		g.setColor(new Color(75, greenValue, 0));
		g.fillRect(15, 15, patient * 10, 32);
		g.setColor(Color.white);
		g.drawRect(15, 15, capacity * 10, 32);
		

		for(int i = 0; i < hospitalMember.size(); i++) {
			hospitalMember.get(i).draw(g);
		}
		
		

	}

	/**
	 * Set the value for patient
	 * 
	 * @param patient, an integer
	 */
	public void setPatient(int patient) {
		this.patient = patient;
	}

	/**
	 * Return the value of the current patient
	 * 
	 * @return patient, an integer
	 */
	public int getPatient() {
		return patient;
	}

	/**
	 * Set the capasity of the hospital
	 * 
	 * @param capacity, an integer
	 */
	public void setcapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * Return the current capacity of the hospital
	 * 
	 * @return capacity, an integer
	 */
	public int getcapacity() {
		return capacity;
	}
	
	/**
	 * Return the number of all the patients in the hospital right now
	 * 
	 * @return hospitalMember.size(), an integer.
	 */
	public int getPatientNum() {
		return this.hospitalMember.size();
	}

	/**
	 * Add the patient to the hospital
	 * 
	 * @param patient, a Person object
	 */
	public void addPatient(Person patient) {
		this.patient+=1;
		this.hospitalMember.add(patient);
	}

	/**
	 * Remove the patient from the hospital
	 * 
	 * @param patient, a Person object
	 */
	public void removePatient(Person patient) {
			this.hospitalMember.remove(patient);
			this.patient-=1;
	}

	/**
	 * Check if the hospital is full
	 * 
	 * @return a boolean, true for NOT full and false for full
	 */
	public boolean checkNotFull() {
		if(capacity > patient)
			return true;
		else
			return false;
	}

	/**
	 * Procedure to make sure that the input var does not go outside of the boundary between min and max
	 * 
	 * @param var, an integer
	 * @param min, an integer
	 * @param max, an integer
	 * @return
	 */
	public static int clamp(int var, int min, int max) {
		if(var >= max)
			return var = max;
		else if(var <= min)
			return var = min;
		else
			return var;
	}

}



