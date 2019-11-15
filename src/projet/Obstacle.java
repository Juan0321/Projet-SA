package projet;

import java.util.ArrayList;

import lejos.hardware.Button;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

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
		// TODO Auto-generated method stub
		dist.fetchSample(sample, 0);
		return sample[0]<0.08 && state[5]==1;
	}

	@Override
	public void action() {
		if(state[4]!=3){
			pilot.stop();
			state[2]=state[0];
		}
		else{
			pilot.stop();
			pilot.backward();
			Delay.msDelay(150);
			pilot.stop();
			System.out.println("obstacle");
			pilot.backward();
			while(!(color.getColor().equalsIgnoreCase("Black")));
			pilot.stop();
			state[2]=state[0];
			
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
