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

/**
 * prend le control quand le robot passe sur une ligne noir,
 * v�rifie la couleur de la nouvelle case,
 * change les coordonn�es actuelles du robot
 */
public class VerifLocalisation implements Behavior{
	
	private ArrayList<Integer> map = new ArrayList(35);
	private int[] state;
	CalibrateColor color;
	private float[] sample = new float[1];
	private MovePilot pilot;
	List<Integer> path;
	private int direction;

	
	public VerifLocalisation(ArrayList<Integer> map, int[]state, List<Integer> path, CalibrateColor color, MovePilot pilot) {
	
		this.map = map;
		this.state = state;
		this.color = color;
		this.path = path;
		this.pilot=pilot;
	}
	/*
	 * On rentre dans le comportement si la couleur retourner par le capteur de couleur est le noir (le robot rencontre une ligne noir),
	 * que la coordonn�e actuelle du robot soit diff�rente de la coordonn�e de destination et que le programme est pass� par 
	 * un autre behavior (pour remettre le state[5] � 1), ceci pour �viter que le robot reste bloqu� sur la ligne noir et rentre 
	 * en boucle dans le behavior VerifLocalisation 
	 * @see lejos.robotics.subsumption.Behavior#takeControl()
	 */
	public boolean takeControl() {
		return (color.getColor().equalsIgnoreCase("Black") && (state[0] != state[2])&& (state[5] == 1));
	}

	/*
	 * permet de faire avancer le robot de la ligne noir jusqu'au centre de la case
	 * met � jour la localisation du robot (tableau state[])
	 * �dite la liste path (premier �l�ment = prochaines coordonn�es)
	 * @see lejos.robotics.subsumption.Behavior#action()
	 */
	public void action() {
		
		state[5]=0; // fait en sorte que le robot doive avancer (passer dans le comportement driveForward)
		Delay.msDelay(50);
		state[0] = path.remove(0);//met le premier �l�ment du chemin
		System.out.println(state[0]);
		state[8] += 1;
		if(state[0]!=state[2]){
			verifposition();
			turn();
		}
		else{
			state[5]=1;
		}
		//state[5]=0;
		//System.out.println("State[5](0)="+ state[5]);
	}

	/**
	 * m�thode permettant au robot de tourner
	 * la rotation effectu� est calcul� en soustrayant la direction que doit prendre le robot (direction) � sa direction actuelle (state[1])
	 * ex: si le robot � une direction de 90� (vers la droite) et qu'il doit aller dans une direction de 180 (vers le bas) la rotation vaut 90-180=-90�
	 * la direction devient alors la direction actuelle du robot
	 */
	private void turn() {
		int rotation = state[1]-direction;	
		
		if (rotation==90){
			pilot.backward();
			Delay.msDelay(150);
			pilot.stop();
			pilot.rotate(rotation, false);
			while(pilot.isMoving());
		}
		if ( rotation==-90){
			pilot.forward();
			Delay.msDelay(500);
			pilot.rotate(rotation, false);
			while(pilot.isMoving());
		}
		else if(rotation==180 || rotation==-180){
			pilot.rotate(90, false);
			while(pilot.isMoving());
			pilot.backward();
			while(!(color.getColor().equalsIgnoreCase("Black")));
			pilot.stop();
			
			pilot.rotate(90, false);
			while(pilot.isMoving());
		}
		state[1]=direction;
	}

	/**
	 * calcul la direction vers laquelle doit aller le robot pour aller sur la prochaine case en fonction de la case ou se trouve le robot et
	 * de la prochaine case dans le chemin
	 */
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
