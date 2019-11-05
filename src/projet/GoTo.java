package projet;

import java.util.ArrayList;
import java.util.List;

import lejos.hardware.Button;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
//verif 

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
	private List<Integer> retour;
	private MovePilot pilot;
	
	public GoTo(ArrayList<Integer> map2, int[]state, List<Integer> path, MovePilot pilot){
		this.map = map2;
		this.state = state;
		this.path = path;
		this.retour = new ArrayList<Integer>();
		this.pilot = pilot;
	}
	@Override
	public boolean takeControl() {
		return state[0]==state[2];
	}

	@Override
	public void action() {
		state[5]=1;
		System.out.println("GoTO");
		Button.DOWN.waitForPress();
		//pilot.backward();
		//Delay.msDelay(350);
		if(state[3]==0){
			System.out.println("je suis un sauvageons");
			if(state[4]==0){
				System.out.println("Mission 1");
				path.add(4);path.add(3);path.add(2);path.add(1);path.add(0);
				state[2]=0;
				state[4]=1;
				retour.add(0);retour.add(1);retour.add(2);retour.add(3);retour.add(4);
			}
			else if(state[4]==1){
				System.out.println("Mission retour");
				path.clear();
				path.addAll(retour);
				state[2]=4;
			}
			else if(state[4]==2){
				System.out.println("Mission 2");
				path.add(4);path.add(3);path.add(2);path.add(1);path.add(0);
				state[4]=1;
				state[2]=0;
			}
			
		}
		else{
			System.out.println("je suis GDLN");
			if(state[4]==0){
				System.out.println("Mission 1");
				path.add(30);path.add(31);path.add(32);path.add(33);path.add(28);
				state[2]=28;
				state[4]=1;
				retour.add(28);retour.add(33);retour.add(32);retour.add(31);retour.add(30);
			}
			else if(state[4]==1){
				System.out.println("Mission retour");
				path.clear();
				path.addAll(retour);
				state[2]=30;
			}
		}
		
	}

	@Override
	public void suppress() {
		
	}

}
