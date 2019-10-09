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
	EV3ColorSensor color;
	private float[] sample = new float[1];
	private MovePilot pilot;
	List<Integer> path;
	private int direction;

	
	public VerifLocalisation(ArrayList<Integer> map2, int[]state, List<Integer> path, EV3ColorSensor color, MovePilot pilot) {
		this.map = map2;
		this.state = state;
		this.color = color;
		this.path = path;
		this.pilot=pilot;
	}
	@Override
	public boolean takeControl() {
		color.fetchSample(sample, 0);
		return (sample[0] == Color.BLACK && (state[0] != state[2]));
	}

	/*
	 * permet de faire avancer le robot de la ligne noir jusqu'au centre de la case
	 * met à jour la localisation du robot (tableau state)
	 * édite la liste path (premier élément = prochaines coordonnées)
	 * @see lejos.robotics.subsumption.Behavior#action()
	 */
	public void action() {
		System.out.println("ligne noir");
		//Button.ENTER.waitForPressAndRelease();
		//pilot.stop();
		//pilot.travel(60);
		//while(pilot.isMoving());	
		//Button.ENTER.waitForPressAndRelease();
		state[0] = path.remove(0);
		System.out.println(state[0]);

		if(state[0]!=state[2]){
			verifposition();
			turn();
			pilot.forward();
			Delay.msDelay(300);
			
			
		}
		
	}

	private void turn() {
		int rotation = state[1]-direction;		
		pilot.rotate(rotation);// tourne dans le sens anti-horaire
		while(pilot.isMoving());
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
