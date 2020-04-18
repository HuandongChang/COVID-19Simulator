import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

public class Hospital {

	public int capasity;
	public int patient;

	public int greenValue = 255;

	public LinkedList<Person> hospitalMember;

	public Hospital() {
		this.capasity = 15;
		this.patient = 0;
		this.hospitalMember = new LinkedList<Person>();
	}


	public void update() {
			greenValue = (int)(255 -  ((float)patient / capasity) * 255);
			greenValue = clamp(greenValue, 0, 255);

		}

	public void render(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(15, 15, capasity * 10, 32);
		g.setColor(new Color(75, greenValue, 0));
		g.fillRect(15, 15, patient * 10, 32);
		g.setColor(Color.white);
		g.drawRect(15, 15, capasity * 10, 32);

		for(int i = 0; i < hospitalMember.size(); i++) {
			hospitalMember.get(i).draw(g);
		}

	}

	public void setPatient(int patient) {
		this.patient = patient;
	}

	public int getPatient() {
		return patient;
	}

	public void setCapasity(int capasity) {
		this.capasity = capasity;
	}

	public int getCapasity() {
		return capasity;
	}

	public void addPatient(Person patient) {
		this.patient+=1;
		this.hospitalMember.add(patient);
	}

	public void removePatient(Person patient) {
			this.hospitalMember.remove(patient);
			this.patient-=1;
	}

	public boolean checkNotFull() {
		if(capasity >= patient)
			return true;
		else
			return false;
	}

	public static int clamp(int var, int min, int max) {
		if(var >= max)
			return var = max;
		else if(var <= min)
			return var = min;
		else
			return var;
	}

}
