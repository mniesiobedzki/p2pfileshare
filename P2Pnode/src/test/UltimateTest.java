package test;

import file.FileServer;

public class UltimateTest {
	
	public static void main(String[] args) {
		
		String usr = "Marcin";
		String thisIp = "192.168.14.121";
		String ip = "192.168.14.127";
		int portIn = 5555;
		int portOut= 6666;
		int portFile= 13267;
		String fname="kuku";
		String path="kuku//";
		
		TestTreeClient klient = new TestTreeClient(ip, portIn);
		TestTreeServer serwer = new TestTreeServer(null,fname, usr, path, portFile, portOut, thisIp);
		new Thread(klient).start();
		new Thread(serwer).start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//serwer.ft.addUser("usr2", path);
		@SuppressWarnings("unused")
		FileServer server = new FileServer(serwer.ft, usr, portFile);
 		while(true){
 			if(klient.changed){
				System.err.println("Wszed³em w zmianê pliku. FolderTree -> changed = true");
 				serwer.ft.update(klient.ft.getFolder());
 				klient.changed=false;
 			}
			else{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.err.println("Nie by³o zmiany w pliku. FolderTree -> changed = false");
			}
 		}

	}
	
}
