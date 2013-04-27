package node;

import java.util.Observable;
import java.util.Observer;

public class TestClassCallback implements Observer {
	
	private Observable observable;
	
	public TestClassCallback(Observable obs) {
		this.observable = obs;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		System.out.println("Obserwacja " + o.getClass().toString() + " " + arg.toString());
		
	}

}
