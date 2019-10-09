package projet;

import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class DriveForward implements Behavior{

	private MovePilot pilot;
	
	public DriveForward(MovePilot pilot) {
		this.pilot = pilot;
	}
	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		pilot.setLinearSpeed(50);
		pilot.forward();
		while(pilot.isMoving());
	}

	@Override
	public void suppress() {
		pilot.stop();
	}

}
