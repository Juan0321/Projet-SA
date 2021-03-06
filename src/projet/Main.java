package projet;

import java.awt.Color;
import java.io.*; 
import java.util.ArrayList;
import java.util.List;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
import projet.StopBehavior;
import projet.VerifLocalisation;

public class Main {	
	//cette variable stock la carte o� les robot vont ce deplace.
	//chaque case de la carte est associe a un index de la liste.
	// si la case est blanche ou innconnu = 0, vert = 1, bleu = 2, orange = 3, rouge = 4 et si la case contient un obstacle = 5
	private ArrayList<Integer> map = new ArrayList(35);


	// contient la position du robot, sa direction, les coordonn�es de destination, le camp du robot(sauvageons/Garde de la nuit), ca mission 
	// et s'il est entraint de rouler[1] ou pas[0])
	/* state[0]:position du robot
	 * state[1]: la direction
	 * state[2]: les coordonn�es de destination
	 * state[3]: le camp du robot(sauvageons=0/Garde de la nuit=1)
	 * state[4]: la mission du robot
	 * state[5]: le robot peut rentrer dans VerifLocalisation[1] ou pas[0]
	 * state[6]: la position du robot adverse (obstacle)
	 * state[7]: bool�en disant si un message est re�u (bluetooth) 0=false, 1=true
	 * state[8]: nombre de cases parcourues depuis le dernier envoie de sa position
	 */
	private int[] state = new int[9];
	private List<Integer> path= new ArrayList<Integer>();

	//Varialbes necessaires pur utilises les senseurs et les moteurs du robot
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

	//variables pour stockes les adresse des robot utiliser dans le bluetooh
	String john="00:16:53:43:8E:49";
	String sansa="00:53:43:96:91";
	String ed="00:16:53:43:AD:EE";
	String margaery="00:16:53:43:37:FC";
	String cersei="00:16:53:43:63:D4";
	String daenerys="00:16:53:43:9E:2F";
	String adresse=ed;
	
	//variable necessaire pour le bleutooh
	BTConnector bt = new BTConnector();
	BTConnection btc = null;

	
	public static void main(String [] args) {
		Main m = new Main();
		Button.ENTER.waitForPressAndRelease();
		m.initialise();		
		
	   }
	
	/**
	 * initialisation des capteurs, moteurs ainsi que des behavior (cr�ation de l'arbitrator)
	 */
	public void initialise() {
		state[3]=0;//0 Pour etre du cote des Sauvage et 1 pour etre du cote de la garde de la nuit
		state[5]=1;//par defaut il avance
		
		/* state[4]=
		 * 0 pour aller au camp militaire le plus proche
		 * 1 pour retourne a notre ville
		 * 2 pour aller a la ville adverse 
		 * 3 pour le modele proie-pr�dateur
		 */
		state[4]=3;
		
		//--SAUVAGE--//
		if (state[3]==0){
			//map Sauvage (blanc=0, vert=1, bleu=2, orange=3, rouge=4)
			map.add(4);map.add(2);map.add(1);map.add(1);map.add(0);
			map.add(0);map.add(2);map.add(1);map.add(1);map.add(1);
			map.add(0);map.add(2);map.add(2);map.add(1);map.add(3);
			map.add(0);map.add(0);map.add(2);map.add(1);map.add(1);
			map.add(0);map.add(3);map.add(3);map.add(3);map.add(1);
			map.add(0);map.add(0);map.add(0);map.add(0);map.add(2);
			map.add(0);map.add(0);map.add(0);map.add(0);map.add(2);
			
			//depart case 0, orient� vers la gauche (270)
			state[0]=4;state[1]=270;state[2]=4;state[6]=30;
			
			//Cette variable attend la demande de connection de l'autre robot
			btc = bt.waitForConnection(10000, NXTConnection.PACKET);
			
			Thread t1 = new Thread(new Recepteur2(btc, state, map));
			System.out.println("t1");
			t1.start();
	      
		}
		
		//--GARDE DE LA NUIT--//
		if(state[3]==1){
			//map garde de la nuit (blanc=0, vert=1, bleu=2, orange=3, rouge=4)
			map.add(4);map.add(2);map.add(0);map.add(0);map.add(0);
			map.add(1);map.add(2);map.add(0);map.add(0);map.add(0);
			map.add(1);map.add(2);map.add(2);map.add(0);map.add(0);
			map.add(1);map.add(1);map.add(2);map.add(0);map.add(0);
			map.add(1);map.add(3);map.add(3);map.add(3);map.add(0);
			map.add(1);map.add(1);map.add(1);map.add(4);map.add(2);
			map.add(1);map.add(1);map.add(1);map.add(1);map.add(2);
			
			//depart case 30, orient� vers la droite (90)
			state[0]=30;state[1]=90;state[2]=30;state[6]=4;
			
			//envoi une demande de connection a l'adresse stocke dans la varialbe adresse
			while(btc == null) {
				btc = bt.connect(adresse, NXTConnection.PACKET);
			}
			
			Thread t1 = new Thread(new Recepteur2(btc, state, map));
			t1.start();
		}
		//initialisation des diff�rent capteurs et moteurs
		portRoueD= LocalEV3.get().getPort("C");
		portRoueG= LocalEV3.get().getPort("B");
		portDistance= LocalEV3.get().getPort("S4");
		portTouch = LocalEV3.get().getPort("S1");
		portColor = LocalEV3.get().getPort("S3");
		
		EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(portDistance);
		EV3TouchSensor touchSensor = new EV3TouchSensor(portTouch);
		
		EV3ColorSensor colorSensor = new EV3ColorSensor(portColor);
		colorSensor.setFloodlight(lejos.robotics.Color.WHITE);
		CalibrateColor color = new CalibrateColor(colorSensor);
		
		EV3LargeRegulatedMotor roueD = new EV3LargeRegulatedMotor(portRoueD);
		EV3LargeRegulatedMotor roueG = new EV3LargeRegulatedMotor(portRoueG);
		Wheel wheel1 = WheeledChassis.modelWheel(roueD, 56).offset(-52.25);
		Wheel wheel2 = WheeledChassis.modelWheel(roueG, 56).offset(52.25);
		Chassis chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2}, 2); 
		MovePilot pilot = new MovePilot(chassis);
		pilot.setAngularSpeed(40);
		pilot.setLinearAcceleration(1);
		pilot.setLinearSpeed(50);
	
		Object[] para = new Object[] {map, state, path, pilot, colorSensor, ultrasonicSensor, touchSensor};
		System.out.println("pret!");
		Button.DOWN.waitForPress();		
		
		//initialisation des behaviors
	    Behavior b1 = new DriveForward(pilot,state, color);
	    Behavior b2 = new VerifLocalisation(map, state, path, color, pilot);
	    Behavior b6 = new BluetoothSend(state, map, btc);
	    Behavior b3 = new GoTo(map, state, path, pilot);
	    Behavior b4 = new Obstacle(ultrasonicSensor, map, state, pilot, color);
	    Behavior b5 = new StopBehavior(pilot, ultrasonicSensor, touchSensor);
	    Behavior [] bArray = { b1, b2, b3,b6, b4, b5};
	    Arbitrator arbi = new Arbitrator(bArray);
	      
	    if(b5 instanceof StopBehavior) {
	    	StopBehavior b = (StopBehavior)b5;
	    	b.setter(arbi);
	    }
	    arbi.go();		
	}
}
