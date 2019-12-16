package projet;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;

public class Recepteur2 implements Runnable {
	private ArrayList<Integer> map;

	Boolean connecter= true;
	InputStream is ;
	DataInputStream dis;
	BTConnection btc;
	private int[] state;
	boolean valeur;
	int position;
	public Recepteur2(BTConnection btc, int[] state, ArrayList<Integer> map){
		this.map =map;
		this.state = state;
		if (btc !=null) {
			this.btc=btc;
		}
		else{
			System.out.println("Pas de connexion");
		}
		
	}
	@Override
	public void run() {
		is = btc.openInputStream();
		dis = new DataInputStream(is);
		try {
		while (connecter){	
			valeur = dis.readBoolean();
			if (valeur){
				position= dis.readInt();
				state[6] = position;
				state[7]= 1;		
			}else{
				for (int i= 0; i<35; i++){
					position = dis.readInt();
					if (position!= 0){
						map.set(i, position);
					}
				}
				position= dis.readInt();
				state[6] = position;
				state[7]= 1;
			}
		}
		dis.close();
		LCD.clear();
		} catch (IOException e) {
			e.printStackTrace();
			
		}	
		
	}

}
