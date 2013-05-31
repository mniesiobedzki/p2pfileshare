package test;

import java.util.HashMap;

public class BootstrapClientTest {

	public static void main(String[] args) {
		
		String usr = "Marcin";
		int portFile= 13267;
		int bootstrapPort= 1025;
		int portOut= 6666;
		String bootstrapIp = "192.168.80.132";
		HashMap<String,User> users= new HashMap<String,User>();
		User u = new User(usr, bootstrapIp, portFile, portOut);
		users.put(usr, u);
		NodTest nod = new NodTest(bootstrapIp, bootstrapPort, users, u);
	}
}
