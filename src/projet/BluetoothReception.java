package projet;

import java.util.List;

import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class BluetoothReception implements Behavior{
	private int[] state;
	private CalibrateColor color;
	private MovePilot pilot;
	
	public BluetoothReception(int[]state,  MovePilot pilot, CalibrateColor color) {
		this.state = state;
		this.pilot = pilot;
		this.color = color;
	}

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return state[7] == 1 && state[5] == 1;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
