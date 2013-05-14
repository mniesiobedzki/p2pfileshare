package test;

public class UltimateTest {
	
	public static void main(String[] args) {
		
		TestTreeClient klient = new TestTreeClient("1.1.1.20", 6666);
		TestTreeServer serwer = new TestTreeServer("kuku", "serwerMichala", "kuku//", 5555);
		
		new Thread(klient).start();
		new Thread(serwer).start();
	}
	
}
