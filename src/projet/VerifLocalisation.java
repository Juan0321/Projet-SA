package projet;

import java.util.ArrayList;
import java.util.List;

import lejos.hardware.Button;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
//verif 

/*
 * prend le control quand le robot passe sur une ligne noir,
 * vérifie la couleur de la nouvelle case,
 * change les coordonnées actuelles du robot
 */
public class VerifLocalisation implements Behavior{
	
	private ArrayList<Integer> map = new ArrayList(35);
	private int[] state;
	CalibrateColor color;
	private float[] sample = new float[1];
	private MovePilot pilot;
	List<Integer> path;
	private int direction;

	
	public VerifLocalisation(ArrayList<Integer> map2, int[]state, List<Integer> path, CalibrateColor color, MovePilot pilot) {
	
		this.map = map2;
		this.state = state;
		this.color = color;
		this.path = path;
		this.pilot=pilot;
	}
	@Override
	public boolean takeControl() {
		
		return (color.getColor().equalsIgnoreCase("Black") && (state[0] != state[2])&& (state[5] == 1));
	}

	/*
	 * permet de faire avancer le robot de la ligne noir jusqu'au centre de la case
	 * met à jour la localisation du robot (tableau state)
	 * édite la liste path (premier élément = prochaines coordonnées)
	 * @see lejos.robotics.subsumption.Behavior#action()
	 */
	public void action() {
		System.out.println("ligne noir");
		state[0] = path.remove(0);
		System.out.println(state[0]);

		if(state[0]!=state[2]){
			verifposition();
			state[5]=0;
			turn2();
		}
		else{
			state[5]=1;
		}
		//state[5]=0;
		//System.out.println("State[5](0)="+ state[5]);
	}

	private void turn() {
		int rotation = state[1]-direction;		
		pilot.rotate(rotation);// tourne dans le sens anti-horaire
		while(pilot.isMoving());
		state[1]=direction;
	}
	private void turn2() {
		int rotation = state[1]-direction;	
		
		if (rotation==90){
			pilot.rotate(rotation);	
			while(pilot.isMoving());
		}
		if ( rotation==-90){
			pilot.forward();
			Delay.msDelay(500);
			pilot.rotate(rotation);	
			while(pilot.isMoving());
		}
		else if(rotation==180 || rotation==-180){
			
			pilot.rotate(90);	
			while(pilot.isMoving());
			pilot.backward();
			while(!(color.getColor().equalsIgnoreCase("Black")));
			pilot.stop();
			
			pilot.rotate(90);	
			while(pilot.isMoving());
		}
		
		state[1]=direction;
		
	}
	

	private void verifposition() {
		if(state[0]-path.get(0)==-1) {
			direction = 90;//right
		}	
		else if(state[0]-path.get(0)==1 ) {
			direction = 270;//left
		}
		else if(state[0]-path.get(0)==-5) {
			direction = 180;//down
		}		
		else if(state[0]-path.get(0)==5) {
			direction = 0;//up
		}
			
	}
	@Override
	public void suppress() {
		pilot.stop();
	}

}
