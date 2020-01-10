package projet;

import java.util.ArrayList;

import lejos.hardware.Button;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
	/**
	 * permet de d�tecter un obstacle via le capteur d'ultrason
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
		/* rentre dans le comportement si la distance avec un objet est inf�rieur � 5cm et que le robot n'est pas en train de tourner 
		 * (s'il n'est pas dans le behavior VerifLocalisation) 
		 */
		return sample[0]<0.08 && state[5]==1; 
	}

	@Override
	public void action() {
		//Si le robot n'est pas dans le modele proie-pr�dateur, il s'arr�te lorsqu'il rencontre un obstacle, sans comportement derri�re (exercice 4)
		if(state[4]!=3){
			pilot.stop();
			state[2]=state[0];
		}
		else{
			/* Si le robot est dans le modele proie-pr�dateurLorsque le robot rencontre un obstacle, il s'arr�te et recule jusqu'� 
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
			
			/* remplace la coordonn�e de destination state[2] par la coordonn�e actuelle du robot state[0] pour qu'il puisse rentrer 
			 * dans le comportement GoTO et calculer un nouveau chemin en fonction de l'obstacle. */
			state[2]=state[0]; 
			
			/*ajoute dans le tableau state[6] la case o� se trouve l'obstacle (l'autre robot) selon la direction actuelle du robot
			 * (state[1]) quand il le d�tecte avec son capteur � ultrasons. */
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
