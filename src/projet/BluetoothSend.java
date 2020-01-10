package projet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import lejos.hardware.Button;
import lejos.remote.nxt.BTConnection;
import lejos.robotics.subsumption.Behavior;
/**
 * Permet d'envoyer un message a l'autre robot
 */
public class BluetoothSend implements Behavior{
	private ArrayList<Integer> map = new ArrayList(35);
	private int[] state;
	BTConnection btc;
	boolean connecter=true;
	OutputStream os;
	DataOutputStream dos;
	/*
	 * initialise les éléments nécessaire pour envoyer un message à l'autre robot
	 */
	public BluetoothSend(int[] state, ArrayList<Integer> map, BTConnection btc) {
		this.state = state; // permet de récupérer la position actuelle du robot pour l'envoyer
		this.map = map; //permet de récupéré les cases connu par le robot
		if (btc !=null) {
			this.btc=btc;//permet de récupérer la connexion avec l'autre robot pour envoyer des données
		}
		else{
			System.out.println("Pas de connexion");
		}
	}

	@Override
	/*rentre dans le comportement lorsque le robot à parcouru 2 cases depuis le dernier envoie ou lorsque le bouton du haut est pressé
	 * (non-Javadoc)
	 * @see lejos.robotics.subsumption.Behavior#takeControl()
	 */
	public boolean takeControl() {
		return state[8] == 2 || Button.UP.isDown();
	}

	@Override
	/*
	 * envoi différente données selon que:
	 * le bouton du haut est pressé -> envoi la carte du robot  
	 * le robot à parcouru 2 cases -> envoi la position du robot
	 * (non-Javadoc)
	 * @see lejos.robotics.subsumption.Behavior#action()
	 */
	public void action() {
		if(state[4]==3 && state[8]==2){
		os = btc.openOutputStream();
		//DataInputStream dis = new DataInputStream(is);
		dos = new DataOutputStream(os);
		try{
			while (connecter){
				Button.RIGHT.waitForPressAndRelease();
				System.out.println("\n\nEnvoi");
				dos.writeBoolean(true);
				dos.writeInt(state[0]); // écrit une valeur dans le flux
				dos.flush(); // force l’envoi
				System.out.println("\nEnvoyé");
			}
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		state[8]=0;
		}else{
			os = btc.openOutputStream();
			//DataInputStream dis = new DataInputStream(is);
			dos = new DataOutputStream(os);
			String s = "";
			
			try{
				dos.writeBoolean(false);
			for(int i =0; i<map.size(); i++){
				s += map.get(i);
				dos.writeInt(map.get(i));// écrit une valeur dans le flux pour chaque valeur de la carte
			}		
				dos.flush(); // force l’envoi
				dos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
