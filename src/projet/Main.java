package projet;

import java.io.*; 
import java.util.ArrayList;
import java.util.List;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import projet.StopBehavior;
import projet.VerifLocalisation;

public class Main {	
	
	private ArrayList<Integer> map = new ArrayList(35);
	
	/*
	 * contient la position du robot, sa direction et les coordonnées de destination
	 */
	private int[] state = new int[3];
	private List<Integer> path;
	
	Port portRoueD;
	Port portRoueG;
	Port portDistance;
	Port portTouch;
	Port portColor;
	EV3UltrasonicSensor ultrasonicSensor;
	EV3TouchSensor touchSensor;
	EV3ColorSensor colorSensor;
	EV3LargeRegulatedMotor roueD; 
	EV3LargeRegulatedMotor roueG;
	Wheel wheel1;
	Wheel wheel2;
	Chassis chassis; 
	MovePilot pilot;
	
	public static void main(String [] args) {
		Main m = new Main();
		m.initialise();		
		
	   }
	
	
	public void initialise() {
		//map Sauvage (blanc=0, vert=1, bleu=2, orange=3, rouge=4)
		map.add(4);map.add(2);map.add(1);map.add(1);map.add(0);
		map.add(0);map.add(2);map.add(1);map.add(1);map.add(1);
		map.add(0);map.add(2);map.add(2);map.add(1);map.add(03);
		map.add(0);map.add(0);map.add(2);map.add(1);map.add(1);
		map.add(0);map.add(3);map.add(3);map.add(3);map.add(1);
		map.add(0);map.add(0);map.add(0);map.add(0);map.add(2);
		map.add(0);map.add(0);map.add(0);map.add(0);map.add(2);
		
		//map garde de la nuit (blanc=0, vert=1, bleu=2, orange=3, rouge=4)
		/*map.add(4);map.add(2);map.add(0);map.add(0);map.add(0);
		map.add(1);map.add(2);map.add(0);map.add(0);map.add(0);
		map.add(1);map.add(2);map.add(2);map.add(0);map.add(0);
		map.add(1);map.add(1);map.add(2);map.add(0);map.add(0);
		map.add(1);map.add(3);map.add(3);map.add(3);map.add(0);
		map.add(1);map.add(1);map.add(1);map.add(4);map.add(2);
		map.add(1);map.add(1);map.add(1);map.add(1);map.add(2);*/
		
		portRoueD= LocalEV3.get().getPort("C");
		portRoueG= LocalEV3.get().getPort("B");
		portDistance= LocalEV3.get().getPort("S4");
		portTouch = LocalEV3.get().getPort("S1");
		portColor = LocalEV3.get().getPort("S3");
		
		EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(portDistance);
		EV3TouchSensor touchSensor = new EV3TouchSensor(portTouch);
		EV3ColorSensor colorSensor = new EV3ColorSensor(portColor);
		EV3LargeRegulatedMotor roueD = new EV3LargeRegulatedMotor(portRoueD);
		EV3LargeRegulatedMotor roueG = new EV3LargeRegulatedMotor(portRoueG);
		Wheel wheel1 = WheeledChassis.modelWheel(roueD, 56).offset(-61);
		Wheel wheel2 = WheeledChassis.modelWheel(roueG, 56).offset(61);
		Chassis chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2}, 2); 
		MovePilot pilot = new MovePilot(chassis);
		Object[] para = new Object[] {map, state, path, pilot, colorSensor, ultrasonicSensor, touchSensor};
		float[] sample = new float[4];
		
		Behavior b2 = new VerifLocalisation(map, state, path, colorSensor, pilot);
	    Behavior b1 = new DriveForward(pilot);
	    Behavior b3 = new GoTo(map, state, path);
	    Behavior b4 = new NextStep();
	    //Behavior b2 = new BatteryLow(6.5f);
	    Behavior b5 = new StopBehavior(pilot, ultrasonicSensor, touchSensor);
	    Behavior [] bArray = {b3, b2, b1, b4, b5};
	    Arbitrator arbi = new Arbitrator(bArray);
	      
	    if(b4 instanceof StopBehavior) {
	    	StopBehavior b = (StopBehavior)b4;
	    	b.setter(arbi);
	    }
	    arbi.go();
		 
		
		
	}
}
