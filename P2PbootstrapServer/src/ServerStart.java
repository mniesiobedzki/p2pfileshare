/**
 * Created with IntelliJ IDEA.
 * User: marek
 * Date: 5/30/13
 * Time: 14:54
 * To change this template use File | Settings | File Templates.
 */
public class ServerStart {

    private int serverPort;

    private BootstrapServerRunner bootstrapServerRunner;

    public ServerStart() {
        this.serverPort  = 21000;
        this.bootstrapServerRunner = new BootstrapServerRunner(this.serverPort);
        this.bootstrapServerRunner.start();
    }

    public ServerStart(int serverPort) {
        this.serverPort = serverPort;
        this.bootstrapServerRunner = new BootstrapServerRunner(this.serverPort);
        this.bootstrapServerRunner.start();
    }


    public static void main(String[] args) {
        System.out.println("Server STARTING");
        ServerStart server = new ServerStart();
        System.out.println("Server Started at port: " + server.getServerPort());
    }

    public int getServerPort() {
        return serverPort;
    }
}
