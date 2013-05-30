import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: marek niesiobedzki
 * Date: 5/30/13
 * Time: 14:54
 * To change this template use File | Settings | File Templates.
 */
public class ServerStart {

    public static final Logger LOG = Logger.getLogger(ServerStart.class);
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

    public void machineInfo(){
        LOG.info("Operating system name: " + System.getProperty("os.name"));
        LOG.info("Operating system version: " + System.getProperty("os.version"));
        LOG.info("Java version: " +  System.getProperty("java.version"));
    }
}
