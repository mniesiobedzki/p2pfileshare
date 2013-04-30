package node;

import java.util.Observable;
import java.util.Observer;

public class TestObserver implements Observer {
	
	private Observable observable;
	
	public TestObserver(Observable obs) {
		this.observable = obs;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("Obserwacja " + o.getClass().toString() + " " + arg.toString());
		
	}

}
