package projet;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;

public class Recepteur2 implements Runnable {
	Boolean connecter= true;
	InputStream is ;
	DataInputStream dis;
	BTConnection btc;
	public Recepteur2(BTConnection btc){
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
			int valeur;	
			valeur = dis.read();
			System.out.println(valeur);
		}
		dis.close();
		Button.RIGHT.waitForPressAndRelease();
		LCD.clear();
		} catch (IOException e) {
			e.printStackTrace();
			
		}	
		
	}

}
