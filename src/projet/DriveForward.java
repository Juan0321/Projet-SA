package projet;

import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class DriveForward implements Behavior{
	//verif 

	private MovePilot pilot;
	private int[] state;
	
	public DriveForward(MovePilot pilot, int[] state) {
		this.pilot = pilot;
		this.state = state;
	}
	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		pilot.forward();
		Delay.msDelay(500);
		state[5]=1;
		while(pilot.isMoving());
		
	}

	@Override
	public void suppress() {
		pilot.stop();
	}

}
