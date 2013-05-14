package test;

public class UltimateTest {
	
	public static void main(String[] args) {
		
		TestTreeClient klient = new TestTreeClient("127.0.0.1", 5555);
		TestTreeServer serwer = new TestTreeServer("kuku", "panSerwer", "kuku//", 5555);
		
		serwer.run();
		klient.run();
		
	}
	
}
