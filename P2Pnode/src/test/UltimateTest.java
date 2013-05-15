package test;

import file.FileClient;
import file.FileServer;

public class UltimateTest {
	
	public static void main(String[] args) {
		
		String usr = "usr1";
		String thisIp = "192.168.80.131";
		String ip = "192.168.80.132";
		int portIn = 5555;
		int portOut= 6666;
		String fname="kuku";
		String path="kuku//";
		
		TestTreeClient klient = new TestTreeClient(ip, portIn);
		TestTreeServer serwer = new TestTreeServer(null,fname, usr, path, portOut, thisIp);
		new Thread(klient).start();
		new Thread(serwer).start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//serwer.ft.addUser("usr2", path);
		FileServer server = new FileServer(serwer.ft, usr);
		while(true){
			if(klient.changed){
				serwer.ft.update(klient.ft.getFolder());
				klient.changed=false;
			}
		}
	}
	
}
