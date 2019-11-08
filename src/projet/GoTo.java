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
	private Dijkstra dij;
	public int graph[][] = dij.GrapheCreator(map);
	private int case900=0;
	
	
	public GoTo(ArrayList<Integer> map, int[]state, List<Integer> path, MovePilot pilot){
		this.map = map;
		this.state = state;
		this.path = path;
		this.retour = new ArrayList<Integer>();
		this.pilot = pilot;
		this.dij = new Dijkstra ();
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
				newpath(0);
				state[2]=0;
				state[4]=1;
			}
			else if(state[4]==1){
				System.out.println("Mission retour");
				path.clear();
				path.addAll(retour);
				state[2]=4;
			}
			else if(state[4]==2){
				System.out.println("Mission 2");
				newpath(30);
				state[4]=1;
				state[2]=30;
			}
			else if(state[4]==3){
				System.out.println("Mission attaque");
				addObstacle();
				newpath(30);
				map.set(case900, 5);
				state[2]=30;
			}
			
		}
		else{
			System.out.println("je suis GDLN");
			if(state[4]==0){
				System.out.println("Mission 1");
				newpath(28);
				state[2]=28;
				state[4]=1;
			}
			else if(state[4]==1){
				System.out.println("Mission retour");
				path.clear();
				path.addAll(retour);
				state[2]=30;
			}
			else if(state[4]==2){
				System.out.println("Mission 2");
				newpath(4);
				state[4]=1;
				state[2]=4;
			}
			else if(state[4]==3){
				System.out.println("Mission défense");
				newpath(4);
				state[2]=4;
			}
		}
		
	}
	private void addObstacle() {
			
			if(state[1]==0) {
				state[6]=state[0]-5*2;
				case900 = state[0]-5;
			}
			else if(state[1]==90) {
				state[6]=state[0]-1*2;
				case900 = state[0]-1;
			}
			else if(state[1]==180) {
				state[6]=state[0]+5*2;
				case900 = state[0]+5;
			}
			else if(state[1]==270) {
				state[6]=state[0]+1*2;
				case900 = state[0]+1;
			}
			map.set(state[6], map.get(state[6])+900);
			map.set(case900, map.get(case900)+900);
		}
	private void newpath(int destination){
		List<Integer> PCC = dij.dijkstra(graph,state[2],destination);
		path.clear();
		path.addAll(PCC);
		retour.clear();
		for (int i=PCC.size()-1; i>=0; i--){
			retour.add(PCC.get(i));
		}
	}
	@Override
	public void suppress() {
		
	}

}
