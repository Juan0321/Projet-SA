package projet;

import java.util.List;

import lejos.hardware.Button;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

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
		return (state[7] == 1 && state[5] == 1);
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		pilot.stop();
		pilot.backward();
		Delay.msDelay(150);
		pilot.stop();
		pilot.backward();
		while(!(color.getColor().equalsIgnoreCase("Black")));
		pilot.stop();
		
		/* remplace la coordonnée de destination state[2] par la coordonnée actuelle du robot state[0] pour qu'il puisse rentrer 
		 * dans le comportement GoTO et calculer un nouveau chemin en fonction de l'obstacle. */
		state[2]=state[0]; 
		state[7]=0;
		
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
