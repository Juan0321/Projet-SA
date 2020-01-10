package projet;

import lejos.hardware.Button;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

// cette behaviors permet au robot de ce deplace en ligne droit pour atteinde une nouvelle case
public class DriveForward implements Behavior{ 

	//Attributs
	private MovePilot pilot;
	private int[] state;
	CalibrateColor color;
	
	public DriveForward(MovePilot pilot, int[] state, CalibrateColor color) {
		this.pilot = pilot;
		this.state = state;
		this.color = color;
	}
	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		//fait avancer le robot pendant une cour temps (0.5 seconde) afin qu'il ne reste pas bloqué sur une ligne noir.
		pilot.forward();
		Delay.msDelay(500);
		pilot.stop();
		timeWait();
		state[5]=1; // permet au robot de pouvoir à nouveau rentrer dans le behavior VerifLocalisation
		pilot.forward();
		while(pilot.isMoving());
		
	}

	@Override
	public void suppress() {
		pilot.stop();
	}
	/* fait effectuer un temps d'arrêt plus ou moins long (aucun, 1 seconde, 5 secondes, 10 secondes) selon la couleur de la case 
	 * ou se trouve le robot 
	 * return= none
	 */
	public void timeWait(){
		if(color.getColor().equalsIgnoreCase("Blue")){
			Delay.msDelay(10000);
		}else if(color.getColor().equalsIgnoreCase("Orange")){
			Delay.msDelay(5000);
		}else if(color.getColor().equalsIgnoreCase("White")){
			
		}else{
			Delay.msDelay(1000);
		}
	}

}
