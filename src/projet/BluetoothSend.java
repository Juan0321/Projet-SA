package projet;

import java.util.ArrayList;

import lejos.robotics.subsumption.Behavior;

public class BluetoothSend implements Behavior{
	private ArrayList<Integer> map = new ArrayList(35);
	private int[] state;
	
	public BluetoothSend(int[]state, ArrayList<Integer> map) {
		this.map = map;
		this.state = state;
	}

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
