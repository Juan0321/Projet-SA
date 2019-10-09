package projet;

import java.util.ArrayList;
import java.util.List;

import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

/*
 * prend le control quand le robot passe sur une ligne noir,
 * vérifie la couleur de la nouvelle case,
 * change les coordonnées actuelles du robot
 */
public class VerifLocalisation implements Behavior{
	
	private ArrayList<Integer> map = new ArrayList(35);
	private int[] state = new int[3];
	EV3ColorSensor color;
	private float[] sample;
	private MovePilot pilot;
	List<Integer> path;
	private int direction;

	
	public VerifLocalisation(ArrayList<Integer> map2, int[]state, List<Integer> path, EV3ColorSensor color, MovePilot pilot) {
		this.map = map2;
		this.state = state;
		this.color = color;
		this.path = path;
	}
	@Override
	public boolean takeControl() {
		color.fetchSample(sample, 0);
		return sample[0] == Color.BLACK;
	}

	/*
	 * permet de faire avancer le robot de la ligne noir jusqu'au centre de la case
	 * met à jour la localisation du robot (tableau state)
	 * édite la liste path (premier élément = prochaines coordonnées)
	 * @see lejos.robotics.subsumption.Behavior#action()
	 */
	public void action() {
		System.out.println("ligne noir");
		pilot.stop();
		//pilot.travel(60);
		//while(pilot.isMoving());
		state[0] = path.remove(0);
		verifposition();
		turn();
		
	}

	private void turn() {
		pilot.rotate(state[1]-direction);
		System.out.println("rotate "+ (state[1]-direction));
		while(pilot.isMoving());
		state[1]=direction;
	}
	private void verifposition() {
		if(state[0]-path.get(0)==-1);//right
			direction = 90;
		if(state[0]-path.get(0)==1 );//left
			direction = 270;
		if(state[0]-path.get(0)==5);//up
			direction = 0;
		if(state[0]-path.get(0)==-5);//down
			direction = 180;
		
	}
	@Override
	public void suppress() {
		pilot.stop();
	}

}
