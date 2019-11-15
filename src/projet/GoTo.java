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
	public int graph[][]; 
	private int case900=0;
	
	
	public GoTo(ArrayList<Integer> map, int[]state, List<Integer> path, MovePilot pilot){
		this.map = map;
		this.state = state;
		this.path = path;
		this.retour = new ArrayList<Integer>();
		this.pilot = pilot;
		this.dij = new Dijkstra ();
		this.graph = dij.GrapheCreator(map);
	}
	@Override
	public boolean takeControl() {
		return state[0]==state[2];
	}

	@Override
	public void action() {
		state[5]=1;
		System.out.println("GoTO");
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
				Button.DOWN.waitForPress();
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
				if(state[0]==30){
					System.out.println("j'ai gagne");
					Button.DOWN.waitForPress();
				}
				addObstacle();
				newpath(30);
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
				Button.DOWN.waitForPress();
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
				intercepter();
				state[4]=4;
			}
			else if(state[4]==4){
				Button.DOWN.waitForPress();
			}
		}
		
	}
	private void intercepter() {
		int colorcase=map.get(state[0]);
		map.set(state[0], 5);
		graph = dij.GrapheCreator(map);
		List<Integer> PCC = dij.dijkstra(graph,state[6],30);
		int futursauvage=PCC.size()/2;
		state[2]=PCC.get(futursauvage);
		System.out.println(futursauvage+", "+state[2]);
		map.set(state[0], colorcase);
		graph = dij.GrapheCreator(map);
		newpath(state[2]);
		
	}
	private void addObstacle() {
			
		
		/*int colorcase900=map.get(case900);
		int colorcaseobstacle=map.get(state[6]);
		map.set(case900, 5);
		map.set(state[6], 5);
		System.out.println(case900 +","+state[6]);
		graph = dij.GrapheCreator(map);
		map.set(case900, colorcase900);
		map.set(state[6], colorcaseobstacle);*/
		
		
		
		/*0|1|2
		 *3|4|5
		 *6|7|8 
		 *-1 pour rappeller qu'il est en dehors du map
		 */
		int[] colorcase=new int[9];
		colorcase[4]=map.get(state[6]);
		map.set(state[6], 5);
		
		if(state[6]+5>34){
			colorcase[6]=-1;//0|1|2
			colorcase[7]=-1;//3|4|5
			colorcase[8]=-1;//6|7|8
			colorcase[1]=map.get(state[6]-5);
			map.set(state[6]-5, 5);
			if(state[6]%5==0){
				colorcase[0]=-1;
				colorcase[3]=-1;
				colorcase[2]=map.get(state[6]-5+1);
				colorcase[5]=map.get(state[6]+1);
				map.set(state[6]-5+1, 5);
				map.set(state[6]+1, 5);
			}else if(state[6]%5==4){
				colorcase[2]=-1;
				colorcase[5]=-1;
				colorcase[0]=map.get(state[6]-5-1);
				colorcase[3]=map.get(state[6]-1);
				map.set(state[6]-5-1, 5);
				map.set(state[6]-1, 5);	
			}else{
				colorcase[2]=map.get(state[6]-5+1);
				colorcase[5]=map.get(state[6]+1);
				colorcase[0]=map.get(state[6]-5-1);
				colorcase[3]=map.get(state[6]-1);
				map.set(state[6]+1, 5);
				map.set(state[6]-5+1, 5);
				map.set(state[6]-1, 5);
				map.set(state[6]-5-1, 5);
			}
			
			
		}else if(state[6]-5<0){
			colorcase[0]=-1;//0|1|2
			colorcase[1]=-1;//3|4|5
			colorcase[2]=-1;//6|7|8
			colorcase[7]=map.get(state[6]+5);
			map.set(state[6]+5, 5);
			if(state[6]%5==0){
				colorcase[3]=-1;
				colorcase[6]=-1;
				colorcase[5]=map.get(state[6]+1);
				colorcase[8]=map.get(state[6]+5+1);
				map.set(state[6]+1, 5);
				map.set(state[6]+5+1, 5);
			}else if(state[6]%5==4){
				colorcase[5]=-1;
				colorcase[8]=-1;
				colorcase[3]=map.get(state[6]-1);
				colorcase[6]=map.get(state[6]+5-1);
				map.set(state[6]-1, 5);
				map.set(state[6]+5-1, 5);	
			}else{
				colorcase[5]=map.get(state[6]+1);
				colorcase[8]=map.get(state[6]+5+1);
				colorcase[3]=map.get(state[6]-1);
				colorcase[6]=map.get(state[6]+5-1);
				map.set(state[6]+1, 5);
				map.set(state[6]+5+1, 5);
				map.set(state[6]-1, 5);
				map.set(state[6]+5-1, 5);
			}
		}
		
		else{
			colorcase[1]=map.get(state[6]-5);
			colorcase[7]=map.get(state[6]+5);
			map.set(state[6]-5, 5);
			map.set(state[6]+5, 5);
			if(state[6]%5==0){
				colorcase[0]=-1;//0|1|2
				colorcase[3]=-1;//3|4|5
				colorcase[6]=-1;//6|7|8
				colorcase[2]=map.get(state[6]-5+1);
				colorcase[5]=map.get(state[6]+1);
				colorcase[8]=map.get(state[6]+5+1);
				map.set(state[6]-5+1, 5);
				map.set(state[6]+1, 5);
				map.set(state[6]+5+1, 5);
			}else if(state[6]%5==4){
				colorcase[2]=-1;
				colorcase[5]=-1;
				colorcase[8]=-1;
				colorcase[0]=map.get(state[6]-5-1);
				colorcase[3]=map.get(state[6]-1);
				colorcase[6]=map.get(state[6]+5-1);
				map.set(state[6]-5-1, 5);
				map.set(state[6]-1, 5);
				map.set(state[6]+5-1, 5);	
			}else{
				colorcase[0]=map.get(state[6]-5-1);
				colorcase[2]=map.get(state[6]-5+1);
				
				colorcase[3]=map.get(state[6]-1);
				colorcase[5]=map.get(state[6]+1);
				
				colorcase[6]=map.get(state[6]+5-1);
				colorcase[8]=map.get(state[6]+5+1);
				
				map.set(state[6]-5-1, 5);
				map.set(state[6]-5+1, 5);
				
				map.set(state[6]-1, 5);
				map.set(state[6]+1, 5);
				
				map.set(state[6]+5-1, 5);
				map.set(state[6]+5+1, 5);
			}
		}
		
		graph = dij.GrapheCreator(map);
		int a=-6;
		for(int i=0; i<9; i++){
			if(i==3 || i==6){
				a+=2;
			}
			if(colorcase[i]!=-1){
				map.set(state[6]+a, colorcase[i]);
			}
			a+=1;
		}
		
	}
	private void newpath(int destination){
		List<Integer> PCC = dij.dijkstra(graph,state[0],destination);
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
