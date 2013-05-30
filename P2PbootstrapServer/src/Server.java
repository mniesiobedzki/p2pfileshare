public class Server {
	
	private BootstrapServerRunner bs;

	public Server() {
		int bsPort = 21000;
		this.bs = new BootstrapServerRunner(bsPort);
		this.bs.start();
	}
	
	public static void main(String[] args) {
        System.out.println("Server STARTING");
        Server server = new Server();
        System.out.println("Server Started");
    }
}
