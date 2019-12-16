package projet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;
import lejos.utility.Delay;

public class BTMain {
	static boolean GDLN=false;
	public static void main(String[] args) {
		String john="00:16:53:43:8E:49";
		String sansa="";
		String ed="00:16:53:43:AD:EE";
		String adresse=ed;
		
		BTConnector bt = new BTConnector();
		BTConnection btc=null;
		if (GDLN){
			//Delay.msDelay(2000);
			//System.out.println("ici");
			//Button.RIGHT.waitForPressAndRelease();
			btc = bt.connect("adresse", NXTConnection.PACKET);
			//Recepteur2 r=new Recepteur2(btc);
			Delay.msDelay(2000);
			Thread t1 = new Thread(new Recepteur2(btc));
			System.out.println("t1");
			
			Thread t2 = new Thread(new Emetteur2(btc));
			System.out.println("t2");
			t1.start();
	        t2.start();
			/*OutputStream os = qtc.openOutputStream();
			//DataInputStream dis = new DataInputStream(is);
			DataOutputStream dos = new DataOutputStream(os);
			System.out.println("\n\nEnvoi");
			try {
				for(int i=0;i<6;i++){
					dos.write(i);
					dos.flush(); // force l’envoi
					System.out.println("\nEnvoyé");
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // écrit une valeur dans le flux
			Button.ENTER.waitForPressAndRelease();*/
			
		}else{
			//Button.RIGHT.waitForPressAndRelease();
			int i=0;
			while (btc == null || i<10){
				btc = bt.waitForConnection(10000, NXTConnection.PACKET);
				i++;
			}
			//System.out.println("ici");
			//Delay.msDelay(2000);
			//Emetteur2 e=new Emetteur2(btc);
			Thread t1 = new Thread(new Recepteur2(btc));
			System.out.println("t1");
			Thread t2 = new Thread(new Emetteur2(btc));
			t1.start();
	        t2.start();
			/*InputStream is = btc.openInputStream();
			//OutputStream os = btc.openOutputStream();
			DataInputStream dis = new DataInputStream(is);
			//DataOutputStream dos = new DataOutputStream(os);
			try {
				int valeur = dis.read();
				
			System.out.println(valeur);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Button.ENTER.waitForPressAndRelease();*/

			
		}
	}

}
