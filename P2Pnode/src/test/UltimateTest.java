package test;

import file.FileClient;
import file.FileServer;

public class UltimateTest {
	
	public static void main(String[] args) {
		
		String usr = "heps";
		String ip = "1.1.1.12";
		int portIn = 5555;
		int portOut= 6666;
		String fname="kuku";
		String path="kuku//";
		
		TestTreeClient klient = new TestTreeClient(ip, portIn);
		TestTreeServer serwer = new TestTreeServer(fname, usr, path, portOut);
		new Thread(klient).start();
		new Thread(serwer).start();
		FileServer server = new FileServer(serwer.ft, usr);
		while(true){
			if(klient.changed){
				serwer.ft.update(klient.ft.getFolder(), usr, ip, path);
				klient.changed=false;
			}
		}
	}
	
}
