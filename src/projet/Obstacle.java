package projet;

import java.util.ArrayList;

import lejos.hardware.Button;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
	/**
	 * permet de détecter un obstacle via le capteur d'ultrason
	 */
public class Obstacle implements Behavior{
	private EV3UltrasonicSensor dist;
	private ArrayList<Integer> map;
	int[]state;
	private MovePilot pilot;
	private float[] sample= new float[1];
	private CalibrateColor color;
	
	
	public Obstacle(EV3UltrasonicSensor dist,ArrayList<Integer> map, int[]state, MovePilot pilot, CalibrateColor color){
		this.dist=dist;
		this.map = map;
		this.state = state;
		this.pilot = pilot;
		this.color = color;
	}
	@Override
	public boolean takeControl() {
		dist.fetchSample(sample, 0);
		/* rentre dans le comportement si la distance avec un objet est inférieur à 5cm et que le robot n'est pas en train de tourner 
		 * (s'il n'est pas dans le behavior VerifLocalisation) 
		 */
		return sample[0]<0.08 && state[5]==1; 
	}

	@Override
	public void action() {
		//Si le robot n'est pas dans le modele proie-prédateur, il s'arrête lorsqu'il rencontre un obstacle, sans comportement derrière (exercice 4)
		if(state[4]!=3){
			pilot.stop();
			state[2]=state[0];
		}
		else{
			/* Si le robot est dans le modele proie-prédateurLorsque le robot rencontre un obstacle, il s'arrête et recule jusqu'à 
			 * rencontrer une ligne noir
			 */
			pilot.stop();
			pilot.backward();
			Delay.msDelay(150);
			pilot.stop();
			System.out.println("obstacle");
			pilot.backward();
			while(!(color.getColor().equalsIgnoreCase("Black")));
			pilot.stop();
			
			/* remplace la coordonnée de destination state[2] par la coordonnée actuelle du robot state[0] pour qu'il puisse rentrer 
			 * dans le comportement GoTO et calculer un nouveau chemin en fonction de l'obstacle. */
			state[2]=state[0]; 
			
			/*ajoute dans le tableau state[6] la case où se trouve l'obstacle (l'autre robot) selon la direction actuelle du robot
			 * (state[1]) quand il le détecte avec son capteur à ultrasons. */
			if(state[1]==0) {
				state[6]= state[0]-5*2;
			}
			else if(state[1]==90) {
				state[6]= state[0]+1*2;
			}
			else if(state[1]==180) {
				state[6]= state[0]+5*2;
			}
			else if(state[1]==270) {
				state[6]= state[0]-1*2;
			}
		}
	}

	
	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}
}
