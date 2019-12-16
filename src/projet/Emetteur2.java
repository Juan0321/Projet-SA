package projet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import lejos.hardware.Button;
import lejos.remote.nxt.BTConnection;

public class Emetteur2 implements Runnable {
	boolean connecter=true;
	OutputStream os;
	DataOutputStream dos;
	BTConnection btc;
	
	public Emetteur2(BTConnection btc){
		this.btc=btc;
	}

	@Override
	public void run() {
		os = btc.openOutputStream();
		//DataInputStream dis = new DataInputStream(is);
		dos = new DataOutputStream(os);
		try{
			while (connecter){
				Button.RIGHT.waitForPressAndRelease();
				System.out.println("\n\nEnvoi");
				dos.write(1234); // écrit une valeur dans le flux
				dos.flush(); // force l’envoi
				System.out.println("\nEnvoyé");
			}
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
