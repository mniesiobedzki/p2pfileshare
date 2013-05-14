public class Server {
	
	private BootstrapServerRunner bs;

	public Server() {
		int bsPort = 21000; // c r e a t e s s impl e boot s t rap s e r v e r
		this.bs = new BootstrapServerRunner(bsPort); // run bs
		this.bs.start();
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Server server = new Server();
	}
}
