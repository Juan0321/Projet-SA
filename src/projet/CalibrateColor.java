package projet;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.RangeFinder;
import lejos.robotics.RangeFinderAdapter;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.robotics.filter.MedianFilter;
import lejos.utility.Delay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;

public class CalibrateColor {
//Attributs
	
	Properties prop = new Properties();
	/**
	 * Tableaux contenant les vecteurs RVB pour chaque couleur
	 */
	float[] white = new float[3],
			red = new float[3],
			black = new float[3],
			green = new float[3],
			blue = new float[3],
			orange = new float[3];
	/**
	 * Capteurs et leurs ports
	 */
	
	EV3ColorSensor captColor;
	
	
// Constructeurs
	/**
	 * Constructeur par défaut des trois capteurs : couleur, touché, distance
	 */
	public CalibrateColor(EV3ColorSensor colorSensor) {
		this.captColor = colorSensor;
		initCapteur();
	}
		
	/**
	 * Méthode initCateur()
	 * Utilisée par les constructeurs de Capteur
	 * Instancie les capteurs selon les ports, charge les vecteurs de couleurs depuis le fichier robo.config
	 */
	public void initCapteur() {
					
		try {
		File config = new File("iRobot.config");
		if (!config.exists())
			calibrateColor(false);
		InputStream input = new FileInputStream(config);
		prop.load(input);
		} catch (Throwable t) {
			t.printStackTrace();
			Delay.msDelay(10000);
			System.exit(0);
		}
		propertiesToTable(prop, "WHITE",white);
		propertiesToTable(prop, "RED",red);
		propertiesToTable(prop, "BLACK",black);
		propertiesToTable(prop, "GREEN",green);
		propertiesToTable(prop, "BLUE",blue);
		propertiesToTable(prop, "ORANGE",orange);
		
		
	}
	
// Utilitaires	
	/**
	 * Utilisée par toutes les méthodes de Capteur
	 * Permet d'afficher un msg sur une nouvelle ligne sur la console du robot
	 * @param msg
	 */
	public static void log(String msg)
    {
        System.out.println(msg);
    }
	
	/**
	 * Affecte les valeurs RGB d'une couleur depuis le fichier prop dans un tableau.
	 * Utilisée par initCapteur.
	 * @param prop
	 * @param color : nom de la couleur en chaîne 
	 * @param tab : tableau de floats. 
	 */
	private static void propertiesToTable(Properties prop, String color, float[] tab) {
		tab[0]=Float.parseFloat(prop.getProperty(color+"_R"));
		tab[1]=Float.parseFloat(prop.getProperty(color+"_G"));
		tab[2]=Float.parseFloat(prop.getProperty(color+"_B"));
	}
	
	/**
	 * Utilisée par getColor() et calibrateColor().
	 * @param v1 : un tableau de float non nuls.
	 * @param v2 : un tableau de float non nuls.
	 * @return le produit scalaire (double) des deux tableaux de float en paramètres.
	 */
	private static double scalaire(float[] v1, float[] v2) {
		return Math.sqrt (Math.pow(v1[0] - v2[0], 2.0) +
				Math.pow(v1[1] - v2[1], 2.0) +
				Math.pow(v1[2] - v2[2], 2.0));
	}
	
	
	
// Accesseurs

	/**
	 * @return la couleur (String) détectée par le capteur de couleur
	 * par minimum du produit scalaire entre la couleur détectée et les
	 * valeurs enrgistrées dans robot.config.
	 */
	public String getColor() {
		
		SampleProvider average = new MeanFilter(captColor.getRGBMode(), 10);
		float[] sample = new float[average.sampleSize()];
		average.fetchSample(sample, 0);
		double minscal = Double.MAX_VALUE;
		String color = "";
		
		
				double scalaire = scalaire(sample, blue);
				
				if (scalaire < minscal) {
					minscal = scalaire;
					color = "blue";
				}
				
				scalaire = scalaire(sample, red);
				if (scalaire < minscal) {
					minscal = scalaire;
					color = "red";
				}
				
				scalaire = scalaire(sample, green);
				if (scalaire < minscal) {
					minscal = scalaire;
					color = "green";
				}
				
				scalaire = scalaire(sample, black);
				if (scalaire < minscal) {
					minscal = scalaire;
					color = "black";
				}
				
				scalaire = scalaire(sample, orange);
				if (scalaire < minscal) {
					minscal = scalaire;
					color = "orange";
				}
				
				scalaire = scalaire(sample, white);
				if (scalaire < minscal) {
					minscal = scalaire;
					color = "white";
				}
				
				return color;
	}

// ToString
	
	/**
	 * Méthode toString() pour capteur de couleur
	 * @return une String au format "Color : Valeur"
	 */
	public String colorToString() {
		return "Color : "+getColor();
	}
		
// Afficheurs
	
	public void printColor(){
		log("Valeur du capteur de couleurs, en Mode = " + captColor.getName());
		log("ENTRER pour demarrer, BAS pour stopper");
		Button.ENTER.waitForPress();
		while (Button.DOWN.isUp()) {
			Delay.msDelay(500);
			log(colorToString());
		}	
	}
	
	
// Avancées
	
	/**
	 * Enregistre pour chaque couleur détectée un vecteur RVB enregistré dans robo.config.
	 * @param verification : si TRUE, lance une série de vérifications.
	 */
	public void calibrateColor(boolean verification) {
		Properties prop = new Properties();
		FileOutputStream out = null;
				
		try {
			out = new FileOutputStream("iRobot.config");	
						
			SampleProvider average = new MeanFilter(captColor.getRGBMode(), 1);//esssayer avec d'autres longueurs?
			
			System.out.println("Press enter to calibrate white...");
			Button.ENTER.waitForPressAndRelease();
			float[] white = new float[average.sampleSize()];
			average.fetchSample(white, 0);
			prop.setProperty("WHITE_R", Float.toString(white[0]));
			prop.setProperty("WHITE_G", Float.toString(white[1]));
			prop.setProperty("WHITE_B", Float.toString(white[2]));
			
			System.out.println("Press enter to calibrate blue...");
			Button.ENTER.waitForPressAndRelease();
			float[] blue = new float[average.sampleSize()];
			average.fetchSample(blue, 0);
			prop.setProperty("BLUE_R", Float.toString(blue[0]));
			prop.setProperty("BLUE_G", Float.toString(blue[1]));
			prop.setProperty("BLUE_B", Float.toString(blue[2]));
			
			System.out.println("Press enter to calibrate orange...");
			Button.ENTER.waitForPressAndRelease();
			float[] orange = new float[average.sampleSize()];
			average.fetchSample(orange, 0);
			prop.setProperty("ORANGE_R", Float.toString(orange[0]));
			prop.setProperty("ORANGE_G", Float.toString(orange[1]));
			prop.setProperty("ORANGE_B", Float.toString(orange[2]));
			
			System.out.println("Press enter to calibrate green...");
			Button.ENTER.waitForPressAndRelease();
			float[] green = new float[average.sampleSize()];
			average.fetchSample(green, 0);
			prop.setProperty("GREEN_R", Float.toString(green[0]));
			prop.setProperty("GREEN_G", Float.toString(green[1]));
			prop.setProperty("GREEN_B", Float.toString(green[2]));
			
			System.out.println("Press enter to calibrate black...");
			Button.ENTER.waitForPressAndRelease();
			float[] black = new float[average.sampleSize()];
			average.fetchSample(black, 0);
			prop.setProperty("BLACK_R", Float.toString(black[0]));
			prop.setProperty("BLACK_G", Float.toString(black[1]));
			prop.setProperty("BLACK_B", Float.toString(black[2]));
			
			System.out.println("Press enter to calibrate red...");
			Button.ENTER.waitForPressAndRelease();
			float[] red = new float[average.sampleSize()];
			average.fetchSample(red, 0);
			prop.setProperty("RED_R", Float.toString(red[0]));
			prop.setProperty("RED_G", Float.toString(red[1]));
			prop.setProperty("RED_B", Float.toString(red[2]));
			
			prop.store(out, null);
			System.out.println("Press ANY BUTTON to continue \n");
			
			while (verification) {
				
				
				Button.ENTER.waitForPressAndRelease();
				
				System.out.println("The color is " + getColor() + " \n");
				System.out.println("Press ENTER to continue \n");
				System.out.println("ESCAPE to exit");
				Button.waitForAnyPress();
				if(Button.ESCAPE.isDown()) {
					captColor.setFloodlight(false);
					verification = false;
				}
			}
			
			
		} catch (Throwable t) {
			t.printStackTrace();
			Delay.msDelay(10000);
			System.exit(0);
		}
	}
	
}

