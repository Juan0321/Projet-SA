package projet;

import lejos.hardware.Button;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class StopBehavior implements Behavior {

	private EV3UltrasonicSensor dist;
	private EV3TouchSensor touch;
	private MovePilot pilot;
	private Arbitrator arbi;
	
	public StopBehavior(MovePilot pilot, EV3UltrasonicSensor dist, EV3TouchSensor touch) {
		this.pilot = pilot;
		this.dist = dist;
		this.touch = touch;
	}
	
	@Override
	public boolean takeControl() {
		return Button.ENTER.isDown();
	}

	@Override
	public void action() {
		pilot.stop();
		dist.close();
		touch.close();
		arbi.stop();
		System.exit(0);		
	}

	@Override
	public void suppress() {
		
	}
	public void setter(Arbitrator arbi) {
		this.arbi = arbi;
	}

}
