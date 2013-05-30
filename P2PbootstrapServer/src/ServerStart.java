/**
 * Created with IntelliJ IDEA.
 * User: marek
 * Date: 5/30/13
 * Time: 14:54
 * To change this template use File | Settings | File Templates.
 */
public class ServerStart {


    private BootstrapServerRunner bootstrapServerRunner;

    public ServerStart() {
        int bsPort = 21000;
        this.bootstrapServerRunner = new BootstrapServerRunner(bsPort);
        this.bootstrapServerRunner.start();
    }


    public static void main(String[] args) {
        System.out.println("Server STARTING");
        ServerStart server = new ServerStart();
        System.out.println("Server Started");

    }


}
