package projet;

import lejos.hardware.Button;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class DriveForward implements Behavior{
	//verif 

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
		pilot.forward();
		Delay.msDelay(500);
		pilot.stop();
		timeWait();
		state[5]=1;
		pilot.forward();
		while(pilot.isMoving());
		
	}

	@Override
	public void suppress() {
		pilot.stop();
	}
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
