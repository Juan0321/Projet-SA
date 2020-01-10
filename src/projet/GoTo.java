package projet;

import java.util.ArrayList;
import java.util.List;

import lejos.hardware.Button;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
//verif 

/**Ce behavior permet de:
 * - attribuer et changer la coordonnée de destination en fonction de la mission,
 * - calculer le chemin le plus court en fonction de la coordonnée actuelle du robot et de la coordonnée de destination,
 * - stocker le chemin auivre dans la List path
 * 
 */
public class GoTo implements Behavior{
	//Attributs
	private ArrayList<Integer> map;
	private int[] state;
	private List<Integer> path;
	private List<Integer> retour;//list qui stock l'inverse du chemin suivit. il est utiliser pour faire la mission retour.
	private MovePilot pilot;
	private Dijkstra dij;
	public int graph[][]; 
	
	
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
	//On rentre dans ce compostement si la casse actuelle est egal a la casse de destination du robot
	public boolean takeControl() {
		return state[0]==state[2];
	}

	@Override
	public void action() {
		state[5]=1;
		System.out.println("GoTO");
		// on rentre dans une condition en fonction du camps de depart et la mission
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
	
	/**
	 * calcul le chemin pour intercepter le robot adverse en fonction de sa position
	 * pour cela, il calcul le chemin potentiel du robot adverse en suposant que celui ci prendra le plus court chemin et quele robot
	 * adverse considere sa case actuelle comme un obstacle. Puis il calcul le chemin pour aller a la moitie du chemin du robot adverse
	 * cette fonction return none
	 */
	private void intercepter() {
		int positionAdverser= state[6];
		//on calcul le plus court chemin du robot adverse
		state[6]=state[0];// on considere que on est l'obstacle du robot adverse
		addObstacle();
		List<Integer> PCC = dij.dijkstra(graph,positionAdverser,30);//on calcule le plus court chemin du robot adverse
		state[6]=positionAdverser; // on revient a la valeur de depart
		int futursauvage=PCC.size()/2;
		state[2]=PCC.get(futursauvage);//on change notre case de destination a la case central du chemin du robot adverse
		graph = dij.GrapheCreator(map);
		newpath(state[2]);// je calcul le chemin a la nouvelle destination
	}
	
	/**
	 * cette fonction recree le graphique utiliser dans la classe dijkstra pour calculer le chemin le plus court en ajoutent la case 
	 * du robot adverse et les case autour comme des obstacles.
	 * Pour cela il va tout d'abord stocker les couleurs de la case du robot adverse et les cases autours
	 * Ensuite il changer les valeurs associees aux cases par la valeur 5(obstacle)
	 * Ensuite il cree le graph
	 * A la fin a l'aide des valeur stocker il changer les valeurs des cases a ses valeurs originals
	 */
	private void addObstacle() {	
		
		/*0|1|2
		 *3|4|5
		 *6|7|8 
		 *-1 pour rappeller qu'il est en dehors du map
		 *la variable color case va stocker les valeurs originales des cases obstacles
		 */
		int[] colorcase=new int[9];
		colorcase[4]=map.get(state[6]);
		map.set(state[6], 5);
		
		/*0|1|2
		 *3|4|5
		 *6|7|8 
		 *cas ou le robot est situe en bas de la carte
		 */
		if(state[6]+5>34){
			colorcase[6]=-1;//0|1|2
			colorcase[7]=-1;//3|4|5
			colorcase[8]=-1;//6|7|8
			colorcase[1]=map.get(state[6]-5);
			map.set(state[6]-5, 5);
			
			/*0|1|2
			 *3|4|5
			 *6|7|8 
			 *cas ou le robot est situe en bas a gauche de la carte
			 */
			if(state[6]%5==0){
				colorcase[0]=-1;
				colorcase[3]=-1;
				colorcase[2]=map.get(state[6]-5+1);
				colorcase[5]=map.get(state[6]+1);
				map.set(state[6]-5+1, 5);
				map.set(state[6]+1, 5);
				
			/*0|1|2
			 *3|4|5
			 *6|7|8 
			 *cas ou le robot est situe en bas a droite de la carte
			 */
			}else if(state[6]%5==4){
				colorcase[2]=-1;
				colorcase[5]=-1;
				colorcase[0]=map.get(state[6]-5-1);
				colorcase[3]=map.get(state[6]-1);
				map.set(state[6]-5-1, 5);
				map.set(state[6]-1, 5);
				
			/*0|1|2
			 *3|4|5
			 *6|7|8 
			 *cas ou le robot est situe en bas au centre de la carte
			 */
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
			
		/*0|1|2
		 *3|4|5
		 *6|7|8 
		 *cas ou le robot est situe en haut de la carte
		 */	
		}else if(state[6]-5<0){
			colorcase[0]=-1;//0|1|2
			colorcase[1]=-1;//3|4|5
			colorcase[2]=-1;//6|7|8
			colorcase[7]=map.get(state[6]+5);
			map.set(state[6]+5, 5);
			
			/*0|1|2
			 *3|4|5
			 *6|7|8 
			 *cas ou le robot est situe en haut a gauche de la carte
			 */	
			if(state[6]%5==0){
				colorcase[3]=-1;
				colorcase[6]=-1;
				colorcase[5]=map.get(state[6]+1);
				colorcase[8]=map.get(state[6]+5+1);
				map.set(state[6]+1, 5);
				map.set(state[6]+5+1, 5);
				
			/*0|1|2
			 *3|4|5
			 *6|7|8 
			 *cas ou le robot est situe en haut a droite de la carte
			 */	
			}else if(state[6]%5==4){
				colorcase[5]=-1;
				colorcase[8]=-1;
				colorcase[3]=map.get(state[6]-1);
				colorcase[6]=map.get(state[6]+5-1);
				map.set(state[6]-1, 5);
				map.set(state[6]+5-1, 5);
				
			/*0|1|2
			 *3|4|5
			 *6|7|8 
			 *cas ou le robot est situe en haut au centre de la carte
			 */	
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
		/*0|1|2
		 *3|4|5
		 *6|7|8 
		 *cas ou le robot est situe au centre de la carte
		 */	
		else{
			colorcase[1]=map.get(state[6]-5);
			colorcase[7]=map.get(state[6]+5);
			map.set(state[6]-5, 5);
			map.set(state[6]+5, 5);
			
			/*0|1|2
			 *3|4|5
			 *6|7|8 
			 *cas ou le robot est situe au centre a gauche de la carte
			 */	
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
				
			/*0|1|2
			 *3|4|5
			 *6|7|8 
			 *cas ou le robot est situe au centre a droite de la carte
			 */	
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
			
			/*0|1|2
			 *3|4|5
			 *6|7|8 
			 *cas ou le robot est situe au centre au centre de la carte
			 */	
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
		// on recree le graphique avec la carte modifier
		graph = dij.GrapheCreator(map);
		
		//on change les valeurs des cases a ses valeurs originals
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
	
	
	/**
	 * Cette fonction fait appelle a la clase dijkstra pour calculer le plus court chemin et stocker ce chemin dans la variable path
	 *Ensuit il actualiser la variable retour comme l'inverse de la variable path
	 */
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
