package projet;

import java.util.ArrayList;
import java.util.List;

import lejos.hardware.Button;
import lejos.robotics.subsumption.Behavior;

/*
 * donne la coordonnée de destination,
 * calcul le chemin le plus court en fonction de la coordonnée actuelle du robot et de la coordonnée de destination,
 * stock le chemin dans la List path
 * 
 */
public class GoTo implements Behavior{
	
	private ArrayList<Integer> map;
	private int[] state;
	private List<Integer> path;
	
	public GoTo(ArrayList<Integer> map2, int[]state, List<Integer> path){
		this.map = map2;
		this.state = state;
		this.path = path;
	}
	@Override
	public boolean takeControl() {
		return Button.DOWN.isDown()&& state[0]==state[2];
	}

	@Override
	public void action() {
		//on commence au sud
		path.add(4);path.add(3);path.add(2);path.add(1);path.add(0);
		
	}

	@Override
	public void suppress() {
		
	}

}
