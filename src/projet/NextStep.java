package projet;


import lejos.robotics.subsumption.Behavior;

/*
 * modifie l'orientation du robot en fonction de la position du robot et de la prochaine coordonnée dans la liste path 
 */
public class NextStep implements Behavior{
	
	private float[] state = new float[3];
	
	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
