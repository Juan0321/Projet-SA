package projet;

import java.util.ArrayList;

import lejos.hardware.Button;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class Obstacle implements Behavior{
	private EV3UltrasonicSensor dist;
	private ArrayList<Integer> map;
	int[]state;
	private MovePilot pilot;
	private float[] sample= new float[1];
	

	public Obstacle(EV3UltrasonicSensor dist,ArrayList<Integer> map, int[]state, MovePilot pilot){
		this.dist=dist;
		this.map = map;
		this.state = state;
		this.pilot = pilot;
	}
	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		dist.fetchSample(sample, 0);
		return sample[0]<0.05;
	}

	@Override
	public void action() {
		pilot.stop();
		System.out.println("obstacle");
		Button.RIGHT.waitForPressAndRelease();
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}
}
