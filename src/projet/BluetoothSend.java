package projet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import lejos.hardware.Button;
import lejos.remote.nxt.BTConnection;
import lejos.robotics.subsumption.Behavior;

public class BluetoothSend implements Behavior{
	private ArrayList<Integer> map = new ArrayList(35);
	private int[] state;
	BTConnection btc;
	boolean connecter=true;
	OutputStream os;
	DataOutputStream dos;
	
	public BluetoothSend(int[] state, ArrayList<Integer> map, BTConnection btc) {
		this.state = state;
		this.map = map;
		if (btc !=null) {
			this.btc=btc;
		}
		else{
			System.out.println("Pas de connexion");
		}
	}

	@Override
	public boolean takeControl() {
		return state[8] == 2 || Button.UP.isDown();
	}

	@Override
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
				dos.writeInt(map.get(i));
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
